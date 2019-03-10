package com.cky.sparkproject.utils;

import com.alibaba.fastjson.JSONObject;
import com.cky.sparkproject.conf.ConfigurationManager;
import com.cky.sparkproject.constants.Constants;
import com.cky.sparkproject.test.MockData;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;

public class SparkUtils {


    public static void setMaster(SparkConf conf){
        Boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if(local){
            conf.setMaster("local");
        }
    }

    public static SQLContext getSQLContext(SparkContext sc){
        Boolean isLocal = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if(isLocal){
            return new SQLContext(sc);
        }else{
            return new HiveContext(sc);
        }
    }

    public static void mockData(JavaSparkContext sc,SQLContext sqlContext){
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
    public static JavaRDD<Row> getActionRDDByDateRange(
            SQLContext sqlContext,JSONObject taskParam){
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);
        String sql = "select * " +
                "from user_visit_action " +
                " where t_date>='"+startDate+"'" +
                " and t_date<='"+endDate+"'";
//        sql = "select * from user_info";
        Dataset<Row> rows = sqlContext.sql(sql);
        /**
         * 生产环境中要冲分区
         */
//        return rows.javaRDD().repartition(100);
        return rows.javaRDD();
    }
}
