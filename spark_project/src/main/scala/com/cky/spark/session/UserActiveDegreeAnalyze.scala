package com.cky.spark.session

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * 用户活跃度分析
  */
object UserActiveDegreeAnalyze {

  case class UserActionLog(logId:Long,userId:Long,
                           actionTime:String,actionType:Long,purchaseMoney:Double) extends Serializable
  case class UserActionLogVO(logId:Long,userId:Long,actionValue:Long) extends Serializable
  case class UserActionLogTotalMoney(logId:Long,userId:Long,purchaseMoney:Double) extends Serializable

  def main(args: Array[String]): Unit = {

    val startDate = "2016-09-01"
    val endDate = "2016-11-01"


    val spark = SparkSession
      .builder()
      .appName("UserActiveDegreeAnalyze")
      .master("local")
      .config(new SparkConf())
      .getOrCreate()

    //导入spark的隐式函数
    import  spark.implicits._
    //导入spark sql的functions
    import org.apache.spark.sql.functions._

    //获取两份数据集
    val userActionLog = spark.read.json(
      "G:\\test\\user_action_log.json")

    val userBaseInfo = spark.read.json(
      "G:\\test\\user_base_info.json")


    userActionLog
    //第一步，过滤数据，找到指定范围内的数据
      .filter("actionTime>='"+startDate + "' and actionTime <='" + endDate+"' and actionType = 0")
    //第二步，：关联对应的用户基本信息数据
      .join(userBaseInfo,userActionLog("userId") === userBaseInfo("userId"))
    //第三步：进行分组
      .groupBy(userActionLog("userId"),userBaseInfo("username"))
    //第四步：进行聚合
      .agg(count(userActionLog("logId")).alias("actionCount"))
    //第五步：进行拍讯
      .sort($"actionCount".desc)
    //第六步：抽取指定的条数
      .limit(10)
    //第七步：展示结果
      .show()

    //第二个功能：获取指定时间范围购买金额最多的10个用户
    userActionLog
      .filter("actionTime>='"+startDate + "' and actionTime <='" + endDate+"' and actionType = 1")
      .join(userBaseInfo,userActionLog("userId") === userBaseInfo("userId"))
      .groupBy(userBaseInfo("userId"),userBaseInfo("username"))
      .agg(round(sum(userActionLog("purchaseMoney")),1).alias("totalPurchaseMoney"))
      .sort($"totalPurchaseMoney".desc)
      .limit(10)
      .show()


    //第三个功能：统计最近一个周期相对上一个周期访问次数增长最多的10个用户
        val userActionLogInFirstPeriod = userActionLog.as[UserActionLog]
            .filter("actionTime >= '2016-10-01' and actionTime <= '2016-10-31' and actionType = 0")
            .map{ userActionLogEntry => UserActionLogVO(userActionLogEntry.logId, userActionLogEntry.userId, 1) }

        val userActionLogInSecondPeriod = userActionLog.as[UserActionLog]
            .filter("actionTime >= '2016-01-01' and actionTime <= '2016-09-30' and actionType = 0")
            .map{ userActionLogEntry => UserActionLogVO(userActionLogEntry.logId, userActionLogEntry.userId, -1) }

        val userActionLogDS = userActionLogInFirstPeriod.union(userActionLogInSecondPeriod)

        userActionLogDS
            .join(userBaseInfo, userActionLogDS("userId") === userBaseInfo("userId"))
            .groupBy(userBaseInfo("userId"), userBaseInfo("username"))
            .agg(sum(userActionLogDS("actionValue")).alias("actionIncr"))
            .sort($"actionIncr".desc)
            .limit(10)
            .show()


    val userActionLongInFirstMoney = userActionLog.as[UserActionLog]
      .filter("actionTime >= '2016-10-01' and actionTime <= '2016-10-31' and actionType = 1")
      .map{userActionLogEntry => UserActionLogTotalMoney(userActionLogEntry.logId,userActionLogEntry.userId,userActionLogEntry.purchaseMoney)}


    val userActionLongInSecondLMoney = userActionLog.as[UserActionLog]
      .filter("actionTime >= '2016-01-01' and actionTime <= '2016-09-30' and actionType = 1")
      .map{userActionLogEntry => UserActionLogTotalMoney(userActionLogEntry.logId,userActionLogEntry.userId,userActionLogEntry.purchaseMoney)}

    val userActionLogTotalMoneyDs = userActionLongInFirstMoney.union(userActionLongInSecondLMoney)


    userActionLogTotalMoneyDs
      .join(userBaseInfo,userActionLogTotalMoneyDs("userId") === userBaseInfo("userId"))
      .groupBy(userBaseInfo("userId"),userBaseInfo("username"))
      .agg(round(sum(userActionLogTotalMoneyDs("purchaseMoney")),2).alias("totalPurchaseMoney"))
      .sort($"totalPurchaseMoney".desc)
      .limit(10)
      .show()

  }
}
