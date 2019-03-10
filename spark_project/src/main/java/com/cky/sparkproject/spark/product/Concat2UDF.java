package com.cky.sparkproject.spark.product;

import org.apache.spark.sql.api.java.UDF3;

/**
 * 将两个字段拼接起来
 */
public class Concat2UDF implements UDF3<Long,String,String,String> {
    @Override
    public String call(Long v1, String v2, String split) throws Exception {
        return String.valueOf(v1) +split+ v2 ;
    }
}