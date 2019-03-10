package com.cky.sparkproject.spark.product;

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
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.*;

/**
 * 各区域top3热门商品统计Spark作业
 */
public class AreaTop3ProdectSpark {

    public static void main(String[] args){
        //1.构造spark上下文
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_PAGE);
        SparkUtils.setMaster(conf);

        JavaSparkContext sc = new JavaSparkContext(conf);

        SQLContext sqlContext = SparkUtils.getSQLContext(sc.sc());

        //注册自定义函数
        sqlContext.udf().register("concat_long_string",
                new Concat2UDF(),DataTypes.StringType);

        sqlContext.udf().register("group_concat_distinct",
                new GroupConcatDistinctUDAF());

        sqlContext.udf().register("get_json_object",
                new GetJsonObjectUDF(),DataTypes.StringType);

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

        //生成点击商品基础信息临时表
        generateClickProductBasicTable(sqlContext,cityId2ClickActionRDD,cityId2CityInfoRDD);

        generateTempAreaProductClickCountTable(sqlContext);

        //生成包含完整商品信息的各区域各商品点击次数的临时表
        generateTempAreaFullProductClickCountTable(sqlContext);

        //使用开窗函数获取各个区域内点击次数排名前3的热门商品
        JavaRDD<Row> areaTop3ProductRDD = getAreaTop3ProductRDD(sqlContext);

        List<Row> rows = areaTop3ProductRDD.collect();
        persistAreaTop3Product(taskId, rows);
        sc.close();
    }


    private static JavaPairRDD<Long, Row> getCityId2ClickActionRDDByDate(
            String startDate,String endDate,SQLContext sqlContext
    ){
        String sql =
                "SELECT "
                        + "city_id,"
                        + "click_product_id product_id "
                        + "FROM user_visit_action "
                        + "WHERE click_product_id IS NOT NULL "
                        + "AND click_product_id != 'NULL' "
                        + "AND click_product_id != 'null' "
                        + "AND action_time>='" + startDate + "' "
                        + "AND action_time<='" + endDate + "'";
        sql = "select city_id," +
                "click_product_id product_id " +
                "from user_visit_action " +
                " where click_product_id IS NOT NULL and t_date>='"+startDate+"'" +
                " and t_date<='"+endDate+"'";
        Dataset<Row> rows = sqlContext.sql(sql);

        System.out.println(rows.takeAsList(10));

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

        System.out.println(rows.takeAsList(10));

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
     * 生成点击商品基础信息临时表
     * @param sqlContext
     * @param cityid2clickActionRDD
     * @param cityid2CityInfoRDD
     */
    private static void generateClickProductBasicTable(
            SQLContext sqlContext,
            JavaPairRDD<Long,Row> cityid2clickActionRDD,
            JavaPairRDD<Long,Row> cityid2CityInfoRDD
    ){

        JavaPairRDD<Long, Tuple2<Row, Row>> joinedRDD =
                cityid2CityInfoRDD.join(cityid2clickActionRDD);

        //将上面的javaPairRDD，转换成一个javaRDD<Row>
        JavaRDD<Row> mapedRDD = joinedRDD.map(
                new Function<Tuple2<Long, Tuple2<Row, Row>>, Row>() {
                    @Override
                    public Row call(Tuple2<Long, Tuple2<Row, Row>> tuple) throws Exception {
                        Long cityId = tuple._1;
                        Row cityInfo = tuple._2._1;
                        Row clickAction = tuple._2._2;

                        long productId = clickAction.getLong(1);
                        String cityName = cityInfo.getString(1);
                        String area = cityInfo.getString(2);

                        return RowFactory.create(cityId, cityName, area, productId);
                    }
                });

        //基于JavaRDD<Row>的格式，就可以将其转换为
        List<StructField> structFields = new ArrayList<StructField>();
        structFields.add(DataTypes.createStructField("city_id",DataTypes.LongType,true));
        structFields.add(DataTypes.createStructField("city_name",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("area",DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("product_id",DataTypes.LongType,true));

        StructType schema = DataTypes.createStructType(structFields);

        Dataset<Row> df = sqlContext.createDataFrame(mapedRDD, schema);

        System.out.println(df.takeAsList(10));
        //将DataFrame中的数据，注册成一张hive临时表
        df.registerTempTable("tmp_click_product_basic");
    }

    /**
     * 生成各区域各商品点击次数临时表
     * @param sqlContext
     */
    private static void generateTempAreaProductClickCountTable(SQLContext sqlContext) {

        //按照area和product_id两个字段进行分组
        //计算出各区域各商品的点击次数
        //可以获取到每个area下的每个product_id的城市信息拼接起来的串
        String sql =
                "SELECT "
                        + "area,"
                        + "product_id,"
                        + "count(*) click_count, "
                        + "group_concat_distinct(concat_long_string(city_id,city_name,':')) city_infos "
                        + "FROM tmp_click_product_basic "
                        + "GROUP BY area,product_id ";
        Dataset<Row> df = sqlContext.sql(sql);
        //在次将查询处理的数据注册为一个临时表
        //各区域各商品的点击次数
        df.registerTempTable("tmp_area_product_click_count");
    }

    /**
     * 生成区域商品点击次数临时表
     * @param sqlContext
     */
    private static void generateTempAreaFullProductClickCountTable(
            SQLContext sqlContext){

        String sql =
                "SELECT "
                        + "tapcc.area,"
                        + "tapcc.product_id,"
                        + "tapcc.click_count,"
                        + "tapcc.city_infos,"
                        + "pi.product_name,"
                        + "if(get_json_object(pi.extend_info,'product_status')=0,'自营商品','第三方商品') product_status "
                        + "FROM tmp_area_product_click_count tapcc "
                        + "JOIN product_info pi ON tapcc.product_id=pi.product_id ";

        Dataset<Row> df = sqlContext.sql(sql);
        System.out.println(df.takeAsList(5));

        df.registerTempTable("tmp_area_fullprod_click_count");
    }

    /**
     * 获取各区域top3热门商品
     * @param sqlContext
     * @return
     */
    private static JavaRDD<Row> getAreaTop3ProductRDD(SQLContext sqlContext){
        String sql =
                "SELECT "
                        + "area,"
                        + "CASE "
                        + "WHEN area='华北' OR area='华东' THEN 'A级' "
                        + "WHEN area='华南' OR area='华中' THEN 'B级' "
                        + "WHEN area='西北' OR area='西南' THEN 'C级' "
                        + "ELSE 'D级' "
                        + "END area_level,"
                        + "product_id,"
                        + "click_count,"
                        + "city_infos,"
                        + "product_name,"
                        + "product_status "
                        + "FROM ("
                        + "SELECT "
                        + "area,"
                        + "product_id,"
                        + "click_count,"
                        + "city_infos,"
                        + "product_name,"
                        + "product_status,"
                        + "ROW_NUMBER() OVER(PARTITION BY area ORDER BY click_count DESC) rank "
                        + "FROM tmp_area_fullprod_click_count "
                        + ") t "
                        + "WHERE rank<=3";

        Dataset<Row> df = sqlContext.sql(sql);

        return df.javaRDD();

    }


    /**
     * 将计算出来的各区域top3热门商品写入MySQL中
     * @param rows
     */
    private static void persistAreaTop3Product(long taskid, List<Row> rows) {
        List<AreaTop3Product> areaTop3Products = new ArrayList<AreaTop3Product>();

        System.out.println(rows);
        for(Row row : rows) {
            AreaTop3Product areaTop3Product = new AreaTop3Product();
            areaTop3Product.setTaskid(taskid);
            areaTop3Product.setArea(row.getString(0));
            areaTop3Product.setAreaLevel(row.getString(1));
            areaTop3Product.setProductid(row.getLong(2));
            areaTop3Product.setClickCount(Long.valueOf(String.valueOf(row.get(3))));
            areaTop3Product.setCityInfos(row.getString(4));
            areaTop3Product.setProductName(row.getString(5));
            areaTop3Product.setProductStatus(row.getString(6));
            areaTop3Products.add(areaTop3Product);
            System.out.println(row);
        }

        IAreaTop3ProductDAO areTop3ProductDAO = DAOFactory.getAreaTop3ProductDAO();
        areTop3ProductDAO.insertBatch(areaTop3Products);
    }


}
