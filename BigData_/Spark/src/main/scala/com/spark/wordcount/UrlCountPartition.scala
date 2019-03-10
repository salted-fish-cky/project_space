package com.spark.wordcount

import java.net.URL

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

object UrlCountPartition {

  def main(args: Array[String]): Unit = {

    //从数据库中加载规则

    val arr = Array("java.itcast.cn","php.itcast.cn","net.itcast.cn")

    val conf = new SparkConf().setAppName("UrlCountPartition").setMaster("local[2]")

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

      (host,(url,t._2))
    })


    val ints = rdd3.map(_._1).distinct().collect()

    println(ints.toBuffer)

    val hostPartitioner = new HostPartitioner(ints)

    val rdd4 = rdd3.partitionBy(hostPartitioner).mapPartitions(it => {

      it.toList.sortBy(_._2._2).reverse.take(2).iterator
    })

    rdd4.saveAsTextFile("G:\\out")

    println(rdd4.collect().toBuffer)

    sc.stop()
  }

}


//自定义Partitioner
class HostPartitioner(ins : Array[String]) extends Partitioner{

  val patrMap = new mutable.HashMap[String,Int]()
  var count = 0;

  for (i<- ins){

    patrMap += (i -> count)

    count +=  1

  }

  override def numPartitions : Int = ins.length

  override def getPartition(key: Any) = {

    patrMap.getOrElse(key.toString,0)

  }
}
