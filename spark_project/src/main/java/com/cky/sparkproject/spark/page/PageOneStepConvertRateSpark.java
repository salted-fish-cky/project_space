package com.cky.sparkproject.spark.page;

import com.alibaba.fastjson.JSONObject;
import com.cky.sparkproject.constants.Constants;
import com.cky.sparkproject.dao.DAOFactory;
import com.cky.sparkproject.dao.IPageSplitConvertRateDAO;
import com.cky.sparkproject.dao.ITaskDAO;
import com.cky.sparkproject.domain.PageSplitConvertRate;
import com.cky.sparkproject.domain.Task;
import com.cky.sparkproject.utils.DateUtils;
import com.cky.sparkproject.utils.NumberUtils;
import com.cky.sparkproject.utils.ParamUtils;
import com.cky.sparkproject.utils.SparkUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

/**
 * 页面单跳转化率模块的spark作业
 */
public class PageOneStepConvertRateSpark {

    public static void main(String[] args){

        //1.构造spark上下文
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_PAGE);
        SparkUtils.setMaster(conf);

        JavaSparkContext sc = new JavaSparkContext(conf);

        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());

        //2.生成模拟数据
        SparkUtils.mockData(sc,sqlContext);
        //3.查询任务，获取任务的参数
        Long taskId = ParamUtils.getTaskIdFromArgs(args,Constants.SPARK_LOCAL_TASKID_PAGE);

        ITaskDAO taskDAO = DAOFactory.getTaskDAO();
        Task task = taskDAO.findById(taskId);
        if(task == null){
            System.out.println(new Date()+":找不到taskId对应的task");
            return;
        }
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());

        //4.查询指定日期范围的用户访问行为数据

        JavaRDD<Row> actionRDD = SparkUtils.getActionRDDByDateRange(sqlContext, taskParam);

        //对用户访问行为数据做一个映射，将其映射为<sessionid，访问行为>的格式
        JavaPairRDD<String, Row> sessionid2ActionRDD = getSessionid2ActionRDD(actionRDD);
        sessionid2ActionRDD = sessionid2ActionRDD.cache();

        //对<sessionid，访问行为>RDD，做一次groupByKey操作
        JavaPairRDD<String, Iterable<Row>> sessionid2actionsRDD = sessionid2ActionRDD.groupByKey();

        //最核心的一步，每个session的单跳页面切片的生成，以及页面流的匹配，算法
        JavaPairRDD<String, Integer> pageSplitRDD =
                generateAndMatchPageSplit(sessionid2actionsRDD, sc, taskParam);
        System.out.println(pageSplitRDD.count());

        Map<String, Long> pageSplitPVMap = pageSplitRDD.countByKey();

        //使用者指定的页面流是：3,2，5,8,6
        //现在拿到的这个pageSplitPVMap ：3->2,2->5,5->8,8->6 ,只要算出3的总量
        long startPagePv = getStartPagePv(taskParam, sessionid2actionsRDD);
        
        //计算目标页面流的各个页面切片的转化率
        Map<String, Double> convertMap =
                computeSplitCounvertRate(pageSplitPVMap, startPagePv, taskParam);

        //持久化转化率
        persistConvertRate(taskId,convertMap);

    }

    /**
     * 获取<sessionid，用户访问行为>格式的数据
     * @param actionRDD
     */
    private static JavaPairRDD<String,Row> getSessionid2ActionRDD(JavaRDD<Row> actionRDD) {

        return actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2),row);
            }
        });
    }

    private static JavaPairRDD<String,Integer> generateAndMatchPageSplit(
            JavaPairRDD<String,Iterable<Row>> sessionid2actionsRDD,
            JavaSparkContext sc,
            JSONObject taskParam
    ){
        String tagertPageFlow = ParamUtils.getParam(taskParam, Constants.PARAM_TARGET_PAGE_FLOW);

        final Broadcast<String> targetPageFlowBroadcast = sc.broadcast(tagertPageFlow);
        return sessionid2actionsRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Iterable<Row>>, String, Integer>() {
                    @Override
                    public Iterator<Tuple2<String, Integer>> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                        List<Tuple2<String,Integer>> list = new ArrayList<Tuple2<String, Integer>>();
                        //获取到当前session的访问的迭代器
                        Iterator<Row> iterator = tuple._2.iterator();
                        //获取使用者指定的页面流
                        String[] targetPages = targetPageFlowBroadcast.value().split(",");
                        //对session的访问行为数据按照时间进行排序

                        List<Row> rows = new ArrayList<Row>();
                        while (iterator.hasNext()){
                            Row row = iterator.next();
                            rows.add(row);
                        }
                        //对时间进行排序
                        Collections.sort(rows, new Comparator<Row>() {
                            @Override
                            public int compare(Row o1, Row o2) {
                                String actionTime1 = o1.getString(4);
                                String actionTime2 = o2.getString(4);

                                Date date1 = DateUtils.parseTime(actionTime1);
                                Date date2 = DateUtils.parseTime(actionTime2);

                                return (int) (date1.getTime()-date2.getTime());
                            }
                        });

                        //页面切片的生成，以及页面流的匹配
                        Long lastPageId = null;
                        for (Row row : rows ){
                            long pageId = row.getLong(3);

                            if(lastPageId == null){
                                lastPageId = pageId;
                                continue;
                            }
                            //生成一个页面切片
                            String pageSplit = lastPageId + "_" + pageId;

                            //对这个切片判断一下，是否在用户指定的页面流中
                            for (int i = 1; i < targetPages.length; i++) {
                                //用户指定的页面流
                                String targetPageSplit = targetPages[i-1]+ "_"+targetPages[i];
                                if(targetPageSplit.equals(pageSplit)){
                                    list.add(new Tuple2<String, Integer>(pageSplit,1));
                                    break;
                                }
                            }

                            lastPageId = pageId;
                        }
                        return list.iterator();
                    }
                }
        );
    }

    private static long getStartPagePv(JSONObject taskParam,
                                       JavaPairRDD<String,Iterable<Row>> sessionid2actionsRDD){

        String targetPageFlow = ParamUtils.getParam(taskParam, Constants.PARAM_TARGET_PAGE_FLOW);
        final long startPageId = Long.valueOf(targetPageFlow.split(",")[0]);
        JavaRDD<Long> startPageRDD = sessionid2actionsRDD.flatMap(new FlatMapFunction<Tuple2<String, Iterable<Row>>, Long>() {
            @Override
            public Iterator<Long> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                List<Long> list = new ArrayList<Long>();
                Iterator<Row> iterator = tuple._2.iterator();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    long pageId = row.getLong(3);
                    if (pageId == startPageId) {
                        list.add(pageId);
                    }
                }
                return list.iterator();
            }
        });

        return startPageRDD.count();
    }

    /**
     * 计算页面切片转换率
     * @param pageSplitPVMap
     * @param startPagePv
     * @param taskParam
     * @return
     */
    private static Map<String,Double> computeSplitCounvertRate(
            Map<String,Long> pageSplitPVMap,
            long startPagePv,
            JSONObject taskParam
    ){
        String[] targetPages = ParamUtils.getParam(taskParam,
                Constants.PARAM_TARGET_PAGE_FLOW).split(",");

        Map<String,Double> convertRateMap = new HashMap<String, Double>();

        long lastPageSplitPv = 0L;

        for (int i = 1; i < targetPages.length; i++) {
            String targetPageSplit = targetPages[i-1] +"_" +targetPages[i];
            Long targetPageSplitPv = pageSplitPVMap.get(targetPageSplit);
            double couvertRate = 0.0;
            if(i == 1){
                couvertRate = NumberUtils.formatDouble(
                        (double) targetPageSplitPv / (double) startPagePv, 2);
            }else {
                couvertRate = NumberUtils.formatDouble(
                        (double) targetPageSplitPv / (double) lastPageSplitPv, 2);
                
            }
            convertRateMap.put(targetPageSplit,couvertRate);
            lastPageSplitPv = targetPageSplitPv;
        }
        return convertRateMap;
    }

    /**
     * 持久化转化率
     * @param taskid
     * @param convertRateMap
     */
    private static void persistConvertRate(long taskid,
                                           Map<String,Double> convertRateMap){
                StringBuffer sb = new StringBuffer("");

                for (Map.Entry<String,Double> convertRateEntry : convertRateMap.entrySet()){
                    String pageSplit = convertRateEntry.getKey();
                    Double converRate = convertRateEntry.getValue();

                    sb.append(pageSplit + "=" + converRate + "|");
                }
        String converRate = sb.toString();
        converRate = converRate.substring(0,converRate.length()-1);
        PageSplitConvertRate pageSplitConvertRate = new PageSplitConvertRate();
        pageSplitConvertRate.setConvertRate(converRate);
        pageSplitConvertRate.setTaskid(taskid);
        IPageSplitConvertRateDAO pageSplitConvertRateDAO = DAOFactory.getPageSplitConvertRateDAO();
        pageSplitConvertRateDAO.insert(pageSplitConvertRate);

    }
}
