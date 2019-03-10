package com.spark.streaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingWrodCount {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("StreamingWrodCount").setMaster("local[2]")

    val sc = new SparkContext(conf)

    val ssc = new StreamingContext(sc,Seconds(5))

    //接收数据
    val ds = ssc.socketTextStream("number1",8888)

    //DStream是一个特殊的RDD
    val result = ds.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()

    ssc.awaitTermination()
  }
}
