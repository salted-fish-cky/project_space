package com.spark.wordcount

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

object UrlCount {

  def main(args: Array[String]): Unit = {
    //通向Spark集群的入口
    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")

    val sc = new SparkContext(conf)

    //rdd1将数据切分，元组中放的是（URL，1）
    val rdd1 = sc.textFile("F:\\download\\压缩包\\itcast.log").map(line => {

      val f = line.split("\t")

      (f(1),1)
    })

    val rdd2 = rdd1.reduceByKey(_+_)

    val rdd3 = rdd2.map(t => {

      val url = t._1

      val host = new URL(url).getHost

      (host,url,t._2)
    })

    val rdd4 = rdd3.groupBy(_._1).mapValues( it=>{

      it.toList.sortBy(_._3).reverse.take(3)
    })

    println(rdd4.collect().toBuffer)

    sc.stop()

  }

}
