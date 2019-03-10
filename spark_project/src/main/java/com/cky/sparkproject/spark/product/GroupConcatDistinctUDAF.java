package com.cky.sparkproject.spark.product;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;

/**
 * 组内拼接去重函数()
 */
public class GroupConcatDistinctUDAF extends UserDefinedAggregateFunction{
    //指定输入数据的字段类型
    private StructType inputSchema = DataTypes.createStructType(Arrays.asList(
        DataTypes.createStructField("cityInfo",DataTypes.StringType,true)
    ));
    //指定缓冲数据的字段类型
    private StructType bufferSchema = DataTypes.createStructType(Arrays.asList(
            DataTypes.createStructField("bufferCityInfo",DataTypes.StringType,true)
    ));
    //指定返回类型
    private DataType dataType = DataTypes.StringType;
    private boolean deterministic = true;
    @Override
    public StructType inputSchema() {

        return inputSchema;
    }

    @Override
    public StructType bufferSchema() {
        return bufferSchema;
    }

    @Override
    public DataType dataType() {
        return dataType;
    }

    @Override
    public boolean deterministic() {
        return deterministic;
    }

    /**
     * 初始化
     *
     * @param buffer
     */
    @Override
    public void initialize(MutableAggregationBuffer buffer) {
        buffer.update(0,"");
    }

    /**
     * 更新
     * 可以认为是，一个一个的将组内的字段值传递进来
     * @param buffer
     * @param input
     */
    @Override
    public void update(MutableAggregationBuffer buffer, Row input) {
        //缓冲中的已经拼接过的城市信息串
        String bufferCityInfo = buffer.getString(0);
        //刚刚传递进来的某个城市信息
        String cityInfo = input.getString(0);

        //实现去重逻辑
        if(!bufferCityInfo.contains(cityInfo)){
            if("".equals(bufferCityInfo)){
                bufferCityInfo += cityInfo;
            }else{
                bufferCityInfo += "," + cityInfo;
            }

            buffer.update(0,bufferCityInfo);
        }
    }

    /**
     * 合并
     * update操作，可能是针对一个分组内的部分数据，在某个节点上发生的
     * 但是可能一个分组内的数，会分布在多个节点上处理
     * @param buffer1
     * @param buffer2
     */
    @Override
    public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
        String bufferCityInfo1 = buffer1.getString(0);
        String bufferCityInfo2 = buffer2.getString(0);

        for (String cityInfo : bufferCityInfo2.split(",")){
            if(!bufferCityInfo1.contains(cityInfo)){
                bufferCityInfo1 += cityInfo;
            }else {
                bufferCityInfo1 += "," + cityInfo;
            }

        }
        buffer1.update(0,bufferCityInfo1);
    }

    @Override
    public Object evaluate(Row row) {
        return row.getString(0);
    }
}
