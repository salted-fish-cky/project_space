
package com.spark.wordcount

import java.net.URL

import com.alibaba.fastjson.JSON
import org.apache.spark.{SparkConf, SparkContext}

object Test {

  def main(args: Array[String]): Unit = {
    //通向Spark集群的入口
    val conf = new SparkConf().setAppName("Test").setMaster("local[2]")

    val sc = new SparkContext(conf)

    //rdd1将数据切分，元组中放的是（URL，1）
    val rdd1 = sc.textFile("F:\\download\\压缩包\\track.log").map(line => {

      val json = JSON.parseObject(line.toString())

      (json.getString("time"),json.getString("client"),json.getString("referrer"),json.getString("type"))

    })

    val rdd2 = rdd1.filter(_._3 != "")

    val rdd3 = rdd2.map( r => {
      ((r._2,r._3),r._1,r._4)
    })

    val rdd4 = rdd3.groupBy(_._1).map(r =>(r._1._2,1)).reduceByKey(_+_)

    val rdd5 = rdd1.map(r => {
      ((r._2,r._4),r._1,r._3)
    })

    val rdd6 = rdd5.groupBy(_._1).groupBy(_._1._1).filter(_._2.size == 2)

    //客户的来源
    println(rdd4.collect().toBuffer)
    //新增用户的数量
    println(rdd6.count())


    sc.stop()

  }

}
