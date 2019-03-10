package com.cky.sparkproject.spark.session;

import com.alibaba.fastjson.JSONObject;
import com.cky.sparkproject.conf.ConfigurationManager;
import com.cky.sparkproject.constants.Constants;
import com.cky.sparkproject.dao.*;
import com.cky.sparkproject.domain.*;
import com.cky.sparkproject.test.MockData;
import com.cky.sparkproject.utils.*;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.*;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.*;

/**
 * 用户访问session分析spark作业
 * 接收用户创建的分析任务，用户可能指定的条件如下：
 *
 * 1、时间范围：起始日期~结束日期
 * 2、性别：男或女
 * 3、年龄范围
 * 4、职业：多选
 * 5、城市：多选
 * 6、搜索词：多个搜索词，只要某个session中的任何一个action搜索过指定的关键词，那么session就符合条件
 * 7、点击品类：多个品类，只要某个session中的任何一个action点击过某个品类，那么session就符合条件
 *
 *  2.0 基础：session粒度聚合、按筛选条件进行过滤
    2.1 session聚合统计：统计出访问时长和访问步长，各个区间范围的session数量，占总session数量的比例
    2.2 session随机抽取：按时间比例，随机抽取出100个session
    2.3 top10热门品类：获取通过筛选条件的session，点击、下单和支付次数最多的10个品类
    2.4 top10活跃session：获取top10热门品类中，每个品类点击次数最多的10个session

 */
public class UserVisitSessionAnalyzeSpark {

    public static void main(String[] args){

        SparkConf conf = new SparkConf()
                            .setAppName(Constants.SPARK_APP_NAME_SESSION)
                            ;
        SparkUtils.setMaster(conf);

        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());

        //生成模拟测试数据u
       SparkUtils.mockData(sc,sqlContext);

        //创建需要使用的Dao组件
        ITaskDAO taskDAO = DAOFactory.getTaskDAO();

        //首先查询出来指定的任务
        Long taskId = ParamUtils.getTaskIdFromArgs(args,Constants.SPARK_LOCAL_TASKID_SESSION);
        Task task = taskDAO.findById(taskId);
        if(task == null){
            System.out.println(new Date()+":找不到taskId对应的task");
            return;
        }
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());
        //如果要进行session力度的数据聚合，
        //首先要从user_visit_action表中，查询出指定日期范围内的行为数据
//        JavaRDD<Row> actionRDD = getActionRDDByDateRange(sqlContext, taskParam);
        JavaRDD<Row> actionRDD = SparkUtils.getActionRDDByDateRange(sqlContext, taskParam);

        actionRDD.persist(StorageLevel.MEMORY_ONLY());

        JavaPairRDD<String, Row> sessiondi2ActionRDD = getSessiondi2ActionRDD(actionRDD);

        sessiondi2ActionRDD = sessiondi2ActionRDD.persist(StorageLevel.MEMORY_ONLY());

        // 首先，可以将行为数据，按照session_id进行groupByKey分组
        // 此时的数据的粒度就是session粒度了，然后呢，可以将session粒度的数据
        // 与用户信息数据，进行join
        // 然后就可以获取到session粒度的数据，同时呢，数据里面还包含了session对应的user的信息

        JavaPairRDD<String, String> sessionid2AggrInfoRDD =
                aggregateBySession(actionRDD, sqlContext);


        //重构，同时进行过滤和统计
        Accumulator<String> accumulator = sc.accumulator("",new SessionAggrStatAccumulator());
        //针对session粒度的聚合数据，按照使用者指定的筛选参数进行数据过滤
        JavaPairRDD<String, String> filteredSessionid2AggrInfoRDD =
                filterSessionAndAggrStat(sessionid2AggrInfoRDD, taskParam,accumulator);

        filteredSessionid2AggrInfoRDD = filteredSessionid2AggrInfoRDD.persist(StorageLevel.MEMORY_ONLY());


        JavaPairRDD<String, Row> sessionid2detailRDD = getSessionid2detailRDD(filteredSessionid2AggrInfoRDD, sessiondi2ActionRDD);

        sessionid2detailRDD = sessionid2detailRDD.persist(StorageLevel.MEMORY_ONLY());

        /**
         *
         */
        randomExtractSession(filteredSessionid2AggrInfoRDD,taskId,sessiondi2ActionRDD,sc);

        // 计算出各个范围的session占比，并写入MySQL
        calculateAndPersistAggrStat(accumulator.value(),
                task.getTaskId());

        /**
         * 获取top10热门品类
         */
        List<Tuple2<CatetorySortKey, String>> top10Category = getTop10Category(sessionid2detailRDD, taskId);
        /**
         * 获取top活跃session
         */
        getTop10Session(taskId,sessionid2detailRDD,top10Category,sc);

        sc.close();
    }


    /**
     * 获取SQLContext
     * 如果是在本地测试环境的话，那么就生成SQLContext对象
     * 如果是在生产环境运行的话，那么就生成HiveContext对象
     * @param sc
     * @return
     */
    private static SQLContext getSQLContext(SparkContext sc){
        Boolean isLocal = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if(isLocal){
            return new SQLContext(sc);
        }else{
            return new HiveContext(sc);
        }
    }

    /**
     * 生成模拟数据（只有本地模式才会生成模拟数据）
     * @param sc
     * @param sqlContext
     */
    private static void mockData(JavaSparkContext sc,SQLContext sqlContext){
        Boolean isLocal = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if(isLocal){
            MockData.mock(sc,sqlContext);
        }
    }

    /**
     * 获取指定日期范围内的用户访问行为数据
     * @param sqlContext
     * @param taskParam
     * @return
     */
    private static JavaRDD<Row> getActionRDDByDateRange(
            SQLContext sqlContext,JSONObject taskParam){
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);
        String sql = "select * " +
                     "from user_visit_action " +
                    " where date>='"+startDate+"'" +
                    " and date<='"+endDate+"'";
//        sql = "select * from user_info";
        Dataset<Row> rows = sqlContext.sql(sql);
        /**
         * 生产环境中要重分区
         */
//        return rows.javaRDD().repartition(100);
        return rows.javaRDD();
    }

    /**
     * 获取sessionid到访问行为数据的映射RDD
     * @param actionRDD
     * @return
     */
    public static JavaPairRDD<String,Row> getSessiondi2ActionRDD(JavaRDD<Row> actionRDD){

//        return actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
//            @Override
//            public Tuple2<String, Row> call(Row row) throws Exception {
//                return new Tuple2<String, Row>(row.getString(2),row);
//            }
//        });

        return actionRDD.mapPartitionsToPair(
                new PairFlatMapFunction<Iterator<Row>, String, Row>() {
                    @Override
                    public Iterator<Tuple2<String, Row>> call(Iterator<Row> rowIterator) throws Exception {
                        List<Tuple2<String, Row>> list = new ArrayList<Tuple2<String, Row>>();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();
                            list.add(new Tuple2<String, Row>(row.getString(2), row));
                        }
                        return list.iterator();
                    }
                });
    }


    /**
     * 对行为数据按session粒度进行聚合
     * @param actionRDD
     * @return
     */
    private static JavaPairRDD<String,String>
    aggregateBySession(JavaRDD<Row> actionRDD,SQLContext sqlContext){

        //将row映射成<sessionid,Row>的格式
        JavaPairRDD<String, Row> sessionid2ActionRDD = actionRDD.mapToPair(
                /**
                 * 第一个参数，相当于是函数的输入
                 * 第二个参数和第三个参数，相当于函数的输出
                 */
                new PairFunction<Row, String, Row>() {

                    @Override
                    public Tuple2<String, Row> call(Row row) throws Exception {

                        return new Tuple2<String, Row>(row.getString(2), row);
                    }
                });

        //对行为数据按session粒度进行分组
        JavaPairRDD<String, Iterable<Row>> sessionid2ActionsRDD = sessionid2ActionRDD.groupByKey();

        //对每一个session分组进行聚合，将session中所有的搜索词和点击品类都聚合起来
        // 数据格式为 <userId,partAggInfo(sessionid,searchKeywords,clickCategoryIds)>
        JavaPairRDD<Long, String> userid2PartAggrInfoRDD = sessionid2ActionsRDD.mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, Long, String>() {
            @Override
            public Tuple2<Long, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                String sessionid = tuple._1;
                Iterator<Row> iterator = tuple._2.iterator();

                StringBuffer keywordBuffer = new StringBuffer("");
                StringBuffer categoryIdsBuffer = new StringBuffer("");

                Long userId = null;

                //session的起始时间
                Date startTime = null;
                Date endTime = null;
                //session的访问步长
                int stepLength = 0;

                //遍历session所有的访问行为
                while (iterator.hasNext()){
                    Row row = iterator.next();
                    String searchKeyword = row.getString(5);
                    Object clickCategoryId = row.get(6);
                    if(userId == null){
                        userId = row.getLong(1);
                    }

                    if(StringUtils.isNotEmpty(searchKeyword)){
                        if(!keywordBuffer.toString().contains(searchKeyword)){
                            keywordBuffer.append(searchKeyword+",");
                        }
                    }

                    if(clickCategoryId != null){
                        if(!categoryIdsBuffer.toString().contains(String.valueOf(clickCategoryId))){
                            categoryIdsBuffer.append(clickCategoryId+",");
                        }
                    }

                    //计算session开始和结束时间
                    Date actionTime = DateUtils.parseTime(row.getString(4));
                    if(startTime == null){
                        startTime = actionTime;
                    }
                    if(endTime == null){
                        endTime = actionTime;
                    }
                    if(actionTime.before(startTime)){
                        startTime = actionTime;
                    }
                    if(actionTime.after(endTime)){
                        endTime =actionTime;
                    }

                    //计算session访问步长
                    stepLength++;

                }

                String searchKeywords = StringUtils.trimComma(keywordBuffer.toString());
                String clickCategoryIds = StringUtils.trimComma(categoryIdsBuffer.toString());

                //计算session访问时长（秒）
                long visitLength  = (endTime.getTime() - startTime.getTime())/1000;

                String partAggrInfo = Constants.FIELD_SESSION_ID + "=" + sessionid + "|"
                        +Constants.FIELD_SEARCH_KEYWORDS + "=" + searchKeywords + "|"
                        +Constants.FIELD_CLICK_CATEGORY_IDS + "=" +clickCategoryIds+"|"
                        +Constants.FIELD_STEP_LENGTH + "=" +stepLength+ "|"
                        +Constants.FIELD_VISIT_LENGTH+ "="+visitLength+ "|"
                        +Constants.FIELD_START_TIME+ "=" + DateUtils.formatTime(startTime);
                return new Tuple2<Long, String>(userId,partAggrInfo);

            }
        });

        //查询所有用户数据，并映射成(userid,row)
        String sql = "select * from user_info";
        final JavaRDD<Row> userInfoRDD = sqlContext.sql(sql).javaRDD();
        JavaPairRDD<Long, Row> userid2InfoRDD = userInfoRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception {
                return new Tuple2<Long, Row>(row.getLong(0), row);
            }
        });

        //将session粒度聚合数据,于用户信息进行join
        JavaPairRDD<Long, Tuple2<String, Row>> userid2FullInfoRDD
                = userid2PartAggrInfoRDD.join(userid2InfoRDD);

        //对join起来的数据进行拼接，彬返回<sessionid,fullaggrInfo>格式的数据
        JavaPairRDD<String, String> sessionid2FullAggrInfoRDD = userid2FullInfoRDD.mapToPair(new PairFunction<Tuple2<Long, Tuple2<String, Row>>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<Long, Tuple2<String, Row>> tuple) throws Exception {
                String partAggrInfo = tuple._2._1;
                Row userInfoRow = tuple._2._2;

                String sessionid = StringUtils.getFieldFromConcatString(partAggrInfo, "\\|", Constants.FIELD_SESSION_ID);
                int age = userInfoRow.getInt(3);
                String professional = userInfoRow.getString(4);
                String city = userInfoRow.getString(5);
                String sex = userInfoRow.getString(6);
                partAggrInfo = partAggrInfo + "|" +
                        Constants.FIELD_AGE + "=" + age + "|" +
                        Constants.FIELD_PROFESSIONAL + "=" + professional + "|" +
                        Constants.FIELD_CITY + "=" + city + "|" +
                        Constants.FIELD_SEX + "=" + sex;

                return new Tuple2<String, String>(sessionid, partAggrInfo);
            }
        });
        return sessionid2FullAggrInfoRDD;

    }



    /**
     * 过滤session数据
     * @param sessionid2FullAggrInfoRDD
     * @return
     */
    private static JavaPairRDD<String,String> filterSessionAndAggrStat(JavaPairRDD<String,String> sessionid2FullAggrInfoRDD
            , JSONObject taskParam, final Accumulator<String> accumulator){


        String startAge = ParamUtils.getParam(taskParam, Constants.PARAM_START_AGE);
        String endAge = ParamUtils.getParam(taskParam, Constants.PARAM_END_AGE);
        String professionals = ParamUtils.getParam(taskParam, Constants.PARAM_PAROFESSIONALS);
        String cities = ParamUtils.getParam(taskParam, Constants.PARAM_CITIES);
        String sex = ParamUtils.getParam(taskParam, Constants.PARAM_SEX);
        String keywords = ParamUtils.getParam(taskParam, Constants.PARAM_KEYWORDS);
        final String categoryIds = ParamUtils.getParam(taskParam, Constants.PARAM_CATEGORY_IDS);

        String parameter = (startAge != null? Constants.PARAM_START_AGE + "=" +startAge +"|":"")
                +(endAge != null? Constants.PARAM_END_AGE + "=" +endAge +"|":"")
                +(professionals != null? Constants.PARAM_PAROFESSIONALS + "=" +professionals +"|":"")
                +(cities != null? Constants.PARAM_CITIES + "=" +cities +"|":"")
                +(sex != null? Constants.PARAM_SEX + "=" +sex +"|":"")
                +(keywords != null? Constants.PARAM_KEYWORDS + "=" +keywords +"|":"")
                +(categoryIds != null? Constants.PARAM_CATEGORY_IDS + "=" +categoryIds :"");

        if(parameter.endsWith("|")){
            parameter = parameter.substring(0,parameter.length()-1);
        }
        final String _parameter = parameter;

        JavaPairRDD<String, String> filteredSessionid2AggrInfoRDD = sessionid2FullAggrInfoRDD.filter(new Function<Tuple2<String, String>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, String> tuple) throws Exception {
                String aggrInfo = tuple._2;

                //按照年龄范围进行过滤
                if(!ValidUtils.between(aggrInfo,Constants.FIELD_AGE,
                        _parameter,Constants.PARAM_START_AGE,Constants.PARAM_END_AGE)){
                    return false;
                }
                //按照职业范围进行过滤
                if(!ValidUtils.in(aggrInfo,Constants.FIELD_PROFESSIONAL,
                        _parameter,Constants.PARAM_PAROFESSIONALS)){
                    return false;
                }
                //按照城市范围进行过滤
                if(!ValidUtils.in(aggrInfo,Constants.FIELD_CITY,
                        _parameter,Constants.PARAM_CITIES)){
                    return false;
                }
                //按照性别进行过滤
                if(!ValidUtils.equal(aggrInfo,Constants.FIELD_SEX,
                        _parameter,Constants.PARAM_SEX)){
                    return false;
                }
                //按照搜索词进行过滤
                if(!ValidUtils.in(aggrInfo,Constants.FIELD_SEARCH_KEYWORDS,
                        _parameter,Constants.PARAM_KEYWORDS)){
                    return false;
                }

                //按照点击品类id进行过滤
                if(!ValidUtils.in(aggrInfo,Constants.FIELD_CLICK_CATEGORY_IDS,
                        _parameter,Constants.PARAM_CATEGORY_IDS)){
                    return false;
                }

                //对session的访问时长和访问步长，进行统计
                accumulator.add(Constants.SESSION_COUNT);
                //计算seesion的访问时长和访问步长的范围，并进行累加
                Long visitLength = Long.valueOf(StringUtils.getFieldFromConcatString(aggrInfo,
                        "\\|",Constants.FIELD_VISIT_LENGTH));
                Long stepLenght = Long.valueOf(StringUtils.getFieldFromConcatString(aggrInfo,
                        "\\|",Constants.FIELD_STEP_LENGTH));

                calculateStepLength(stepLenght);
                calculateVisitLength(visitLength);
                return true;
            }

            /**
             * 计算访问时长范围
             * @param visitLength
             */
            private void calculateVisitLength(long visitLength){
                if(visitLength >=1 && visitLength<=3){
                    accumulator.add(Constants.TIME_PERIOD_1s_3s);
                }else if(visitLength >=4 && visitLength<=6){
                    accumulator.add(Constants.TIME_PERIOD_4s_6s);
                }else if(visitLength >=7 && visitLength<=9){
                    accumulator.add(Constants.TIME_PERIOD_7s_9s);
                } else if(visitLength >=10 && visitLength<=30){
                    accumulator.add(Constants.TIME_PERIOD_10s_30s);
                }else if(visitLength >=31 && visitLength<=60){
                    accumulator.add(Constants.TIME_PERIOD_30s_60s);
                }else if(visitLength >60 && visitLength<=180){
                    accumulator.add(Constants.TIME_PERIOD_1m_3m);
                }else if(visitLength >180 && visitLength<=600){
                    accumulator.add(Constants.TIME_PERIOD_3m_10m);
                }else if(visitLength >600 && visitLength<=1800){
                    accumulator.add(Constants.TIME_PERIOD_10m_30m);
                }else if(visitLength >1800){
                    accumulator.add(Constants.TIME_PERIOD_30m);
                }
            }

            /**
             * 计算访问步长范围
             * @param stepLength
             */
            private void calculateStepLength(long stepLength){
                if(stepLength >=1 && stepLength<=3){
                    accumulator.add(Constants.STEP_PERIOD_1_3);
                }else if(stepLength >=4 && stepLength<=6){
                    accumulator.add(Constants.STEP_PERIOD_4_6);
                }else if(stepLength >=7 && stepLength<=9){
                    accumulator.add(Constants.STEP_PERIOD_7_9);
                }else if(stepLength >=10 && stepLength<30){
                    accumulator.add(Constants.STEP_PERIOD_10_30);
                }else if(stepLength >=30 && stepLength<60){
                    accumulator.add(Constants.STEP_PERIOD_30_60);
                }else if(stepLength >=60){
                    accumulator.add(Constants.STEP_PERIOD_60);
                }
            }
        });
        return filteredSessionid2AggrInfoRDD;
    }

    /**
     * 获取通过筛选条件的session的访问明细数据RDD
     * @param sessionid2aggrInfoRDD
     * @param sessionid2actionRDD
     * @return
     */
    private static JavaPairRDD<String,Row> getSessionid2detailRDD(
            JavaPairRDD<String,String> sessionid2aggrInfoRDD,
            JavaPairRDD<String,Row> sessionid2actionRDD){

        JavaPairRDD<String, Row> sessionid2detailRDD = sessionid2aggrInfoRDD.join(sessionid2actionRDD)
                .mapToPair(new PairFunction<Tuple2<String, Tuple2<String, Row>>, String, Row>() {
                    @Override
                    public Tuple2<String, Row> call(Tuple2<String, Tuple2<String, Row>> tuple2) throws Exception {
                        return new Tuple2<String, Row>(tuple2._1,tuple2._2._2);
                    }
                });

        return sessionid2detailRDD;
    }

    /**
     * 随机抽取session
     * @param sessionid2AggrInfoRdd
     */

    private static void randomExtractSession(
            JavaPairRDD<String,String> sessionid2AggrInfoRdd,
            final long taskId,JavaPairRDD<String,Row> sessiondi2ActionRDD,
            JavaSparkContext sc){

        //第一步，计算出每天没消失的session数量
        JavaPairRDD<String, String> time2sessionidRDD = sessionid2AggrInfoRdd.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> tuple2) throws Exception {
                String aggrInfo = tuple2._2;

                String startTime = StringUtils.getFieldFromConcatString(aggrInfo,
                        "\\|", Constants.FIELD_START_TIME);
                String dateHour = DateUtils.getDateHour(startTime);
                return new Tuple2<String, String>(dateHour, aggrInfo);
            }
        });

        //得到每天每小时的session数量
        Map<String, Long> countMap = time2sessionidRDD.countByKey();

        //第二步，使用按时间比例抽取算法，计算出每天每小时要抽取session的索引
        //将<yyyy-MM-dd_HH,count>格式的map，转换成<yyyy-MM-dd,<HH,count>>
        Map<String,Map<String,Long>> dayHourCountMap =
                new HashMap<String, Map<String, Long>>();
        for (Map.Entry<String,Long> countEntry:countMap.entrySet()){
            String dateHour = countEntry.getKey();
            String date = dateHour.split("_")[0];
            String hour = dateHour.split("_")[1];

            long count = countEntry.getValue();

            Map<String,Long> hourCountMap = dayHourCountMap.get(date);
            if(hourCountMap == null){
                hourCountMap = new HashMap<String, Long>();
                dayHourCountMap.put(date,hourCountMap);
            }

            hourCountMap.put(hour,count);

        }

        //开始实现按时间billing随机抽取算法

        //总共要抽取100个session，先按天数进行平分
        int extractNumberPerDay = 100 / dayHourCountMap.size();
        System.out.println(dayHourCountMap.size());

        //<date,<hour,(3,5,2,50)>>
        final Map<String,Map<String,List<Integer>>> dateHourExtractMap =
                new HashMap<String, Map<String, List<Integer>>>();

        Random random = new Random();

        for (Map.Entry<String,Map<String,Long>> dateHourCountEntry:
                dayHourCountMap.entrySet()){
            String date = dateHourCountEntry.getKey();
            Map<String, Long> hourCountMap = dateHourCountEntry.getValue();

            //计算出这一天的seesion总数
            long sessionCount = 0L;
            for (long hourCount : hourCountMap.values()){
                sessionCount += hourCount;
            }

            Map<String, List<Integer>> hourExtractMap = dateHourExtractMap.get(date);
            if(hourExtractMap == null){
                hourExtractMap = new HashMap<String, List<Integer>>();
                dateHourExtractMap.put(date,hourExtractMap);
            }

            //遍历每一个小时
            for (Map.Entry<String,Long> hourCountEntry : hourCountMap.entrySet()){
                String hour = hourCountEntry.getKey();
                long count = hourCountEntry.getValue();

                //计算每一个小时的session数量，占据当天总session数量的比例
                int hourExtractNumber = (int) (((double)count / (double)sessionCount)
                        *extractNumberPerDay);

                if(hourExtractNumber > count){
                    hourExtractNumber = (int)count;
                }

                //先获取当前小时的存放随机数的list
                List<Integer> extractIndeList = hourExtractMap.get(hour);
                if(extractIndeList == null){
                    extractIndeList = new ArrayList<Integer>();
                    hourExtractMap.put(hour,extractIndeList);
                }

                //生成上面计算出来的随机数
                for (int i = 0; i <hourExtractNumber ; i++) {
                    int extractIndex = random.nextInt((int) count);
                    while (extractIndeList.contains(extractIndex)){
                        extractIndex = random.nextInt((int) count);
                    }

                    extractIndeList.add(extractIndex);
                }
            }
        }
        /**
         *广播变量
         */
        final Broadcast<Map<String,Map<String,List<Integer>>>> dataHourExtractMapBroadcast =
                sc.broadcast(dateHourExtractMap);

        //第三步：遍历每天每小时的session，然后根据随机索引进行抽取
        JavaPairRDD<String, Iterable<String>> time2sessionsRDD = time2sessionidRDD.groupByKey();

        JavaPairRDD<String, String> extractSessionidsRDD = time2sessionsRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Iterable<String>>, String, String>() {
                    @Override
                    public Iterator<Tuple2<String, String>>
                    call(Tuple2<String, Iterable<String>> tuple) throws Exception {

                        List<Tuple2<String,String>> extractSessionIds = new ArrayList<Tuple2<String,String>>();

                        String dateHour = tuple._1;
                        String date = dateHour.split("_")[0];
                        String hour = dateHour.split("_")[1];
                        Iterator<String> iterator = tuple._2.iterator();

                        Map<String, Map<String, List<Integer>>> dateHourExtractMap =
                                dataHourExtractMapBroadcast.value();

                        List<Integer> extractIndexList = dateHourExtractMap.get(date).get(hour);

                        int index = 0;
                        ISessionRandomExtractDAO sessionRandomExtractDAO = DAOFactory.getSessionRandomExtractDAO();
                        while (iterator.hasNext()) {
                            String sessionAggrInfo = iterator.next();

                            if (extractIndexList.contains(index)) {
                                //将数据写入数据库

                                String sessionId = StringUtils.getFieldFromConcatString(sessionAggrInfo,
                                        "\\|", Constants.FIELD_SESSION_ID);

                                SessionRandomExtract sessionRandomExtract = new SessionRandomExtract();
                                sessionRandomExtract.setTaskid(taskId);
                                sessionRandomExtract.setSessionid(sessionId);
                                sessionRandomExtract.setStartTime(StringUtils.getFieldFromConcatString(sessionAggrInfo,
                                        "\\|", Constants.FIELD_START_TIME));
                                sessionRandomExtract.setClickCategoryIds(StringUtils.getFieldFromConcatString(sessionAggrInfo,
                                        "\\|", Constants.FIELD_CLICK_CATEGORY_IDS));
                                sessionRandomExtract.setSearchKeywords(StringUtils.getFieldFromConcatString(sessionAggrInfo,
                                        "\\|", Constants.FIELD_SEARCH_KEYWORDS));

                                sessionRandomExtractDAO.insert(sessionRandomExtract);

                                //将sessionid加入list
                                extractSessionIds.add(new Tuple2<String, String>(sessionId,sessionId));
                            }
                            index ++;
                        }

                        return extractSessionIds.iterator();
                    }
                });
        
        //第四步：获取抽取出来的session的明细数据

        JavaPairRDD<String, Tuple2<String, Row>> extractSessionDetailRDD =
                extractSessionidsRDD.join(sessiondi2ActionRDD);


//        extractSessionDetailRDD.foreach(new VoidFunction<Tuple2<String, Tuple2<String, Row>>>() {
//            @Override
//            public void call(Tuple2<String, Tuple2<String, Row>> tuple) throws Exception {
//
//                Row row = tuple._2._2;
//
//                SessionDetail  sessionDetail = new SessionDetail();
//                sessionDetail.setTaskid(taskId);
//                sessionDetail.setUserid(row.getLong(1));
//                sessionDetail.setSessionid(row.getString(2));
//                sessionDetail.setPageid(row.get(3)==null?null:row.getLong(3));
//                sessionDetail.setActionTime(row.getString(4));
//                sessionDetail.setSearchKeyword(row.getString(5));
//                sessionDetail.setClickCategoryId( row.get(6)==null?null:row.getLong(6));
//                sessionDetail.setClickProductId(row.get(7)==null?null:row.getLong(7));
//                sessionDetail.setOrderCategoryIds(row.getString(8));
//                sessionDetail.setOrderProductIds(row.getString(9));
//                sessionDetail.setPayCategoryIds(row.getString(10));
//                sessionDetail.setPayProductIds(row.getString(11));
//                ISessionDetailDAO sessionDetailDAO = DAOFactory.getSessionDetailDAO();
//                sessionDetailDAO.insert(sessionDetail);
//
//            }
//        });

        extractSessionDetailRDD.foreachPartition(
                new VoidFunction<Iterator<Tuple2<String, Tuple2<String, Row>>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Tuple2<String, Row>>> iterator) throws Exception {

                        List<SessionDetail> sessionDetails = new ArrayList<SessionDetail>();

                        while (iterator.hasNext()){
                            Tuple2<String, Tuple2<String, Row>> tuple = iterator.next();
                                    Row row = tuple._2._2;

                            SessionDetail  sessionDetail = new SessionDetail();
                            sessionDetail.setTaskid(taskId);
                            sessionDetail.setUserid(row.getLong(1));
                            sessionDetail.setSessionid(row.getString(2));
                            sessionDetail.setPageid(row.get(3)==null?null:row.getLong(3));
                            sessionDetail.setActionTime(row.getString(4));
                            sessionDetail.setSearchKeyword(row.getString(5));
                            sessionDetail.setClickCategoryId( row.get(6)==null?null:row.getLong(6));
                            sessionDetail.setClickProductId(row.get(7)==null?null:row.getLong(7));
                            sessionDetail.setOrderCategoryIds(row.getString(8));
                            sessionDetail.setOrderProductIds(row.getString(9));
                            sessionDetail.setPayCategoryIds(row.getString(10));
                            sessionDetail.setPayProductIds(row.getString(11));
                            ISessionDetailDAO sessionDetailDAO = DAOFactory.getSessionDetailDAO();
                            sessionDetailDAO.insert(sessionDetail);iterator.next();

                            sessionDetails.add(sessionDetail);
                        }

                        ISessionDetailDAO sessionDetailDAO = DAOFactory.getSessionDetailDAO();
                        sessionDetailDAO.insert(sessionDetails);

                    }
                }
        );

    }

    /**
     * 计算各session范围占比，并写入MySQL
     * @param value
     */
    private static void calculateAndPersistAggrStat(String value, long taskid) {
        // 从Accumulator统计串中获取值
        long session_count = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.SESSION_COUNT));

        long visit_length_1s_3s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_1s_3s));
        long visit_length_4s_6s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_4s_6s));
        long visit_length_7s_9s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_7s_9s));
        long visit_length_10s_30s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_10s_30s));
        long visit_length_30s_60s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_30s_60s));
        long visit_length_1m_3m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_1m_3m));
        long visit_length_3m_10m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_3m_10m));
        long visit_length_10m_30m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_10m_30m));
        long visit_length_30m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_30m));

        long step_length_1_3 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_1_3));
        long step_length_4_6 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_4_6));
        long step_length_7_9 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_7_9));
        long step_length_10_30 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_10_30));
        long step_length_30_60 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_30_60));
        long step_length_60 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_60));

        // 计算各个访问时长和访问步长的范围
        double visit_length_1s_3s_ratio = NumberUtils.formatDouble(
                (double) visit_length_1s_3s / (double)session_count, 2);
        double visit_length_4s_6s_ratio = NumberUtils.formatDouble(
                (double)visit_length_4s_6s / (double)session_count, 2);
        double visit_length_7s_9s_ratio = NumberUtils.formatDouble(
                (double)visit_length_7s_9s / (double)session_count, 2);
        double visit_length_10s_30s_ratio = NumberUtils.formatDouble(
                (double)visit_length_10s_30s / (double)session_count, 2);
        double visit_length_30s_60s_ratio = NumberUtils.formatDouble(
                (double)visit_length_30s_60s / (double)session_count, 2);
        double visit_length_1m_3m_ratio = NumberUtils.formatDouble(
                (double)visit_length_1m_3m / (double)session_count, 2);
        double visit_length_3m_10m_ratio = NumberUtils.formatDouble(
                (double)visit_length_3m_10m / (double)session_count, 2);
        double visit_length_10m_30m_ratio = NumberUtils.formatDouble(
                (double)visit_length_10m_30m / (double)session_count, 2);
        double visit_length_30m_ratio = NumberUtils.formatDouble(
                (double)visit_length_30m / (double)session_count, 2);

        double step_length_1_3_ratio = NumberUtils.formatDouble(
                (double)step_length_1_3 / (double)session_count, 2);
        double step_length_4_6_ratio = NumberUtils.formatDouble(
                (double)step_length_4_6 / (double)session_count, 2);
        double step_length_7_9_ratio = NumberUtils.formatDouble(
                (double)step_length_7_9 / (double)session_count, 2);
        double step_length_10_30_ratio = NumberUtils.formatDouble(
                (double)step_length_10_30 / (double)session_count, 2);
        double step_length_30_60_ratio = NumberUtils.formatDouble(
                (double)step_length_30_60 / (double)session_count, 2);
        double step_length_60_ratio = NumberUtils.formatDouble(
                (double)step_length_60 / (double)session_count, 2);

        // 将统计结果封装为Domain对象
        SessionAggrStat sessionAggrStat = new SessionAggrStat();
        sessionAggrStat.setTaskid(taskid);
        sessionAggrStat.setSession_count(session_count);
        sessionAggrStat.setVisit_length_1s_3s_ratio(visit_length_1s_3s_ratio);
        sessionAggrStat.setVisit_length_4s_6s_ratio(visit_length_4s_6s_ratio);
        sessionAggrStat.setVisit_length_7s_9s_ratio(visit_length_7s_9s_ratio);
        sessionAggrStat.setVisit_length_10s_30s_ratio(visit_length_10s_30s_ratio);
        sessionAggrStat.setVisit_length_30s_60s_ratio(visit_length_30s_60s_ratio);
        sessionAggrStat.setVisit_length_1m_3m_ratio(visit_length_1m_3m_ratio);
        sessionAggrStat.setVisit_length_3m_10m_ratio(visit_length_3m_10m_ratio);
        sessionAggrStat.setVisit_length_10m_30m_ratio(visit_length_10m_30m_ratio);
        sessionAggrStat.setVisit_length_30m_ratio(visit_length_30m_ratio);
        sessionAggrStat.setStep_length_1_3_ratio(step_length_1_3_ratio);
        sessionAggrStat.setStep_length_4_6_ratio(step_length_4_6_ratio);
        sessionAggrStat.setStep_length_7_9_ratio(step_length_7_9_ratio);
        sessionAggrStat.setStep_length_10_30_ratio(step_length_10_30_ratio);
        sessionAggrStat.setStep_length_30_60_ratio(step_length_30_60_ratio);
        sessionAggrStat.setStep_length_60_ratio(step_length_60_ratio);

        // 调用对应的DAO插入统计结果
        ISessionAggrStatDAO sessionAggrStatDAO = DAOFactory.getSessionAggrStatDAO();
        sessionAggrStatDAO.insert(sessionAggrStat);
    }

    /**
     * 获取top10热门品类
     * @param
     * @param sessionid2detailRDD
     */
    private static List<Tuple2<CatetorySortKey,String>> getTop10Category(
            JavaPairRDD<String,Row> sessionid2detailRDD ,final long taskId){
        /**
         * 第一步：获取符合条件的session访问的所有品类
         *
         */

            //获取session访问过的所有品类id
        //访问过：值得是 点击过、下单过或支付过的品类
        JavaPairRDD<Long, Long> categoryidRDD = sessionid2detailRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Row>, Long, Long>() {
                    @Override
                    public Iterator<Tuple2<Long, Long>> call(Tuple2<String, Row> tuple) throws Exception {

                        Row row = tuple._2;
                        List<Tuple2<Long,Long>> list = new ArrayList<Tuple2<Long, Long>>();

                        Long clickCategoryId = row.get(6)==null?null:row.getLong(6);
                        if(clickCategoryId != null){
                            list.add(new Tuple2<Long, Long>(clickCategoryId,clickCategoryId));
                        }

                        String orderCategoryIds = row.getString(8);
                        if(orderCategoryIds != null){
                            String[] orderCategoryIdsSplited = orderCategoryIds.split(",");
                            for (String categoryId : orderCategoryIdsSplited){
                                list.add(new Tuple2<Long, Long>(
                                        Long.valueOf(categoryId),Long.valueOf(categoryId)));
                            }
                        }

                        String payCategoryIds = row.getString(10);
                        if(payCategoryIds != null){
                            String[] payCategoryIdsSplited = payCategoryIds.split(",");
                            for (String payCategoryId : payCategoryIdsSplited){
                                list.add(new Tuple2<Long, Long>(
                                        Long.valueOf(payCategoryId),Long.valueOf(payCategoryId)));
                            }
                        }
                        return list.iterator();
                    }
                });

        /**
         * 必须要进行去重
         * 如果不去重的话，会出现重复的categoryid，排序会对重复的categoryid进行排序
         */
        categoryidRDD = categoryidRDD.distinct();


        /**
         * 第二步：计算各品类的点击、下单和支付次数
         */

        //计算各个品类的点击次数
        JavaPairRDD<Long, Long> clickCategoryId2CountRDD =
                getClickCategoryId2CountRDD(sessionid2detailRDD);
        //计算各个品类下单的次数
        JavaPairRDD<Long, Long> orderCategoryId2CountRDD =
                getOrderCategoryId2CountRDD(sessionid2detailRDD);
        //计算各个品类支付的次数
        JavaPairRDD<Long, Long> payCategoryId2CountRDD =
                getPayCategoryId2CountRDD(sessionid2detailRDD);

        /**
         * 第三步：join各品类与他的点击、下单和支付的次数
         */

        JavaPairRDD<Long, String> categoryid2countRDD =
                joinCategoryAndData(categoryidRDD, clickCategoryId2CountRDD, orderCategoryId2CountRDD, payCategoryId2CountRDD);


        /**
         * 第四步：自定义二次排序的key
         */

        /**
         * 第五步：将数据映射成<CategorySortKey,info>格式的RDD，然后进行二次排序
         */

        JavaPairRDD<CatetorySortKey, String> sortKey2countRDD =
                categoryid2countRDD.mapToPair(
                new PairFunction<Tuple2<Long, String>, CatetorySortKey, String>() {
                    @Override
                    public Tuple2<CatetorySortKey, String> call(Tuple2<Long, String> tuple) throws Exception {
                        String countInfo = tuple._2;
                        long clickCount = Long.valueOf(StringUtils.getFieldFromConcatString(countInfo, "\\|",
                                Constants.FIELD_CLICK_COUNT));
                        long orderCount = Long.valueOf(StringUtils.getFieldFromConcatString(countInfo, "\\|",
                                Constants.FIELD_ORDER_COUNT));
                        long payCount = Long.valueOf(StringUtils.getFieldFromConcatString(countInfo, "\\|",
                                Constants.FIELD_PAY_COUNT));

                        CatetorySortKey sortKey = new CatetorySortKey(clickCount, orderCount, payCount);
//                        SortByKey sortKey = new SortByKey(clickCount, orderCount, payCount);
                        return new Tuple2<CatetorySortKey, String>(sortKey, countInfo);
                    }
                });

        JavaPairRDD<CatetorySortKey, String> sortedCategoryCountRDD =
                sortKey2countRDD.sortByKey(false);

        /**
         * 第六步：用take取出top10热门品类，并写入MySQL
         */
        List<Tuple2<CatetorySortKey, String>> top10Catetory =
                sortedCategoryCountRDD.take(10);

        ITop10CategoryDAO top10CategoryDAO = DAOFactory.getTop10CategoryDAO();

        for (Tuple2<CatetorySortKey,String> tuple:top10Catetory){

            String countInfo = tuple._2;
            long categoryid = Long.valueOf(StringUtils.getFieldFromConcatString(
                    countInfo, "\\|", Constants.FIELD_CATEGORY_ID));
            long clickCount = Long.valueOf(StringUtils.getFieldFromConcatString(
                    countInfo, "\\|", Constants.FIELD_CLICK_COUNT));
            long orderCount = Long.valueOf(StringUtils.getFieldFromConcatString(
                    countInfo, "\\|", Constants.FIELD_ORDER_COUNT));
            long payCount = Long.valueOf(StringUtils.getFieldFromConcatString(
                    countInfo, "\\|", Constants.FIELD_PAY_COUNT));

            Top10Category category = new Top10Category();
            category.setTaskid(taskId);
            category.setCategoryid(categoryid);
            category.setClickCount(clickCount);
            category.setOrderCount(orderCount);
            category.setPayCount(payCount);

            top10CategoryDAO.insert(category);

        }

        return top10Catetory;

    }

    /**
     * 获取各品类点击RDD
     * @param sessionid2detailRDD
     * @return
     */
    private static JavaPairRDD<Long,Long> getClickCategoryId2CountRDD(
            JavaPairRDD<String,Row> sessionid2detailRDD){


        JavaPairRDD<String, Row> clickActionRDD = sessionid2detailRDD.filter(
                new Function<Tuple2<String, Row>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<String, Row> tuple) throws Exception {
                        Row row = tuple._2;
                        return row.get(6) != null?true:false;
                    }
                });

        JavaPairRDD<Long, Long> clickCategoryIdRDD = clickActionRDD.mapToPair(
                new PairFunction<Tuple2<String, Row>, Long, Long>() {
                    @Override
                    public Tuple2<Long, Long> call(Tuple2<String, Row> tuple) throws Exception {
                        Row row = tuple._2;
                        return new Tuple2<Long, Long>(row.getLong(6),1L);
                    }
                });

        JavaPairRDD<Long, Long> clickCategoryId2CountRDD = clickCategoryIdRDD.reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {

                return v1+v2;
            }
        });

        return clickCategoryId2CountRDD;
    }


    /**
     * 获取各品类的下单次数RDD
     * @param sessionid2detailRDD
     * @return
     */
    private static JavaPairRDD<Long,Long> getOrderCategoryId2CountRDD(
            JavaPairRDD<String,Row> sessionid2detailRDD){


        JavaPairRDD<String, Row> orderActionRDD = sessionid2detailRDD.filter(
                new Function<Tuple2<String, Row>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<String, Row> tuple) throws Exception {
                        Row row = tuple._2;
                        return row.getString(8)!=null?true:false;
                    }
                });

        JavaPairRDD<Long, Long> orderCategoryIdRDD = orderActionRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Row>, Long, Long>() {
                    @Override
                    public Iterator<Tuple2<Long, Long>> call(Tuple2<String, Row> tuple) throws Exception {

                        Row row = tuple._2;
                        String orderCategoryIds = row.getString(8);
                        String[] orderCategoryIdsSplited = orderCategoryIds.split(",");
                        List<Tuple2<Long,Long>> list = new ArrayList<Tuple2<Long, Long>>();
                        for (String orderCategoryId : orderCategoryIdsSplited){
                            list.add(new Tuple2<Long, Long>(Long.valueOf(orderCategoryId),1L));
                        }
                        return list.iterator();
                    }
                });

        JavaPairRDD<Long, Long> orderCategoryId2CountRDD = orderCategoryIdRDD.reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {

                return v1+v2;
            }
        });

        return orderCategoryId2CountRDD;
    }

    /**
     * 获取各个品类支付的RDD
     * @param sessionid2detailRDD
     * @return
     */
    private static JavaPairRDD<Long,Long> getPayCategoryId2CountRDD(
            JavaPairRDD<String,Row> sessionid2detailRDD){


        JavaPairRDD<String, Row> payActionRDD = sessionid2detailRDD.filter(
                new Function<Tuple2<String, Row>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<String, Row> tuple) throws Exception {
                        Row row = tuple._2;
                        return row.getString(10)!=null?true:false;
                    }
                });

        JavaPairRDD<Long, Long> payCategoryIdRDD = payActionRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Row>, Long, Long>() {
                    @Override
                    public Iterator<Tuple2<Long, Long>> call(Tuple2<String, Row> tuple) throws Exception {

                        Row row = tuple._2;
                        String payCategoryIds = row.getString(10);
                        String[] payCategoryIdsSplited = payCategoryIds.split(",");
                        List<Tuple2<Long,Long>> list = new ArrayList<Tuple2<Long, Long>>();
                        for (String payCategoryId : payCategoryIdsSplited){
                            list.add(new Tuple2<Long, Long>(Long.valueOf(payCategoryId),1L));
                        }
                        return list.iterator();
                    }
                });

        JavaPairRDD<Long, Long> payCategoryId2CountRDD = payCategoryIdRDD.reduceByKey(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {

                return v1+v2;
            }
        });

        return payCategoryId2CountRDD;
    }

    /**
     * 连接品类RDD与数据RDD
     * @param categoryidRDD
     * @param clickCategoryId2CountRDD
     * @param orderCategoryId2CountRDD
     * @param payCategoryId2CountRDD
     * @return
     */
    private static JavaPairRDD<Long,String> joinCategoryAndData(
            JavaPairRDD<Long,Long> categoryidRDD,
            JavaPairRDD<Long,Long> clickCategoryId2CountRDD,
            JavaPairRDD<Long,Long> orderCategoryId2CountRDD,
            JavaPairRDD<Long,Long> payCategoryId2CountRDD){

        JavaPairRDD<Long, Tuple2<Long, Optional<Long>>> temJoinRDD =
                categoryidRDD.leftOuterJoin(clickCategoryId2CountRDD);

        JavaPairRDD<Long, String> tempMapRDD = temJoinRDD.mapToPair(
                new PairFunction<Tuple2<Long, Tuple2<Long, Optional<Long>>>, Long, String>() {
                    @Override
                    public Tuple2<Long, String> call(Tuple2<Long, Tuple2<Long, Optional<Long>>> tuple) throws Exception {
                        Optional<Long> longOptional = tuple._2._2;
                        Long categoryid = tuple._1;
                        long clickCount = 0L;

                        if(longOptional.isPresent()){
                            clickCount = longOptional.get();
                        }

                        String value = Constants.FIELD_CATEGORY_ID + "="+categoryid +"|"
                                +Constants.FIELD_CLICK_COUNT +"="+clickCount;
                        return new Tuple2<Long, String>(categoryid,value);
                    }
                });

        tempMapRDD = tempMapRDD.leftOuterJoin(orderCategoryId2CountRDD).mapToPair(
                new PairFunction<Tuple2<Long, Tuple2<String, Optional<Long>>>, Long, String>() {
            @Override
            public Tuple2<Long, String> call(Tuple2<Long, Tuple2<String, Optional<Long>>> tuple) throws Exception {
                Long categoryid = tuple._1;
                Optional<Long> longOptional = tuple._2._2;
                String value = tuple._2._1;
                long orderCount = 0L;

                if(longOptional.isPresent()){
                    orderCount = longOptional.get();
                }

                value = value + "|" +Constants.FIELD_ORDER_COUNT +"="+orderCount;

                return new Tuple2<Long, String>(categoryid,value);
            }
        });


        tempMapRDD = tempMapRDD.leftOuterJoin(payCategoryId2CountRDD).mapToPair(
                new PairFunction<Tuple2<Long, Tuple2<String, Optional<Long>>>, Long, String>() {
                    @Override
                    public Tuple2<Long, String> call(Tuple2<Long, Tuple2<String, Optional<Long>>> tuple) throws Exception {
                        Long categoryid = tuple._1;
                        Optional<Long> longOptional = tuple._2._2;
                        String value = tuple._2._1;
                        long orderCount = 0L;

                        if(longOptional.isPresent()){
                            orderCount = longOptional.get();
                        }

                        value = value  +"|" +Constants.FIELD_PAY_COUNT +"="+orderCount;

                        return new Tuple2<Long, String>(categoryid,value);
                    }
                });

        return tempMapRDD;

    }

    /**
     * 获取top10活跃session
     * @param taskId
     * @param sessionid2detailRDD
     * @param top10Category
     * @param sc
     */
    private static void getTop10Session(
            final Long taskId, JavaPairRDD<String, Row> sessionid2detailRDD,
            final List<Tuple2<CatetorySortKey, String>> top10Category, JavaSparkContext sc) {

        /**
         * 第一步：将top10热门品类的id，生成一份RDD
         */
        List<Tuple2<Long,Long>> top10CategoryIdList =
                new ArrayList<Tuple2<Long, Long>>();
        for (Tuple2<CatetorySortKey,String> category : top10Category){
            Long categoryId = Long.valueOf(StringUtils.getFieldFromConcatString(category._2, "\\|",
                    Constants.FIELD_CATEGORY_ID));
            top10CategoryIdList.add(new Tuple2<Long, Long>(categoryId,categoryId));
            
        }
        JavaPairRDD<Long, Long> top10CategoryRDD = sc.parallelizePairs(top10CategoryIdList);


        /**
         * 第二步：计算top10品类被各session点击的次数
         */
        JavaPairRDD<String, Iterable<Row>> sessionid2detailsRDD =
        sessionid2detailRDD.groupByKey();

        JavaPairRDD<Long, String> categorydi2sessionCountRDD = sessionid2detailsRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, Long, String>() {
            @Override
            public Iterator<Tuple2<Long, String>> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                String sessionid = tuple._1;
                Iterator<Row> iterator = tuple._2.iterator();

                Map<Long,Long> categoryCountMap = new HashMap<Long, Long>();
                while (iterator.hasNext()){
                    Row row = iterator.next();
                    if(row.get(6) != null){
                        long categoryid = row.getLong(6);
                        Long count = categoryCountMap.get(categoryid);
                        if(count == null){
                            count = 0L;
                        }
                        count++;
                        categoryCountMap.put(categoryid,count);
                    }
                }

                //返回结果<categoryid,sessionid,count> 格式
                List<Tuple2<Long,String>> list = new ArrayList<Tuple2<Long, String>>();

                for (Map.Entry<Long,Long> categoryCountEntry : categoryCountMap.entrySet()){
                    Long categoryid = categoryCountEntry.getKey();
                    Long count = categoryCountEntry.getValue();
                    String value = sessionid + "," + count;
                    list.add(new Tuple2<Long, String>(categoryid,value));

                }
                return list.iterator();
            }
        });
        //获取到top10热门品类，被各个session点击的次数
        JavaPairRDD<Long, String> top10CategorySessionCountRDD =
                top10CategoryRDD.join(categorydi2sessionCountRDD)
                .mapToPair(new PairFunction<Tuple2<Long, Tuple2<Long, String>>, Long,String>() {
                    @Override
                    public Tuple2<Long, String>
                    call(Tuple2<Long, Tuple2<Long, String>> tuple) throws Exception {

                        return new Tuple2<Long, String>(tuple._1,tuple._2._2);
                    }
                });


        /**
         * 第三步：分组取topN算法实现，获取每个品类的top10活跃用户
         */

        JavaPairRDD<Long, Iterable<String>> top10CategorySessionCountsRDD =
                top10CategorySessionCountRDD.groupByKey();

        JavaPairRDD<String, String> top10SessionRDD = top10CategorySessionCountsRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<Long, Iterable<String>>, String, String>() {
                    @Override
                    public Iterator<Tuple2<String, String>>
                    call(Tuple2<Long, Iterable<String>> tuple) throws Exception {
                        Long categoryid = tuple._1;
                        Iterator<String> iterator = tuple._2.iterator();

                        String[] top10Sessions = new String[10];
                        while (iterator.hasNext()) {
                            String sessionCount = iterator.next();
                            Long count = Long.valueOf(sessionCount.split(",")[1]);

                            //遍历排序数组
                            for (int i = 0; i < top10Sessions.length; i++) {
                                //如果当前i位，没有数据，那么直接将i位数据赋值为当前sessionCount
                                if (top10Sessions[i] == null) {
                                    top10Sessions[i] = sessionCount;
                                    break;
                                } else {
                                    long _count = Long.valueOf(top10Sessions[i].split(",")[1]);

                                    if (count > _count) {
                                        //从排序数组最后一位开始，到i位，所有数据往后挪一位
                                        for (int j = 9; j > i; j--) {
                                            top10Sessions[j] = top10Sessions[j - 1];
                                        }
                                        //将i位赋值为sessionCount
                                        top10Sessions[i] = sessionCount;
                                        break;
                                    }
                                }
                            }
                        }
                        //将数据写入Mysql表中
                        List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
                        for (String sessionCount : top10Sessions) {
                            if(sessionCount != null){
                                String sessionid = sessionCount.split(",")[0];
                                Long count = Long.valueOf(sessionCount.split(",")[1]);

                                Top10Session top10Session = new Top10Session();
                                top10Session.setTaskid(taskId);
                                top10Session.setCategoryid(categoryid);
                                top10Session.setClickCount(count);
                                top10Session.setSessionid(sessionid);

                                ITop10SessionDAO top10SessionDAO = DAOFactory.getTop10SessionDAO();
                                top10SessionDAO.insert(top10Session);


                                list.add(new Tuple2<String, String>(sessionid, sessionid));
                            }


                        }

                        return list.iterator();
                    }
                });

        /**
         * 第四步：获取top10活跃session的明细数据，写入mysql
         */

        JavaPairRDD<String, Tuple2<String, Row>> sessionDetailRDD =
                top10SessionRDD.join(sessionid2detailRDD);


        sessionDetailRDD.foreach(new VoidFunction<Tuple2<String, Tuple2<String, Row>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<String, Row>> tuple) throws Exception {

                Row row = tuple._2._2;

                SessionDetail  sessionDetail = new SessionDetail();
                sessionDetail.setTaskid(taskId);
                sessionDetail.setUserid(row.getLong(1));
                sessionDetail.setSessionid(row.getString(2));
                sessionDetail.setPageid(row.get(3)==null?null:row.getLong(3));
                sessionDetail.setActionTime(row.getString(4));
                sessionDetail.setSearchKeyword(row.getString(5));
                sessionDetail.setClickCategoryId( row.get(6)==null?null:row.getLong(6));
                sessionDetail.setClickProductId(row.get(7)==null?null:row.getLong(7));
                sessionDetail.setOrderCategoryIds(row.getString(8));
                sessionDetail.setOrderProductIds(row.getString(9));
                sessionDetail.setPayCategoryIds(row.getString(10));
                sessionDetail.setPayProductIds(row.getString(11));
                ISessionDetailDAO sessionDetailDAO = DAOFactory.getSessionDetailDAO();
                sessionDetailDAO.insert(sessionDetail);

            }
        });
    }


}
