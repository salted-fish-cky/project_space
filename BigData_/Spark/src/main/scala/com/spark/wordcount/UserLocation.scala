package com.spark.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object UserLocation {

  def main(args: Array[String]): Unit = {

    //通向Spark集群的入口
    val conf = new SparkConf().setAppName("UL").setMaster("local")

    val sc = new SparkContext(conf)

    val mbt = sc.textFile("F:\\download\\压缩包\\通过基站信息计算家庭地址和工作地址\\DDE7970F68.log").map(line => {

      val fields = line.split(",")

      val eventType = fields(3)

      val time = fields(1)

      val timeLong = if(eventType == "1") -time.toLong else time.toLong

      (fields(0) + "-" + fields(2),timeLong)

    })

    println(mbt.collect().toBuffer)

    //(18688888888-16030401EAFB68F1E3CDF819735E1C66,40320654252400)
    val rdd1 = mbt.groupBy(_._1).mapValues(_.foldLeft(0L)(_ + _._2))

    val rdd2 = rdd1.map(t => {

      val mobile_bs = t._1

      val mobile = mobile_bs.split("-")(0)

      val lac = mobile_bs.split("-")(1)

      val time = t._2

      (mobile,lac,time)
    })

    val rdd3 = rdd2.groupBy(_._1)

    val rdd4 = rdd3.mapValues(it => {

      it.toList.sortBy(_._3).reverse.take(2)
    })

    println(rdd4.collect().toBuffer)
    sc.stop()
  }

}
