package com.spark.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object AdvUserLocation {

  def main(args: Array[String]): Unit = {

    //通向Spark集群的入口
    val conf = new SparkConf().setAppName("ADUL").setMaster("local")

    val sc = new SparkContext(conf)

    val rdd0 = sc.textFile("F:\\download\\压缩包\\通过基站信息计算家庭地址和工作地址\\DDE7970F68.log").map(line => {

      val fields = line.split(",")

      val eventType = fields(3)

      val time = fields(1)

      val timeLong = if(eventType == "1") -time.toLong else time.toLong

      ((fields(0),fields(2)),timeLong)

    })

    val rdd1 = rdd0.reduceByKey(_+_).map(t => {

      val mobile = t._1._1

      val lac = t._1._2

      val time = t._2

      (lac,(mobile,time))


    })

    val rdd2 = sc.textFile("F:\\download\\压缩包\\通过基站信息计算家庭地址和工作地址\\lac_info.txt").map(line =>{

      val f = line.split(",")
      //（基站id）(经度，纬度)
      (f(0),(f(1),f(2)))
    })

    val rdd3 = rdd1.join(rdd2).map(t => {

      val lac = t._1

      val mobile = t._2._1._1

      val time = t._2._1._2

      val x = t._2._2._1

      val y = t._2._2._2

      (mobile,lac,time,x,y)
    })

    //rdd4分组后的
    val rdd4 = rdd3.groupBy(_._1)

    val rdd5 = rdd4.mapValues(it => {

      it.toList.sortBy(_._3).reverse.take(2)
    })

    rdd5.saveAsTextFile("G:\\out")

    println(rdd5.collect().toBuffer)

    sc.stop()
  }

}
