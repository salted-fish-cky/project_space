package com.cky.sparkproject.test;

import com.alibaba.fastjson.JSONObject;
import com.cky.sparkproject.conf.ConfigurationManager;
import com.cky.sparkproject.constants.Constants;
import com.cky.sparkproject.dao.DAOFactory;
import com.cky.sparkproject.dao.IAreaTop3ProductDAO;
import com.cky.sparkproject.dao.ITaskDAO;
import com.cky.sparkproject.domain.AreaTop3Product;
import com.cky.sparkproject.domain.Task;
import com.cky.sparkproject.utils.ParamUtils;
import com.cky.sparkproject.utils.SparkUtils;
import com.cky.sparkproject.utils.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.util.*;

public class Test {

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
        Long taskId = ParamUtils.getTaskIdFromArgs(args,Constants.SPARK_LOCAL_TASKID_PRODUCT);

        ITaskDAO taskDAO = DAOFactory.getTaskDAO();
        Task task = taskDAO.findById(taskId);
        if(task == null){
            System.out.println(new Date()+":找不到taskId对应的task");
            return;
        }
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());

        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);

        //查询用户指定日期范围内的点击行为数据
        JavaPairRDD<Long, Row> cityId2ClickActionRDD = getCityId2ClickActionRDDByDate
                (startDate, endDate, sqlContext);

        //从mysql中查询城市信息
        JavaPairRDD<Long, Row> cityId2CityInfoRDD = getcityId2CityInfoRDD(sqlContext);

        JavaPairRDD<String, String> productAndCityInfoRDD = joinClickActionRDDAndCityInfoRDD(cityId2ClickActionRDD, cityId2CityInfoRDD);

        JavaPairRDD<Long, Row> productInfoRDD = getProductInfoRDD(sqlContext);


        JavaPairRDD<String, String> areaAndProductFullRDD =
                getAreaProductClickCountRDD(productAndCityInfoRDD, sc, productInfoRDD);

        persistAreaTop3Product(areaAndProductFullRDD,taskId);

        sc.close();
    }


    /**
     * 根据时间
     * @param startDate
     * @param endDate
     * @param sqlContext
     * @return
     */
    private static JavaPairRDD<Long, Row> getCityId2ClickActionRDDByDate(
            String startDate,String endDate,SQLContext sqlContext
    ){
        String sql = "select " +
                "city_id," +
                "click_product_id product_id " +
                "from user_visit_action " +
                "where click_product_id IS NOT NULL " +
                "and click_product_id!='null' " +
                "and click_product_id!='NULL' " +
                "and action_time>='"+startDate+"' " +
                "and action_time<='"+endDate+"'";

        sql = "select city_id," +
                "click_product_id product_id " +
                "from user_visit_action " +
                " where click_product_id IS NOT NULL and t_date>='"+startDate+"'" +
                " and t_date<='"+endDate+"'";
        Dataset<Row> rows = sqlContext.sql(sql);

        JavaRDD<Row> clickActionRDD = rows.javaRDD();

        JavaPairRDD<Long, Row> cityid2ClickActionRDD = clickActionRDD.mapToPair(
                new PairFunction<Row, Long, Row>() {
                    @Override
                    public Tuple2<Long, Row> call(Row row) throws Exception {

                        return new Tuple2<Long, Row>(row.getLong(0), row);
                    }
                });

        return cityid2ClickActionRDD;
    }


    /**
     * 使用Spark SQL从 msyql中查出城市信息
     * @param sqlContext
     * @return
     */
    private static JavaPairRDD<Long,Row> getcityId2CityInfoRDD(SQLContext sqlContext){
        //构建mysql连接配置信息
        Map<String,String> options = new HashMap<String, String>();
        String url = null;
        String user = null;
        String password = null;
        Boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if(local){
            url = ConfigurationManager.getProperty(Constants.JDBC_URL);
            user = ConfigurationManager.getProperty(Constants.JDBC_USER);
            password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
        }else {
            url = ConfigurationManager.getProperty(Constants.JDBC_URL_PROD);
            user = ConfigurationManager.getProperty(Constants.JDBC_USER_PROD);
            password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD_PROD);
        }

        options.put("url",url);
        options.put("dbtable","city_info");
        options.put("user",user);
        options.put("password",password);

        //通过SQLContext取从MySQL中查询数据
        Dataset<Row> rows = sqlContext.read().format("jdbc").options(options).load();

        JavaRDD<Row> cityInfoRDD = rows.javaRDD();

        JavaPairRDD<Long, Row> cityId2CityInfoRDD = cityInfoRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception {
                return new Tuple2<Long, Row>(row.getLong(0), row);
            }
        });

        return cityId2CityInfoRDD;
    }


    /**
     * 返回RDD ((area,productId),productAndCityInfo)
     * @param cityId2ClickActionRDD
     * @param cityId2CityInfoRDD
     * @return
     */
    private static JavaPairRDD<String, String> joinClickActionRDDAndCityInfoRDD(
            JavaPairRDD<Long, Row> cityId2ClickActionRDD,
            JavaPairRDD<Long, Row> cityId2CityInfoRDD) {

        JavaPairRDD<Long, Tuple2<Row, Row>> joined =
                cityId2ClickActionRDD.join(cityId2CityInfoRDD);

        JavaPairRDD<String, String> productAndCityInfoRDD = joined.mapToPair(
                new PairFunction<Tuple2<Long, Tuple2<Row, Row>>, String, String>() {
                    @Override
                    public Tuple2<String, String> call(Tuple2<Long, Tuple2<Row, Row>> tuple) throws Exception {
                        Long cityId = tuple._1;
                        Row clickAction = tuple._2._1;
                        Row cityInfo = tuple._2._2;

                        Long productId = clickAction.getLong(1);
                        String cityName = cityInfo.getString(1);
                        String area = cityInfo.getString(2);

                        String productAndCityInfo = Constants.FIELD_PRODUCTID + "=" + productId + "|" +
                                Constants.FIELD_CITYINFO + "=" + cityId + ":" + cityName+"|"+
                                Constants.FIELD_AREA + "=" + area;

                        return new Tuple2<String, String>(area+","+productId, productAndCityInfo);
                    }
                });

        return productAndCityInfoRDD;

    }

    /**
     * 从表中把product的详细信息读出来
     * @param sqlContext
     * @return
     */
    private static JavaPairRDD<Long, Row> getProductInfoRDD(SQLContext sqlContext) {

        String sql = "select * from product_info";

        Dataset<Row> rows = sqlContext.sql(sql);

        JavaRDD<Row> rowJavaRDD = rows.javaRDD();

        JavaPairRDD<Long, Row> productInfoRDD = rowJavaRDD.mapToPair(
                new PairFunction<Row, Long, Row>() {
                    @Override
                    public Tuple2<Long, Row> call(Row row) throws Exception {
                        return new Tuple2<Long, Row>(row.getLong(0), row);
                    }
                });

        return productInfoRDD;
    }

    /**
     * 得到每个区域每种商品的点击量，并和product详细表join
     * 返回  RDD(area,productAndCityInfo)
     * @param productAndCityInfoRDD
     * @param sc
     * @param productInfoRDD
     * @return
     */
    private static JavaPairRDD<String, String> getAreaProductClickCountRDD(
            JavaPairRDD<String, String> productAndCityInfoRDD,
            JavaSparkContext sc,JavaPairRDD<Long, Row> productInfoRDD) {

        final Map<String, Long> areaProductClickCount = productAndCityInfoRDD.countByKey();
        //创建广播
        final Broadcast<Map<String, Long>> clickCountBroadcast =
                sc.broadcast(areaProductClickCount);

        JavaPairRDD<String, Iterable<String>> groupedRDD = productAndCityInfoRDD.groupByKey();

        JavaPairRDD<Long, String> areaAndProductClickCountRDD = groupedRDD.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Iterable<String>>, Long, String>() {
                    @Override
                    public Iterator<Tuple2<Long, String>>
                    call(Tuple2<String, Iterable<String>> tuple) throws Exception {
                        List<Tuple2<Long, String>> list = new ArrayList<Tuple2<Long, String>>();
                        String areaAndProductId = tuple._1;
                        Iterator<String> iterator = tuple._2.iterator();
                        Map<String, Long> areaProductClickCountBroadcast = clickCountBroadcast.value();
                        Long areaProductClickCount =
                                areaProductClickCountBroadcast.get(areaAndProductId);
                        String cityInfos = "";
                        Long productId = Long.valueOf(areaAndProductId.split(",")[1]);
                        String area = areaAndProductId.split(",")[0];
                        String areaLevel = "";
                        while (iterator.hasNext()) {
                            String productAndCityInfo = iterator.next();
                            String cityInfo = StringUtils.getFieldFromConcatString(
                                    productAndCityInfo, "\\|", Constants.FIELD_CITYINFO);
                            if (!cityInfos.contains(cityInfo)) {
                                cityInfos += cityInfo + ",";

                            }
                        }
                        cityInfos = cityInfos.substring(0,cityInfos.length()-1);
                        if(area.equals("华东") || area.equals("华北")){
                            areaLevel = "A级";
                        }else if(area.equals("华中") || area.equals("华南")){
                            areaLevel = "B级";
                        }else if(area.equals("西北") || area.equals("西南")){
                            areaLevel = "C级";
                        }else if(area.equals("东北")){
                            areaLevel = "D级";
                        }
                        String productAndCityInfo = Constants.FIELD_PRODUCTID + "=" + productId + "|" +
                                Constants.FIELD_CITY_INFOS + "=" + cityInfos + "|" +
                                Constants.FIELD_AREA + "=" + area +"|" +
                                Constants.FIELD_CLICK_COUNT + "=" + areaProductClickCount +"|"+
                                Constants.FIELD_AREA_LEVEL + "=" + areaLevel;

                        list.add(new Tuple2<Long, String>(productId, productAndCityInfo));

                        return list.iterator();
                    }
                });



        JavaPairRDD<Long, Tuple2<String, Row>> joined =
                areaAndProductClickCountRDD.join(productInfoRDD);

        JavaPairRDD<String, String> areaAndProductFullRDD = joined.mapToPair(
                new PairFunction<Tuple2<Long, Tuple2<String, Row>>, String, String>() {
                    @Override
                    public Tuple2<String, String>
                    call(Tuple2<Long, Tuple2<String, Row>> tuple) throws Exception {
                        String productAndCityInfo = tuple._2._1;
                        Row productInfo = tuple._2._2;
                        String productName = productInfo.getString(1);
                        String extendInfo = productInfo.getString(2);
                        String area = StringUtils.getFieldFromConcatString(
                                productAndCityInfo, "\\|", Constants.FIELD_AREA);

                        JSONObject jsonObject = JSONObject.parseObject(extendInfo);

//                        String productStatus = ParamUtils.getParam(jsonObject, Constants.PARAM_PRODUCT_STATUS);
                        String productStatus = ParamUtils.getParamNotArray(jsonObject,Constants.PARAM_PRODUCT_STATUS);
                        if (productStatus.equals("0")) {
                            productStatus = "自营商品";
                        } else {
                            productStatus = "第三方商品";
                        }

                        productAndCityInfo += "|" + Constants.FIELD_PRODUCT_NAME + "=" + productName + "|"
                                + Constants.FIELD_PRODUCT_STATUS + "=" + productStatus;

                        return new Tuple2<String, String>(area, productAndCityInfo);
                    }
                });

        return areaAndProductFullRDD;

    }


    /**
     * 根据每个区域的商品的点击量排序，然后取前三个，写入数据库
     * @param areaAndProductFullRDD
     * @param taskId
     */
    private static void persistAreaTop3Product(
            JavaPairRDD<String, String> areaAndProductFullRDD, final long taskId) {

        //根据区域把数据聚合起来
        JavaPairRDD<String, Iterable<String>> groupedRDD = areaAndProductFullRDD.groupByKey();
        groupedRDD.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Iterable<String>>>>() {
            @Override
            public void call(Iterator<Tuple2<String, Iterable<String>>> iterator) throws Exception {
                Map<String,List<Tuple2<Long,String>>> map =
                        new HashMap<String, List<Tuple2<Long, String>>>();

                while (iterator.hasNext()){
                    List<Tuple2<Long,String>> list = new ArrayList<Tuple2<Long, String>>();
                    Tuple2<String, Iterable<String>> tuple = iterator.next();
                    String area = tuple._1;
                    Iterator<String> areaAndProductIterator = tuple._2.iterator();
                    while (areaAndProductIterator.hasNext()){
                        String areaAndProductInfo = areaAndProductIterator.next();
                        Long countClick = Long.valueOf(StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_CLICK_COUNT));
                        list.add(new Tuple2<Long, String>(countClick,areaAndProductInfo));
                    }
                    map.put(area,list);
                }
                IAreaTop3ProductDAO areaTop3ProductDAO = DAOFactory.getAreaTop3ProductDAO();
                for (Map.Entry<String,List<Tuple2<Long,String>>> mapEntry : map.entrySet()){
                    List<Tuple2<Long, String>> list = mapEntry.getValue();
                    Collections.sort(list, new Comparator<Tuple2<Long, String>>() {
                        @Override
                        public int compare(Tuple2<Long, String> o1, Tuple2<Long, String> o2) {
                            return (int) (o2._1 - o1._1);
                        }
                    });

                    List<AreaTop3Product> areaTop3Products = new ArrayList<AreaTop3Product>();
                    for (int i = 0; i <3 ; i++) {
                        Tuple2<Long, String> tuple = list.get(i);
                        long clickCount = tuple._1;
                        String areaAndProductInfo = tuple._2;

                        String area = StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_AREA);
                        String areaLevel = StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_AREA_LEVEL);
                        String productId = StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_PRODUCTID);
                        String cityInfos = StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_CITY_INFOS);
                        String productName = StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_PRODUCT_NAME);
                        String productStatus = StringUtils.getFieldFromConcatString(
                                areaAndProductInfo,"\\|",Constants.FIELD_PRODUCT_STATUS);

                        AreaTop3Product areaTop3Product = new AreaTop3Product();
                        areaTop3Product.setTaskid(taskId);
                        areaTop3Product.setArea(area);
                        areaTop3Product.setAreaLevel(areaLevel);
                        areaTop3Product.setProductid(Long.valueOf(productId));
                        areaTop3Product.setClickCount(clickCount);
                        areaTop3Product.setCityInfos(cityInfos);
                        areaTop3Product.setProductName(productName);
                        areaTop3Product.setProductStatus(productStatus);

                        areaTop3Products.add(areaTop3Product);

                    }
                    areaTop3ProductDAO.insertBatch(areaTop3Products);

                }
            }
        });


    }
}
