package com.spark.wordcount

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

object AdvUrlCount {


  def main(args: Array[String]): Unit = {

    //从数据库中加载规则

    val arr = Array("java.itcast.cn","php.itcast.cn","net.itcast.cn")

    val conf = new SparkConf().setAppName("AdvUrlCount").setMaster("local[2]")

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


//    val rddJava = rdd3.filter(_._1 == "java.itcast.cn")
//
//    val sortedJava = rddJava.sortBy(_._3,false)

    for (ins <- arr){
      val rdd = rdd3.filter(_._1 == ins)

      val result = rdd.sortBy(_._3,false).take(3)

      print(result.toBuffer)

    }



    sc.stop()
  }
}
