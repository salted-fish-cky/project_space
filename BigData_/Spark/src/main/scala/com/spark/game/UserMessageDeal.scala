package com.spark.game

import com.alibaba.fastjson.JSON
import com.spark.utils.LoggerLevels
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer

object UserMessageDeal {


  def main(args: Array[String]): Unit = {

    LoggerLevels.setStreamingLogLevels()

    val Array(zkQuorum, group, topics, numThreads) = args

    val conf = new SparkConf().setAppName("UserMessageDeal").setMaster("local[2]")


    val ssc = new StreamingContext(conf, Seconds(5))

//    val userList = new ListBuffer[UserMessage]()


    ssc.checkpoint("G://ck1")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap

    val data = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap, StorageLevel.MEMORY_AND_DISK_SER)

    data.print()
   val logs = data.map(line => {

      val json = JSON.parseObject(line.toString())

//     val userMessage = UserMessage(json.getString("time"),json.getString("client"),
//       json.getString("domain"),
//       json.getString("url"),json.getString("title"),json.getString("referrer"),
//       json.getString("sh"),json.getString("cd"),json.getString("lang"),
//       json.getString("ua"),json.getString("trace"),json.getString("type"))
//
//     userList += userMessage

     (json.getString("time"),json.getString("client"),json.getString("referrer"),json.getString("type"))

   })


    val rdd1 = logs.filter(_._3 != "")

    val rdd2 = rdd1.map(r => {
      ((r._2,r._3),r._1,r._4)
    })


    val rdd3 = rdd2.mapPartitions(it => {
      it.toList.groupBy(_._1).toIterator
    })

    val rdd4 = rdd3.map(r => {
      (r._1._2,1)
    }).reduceByKey(_+_)

    rdd4.print()





    logs.print()

    ssc.start()

    ssc.awaitTermination()

  }

}

//case class UserMessage(time : String,client : String,domain : String,
//                       url : String,title : String,referrer : String,
//                       sh : String,cd : String,lang : String,ua : String,
//                       trace : String,_type : String)
