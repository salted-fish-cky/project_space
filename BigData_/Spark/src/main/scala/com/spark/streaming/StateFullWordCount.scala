package com.spark.streaming

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
//Streaming数据累加案例
object StateFullWordCount {

  //Seq 这个批次某个单词的次数
  //Option[Int] 以前的结果

  //分好组的数据
  val updateFunc = (iter: Iterator[(String , Seq[Int],Option[Int])]) => {

    iter.flatMap{case (x,y,z) => Some(y.sum+z.getOrElse(0)).map(m => (x,m))}

  }


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("StreamingWrodCount").setMaster("local[2]")

    val sc = new SparkContext(conf)

    sc.setCheckpointDir("G:\\ck")

    val ssc = new StreamingContext(sc,Seconds(5))

    //接收数据
    val ds = ssc.socketTextStream("number1",8888)

    val result = ds.flatMap(_.split(" ")).map((_,1)).updateStateByKey(updateFunc,new HashPartitioner(sc.defaultMinPartitions),true)

    result.print()

    ssc.start()

    ssc.awaitTermination()

  }

}
