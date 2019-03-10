package com.spark.game

import org.apache.spark.{SparkConf, SparkContext}

object GameKPI {



  def main(args: Array[String]): Unit = {

    val queryTime = "2016-02-02 00:00:00"

    val beginTime = TimeUtils(queryTime)

    val endTime = TimeUtils.getCertainDayTime(+1)

    val conf = new SparkConf().setAppName("GameKPI").setMaster("local[*]")

    val sc = new SparkContext(conf)
//切分之后的数据
    val splitedLogs = sc.textFile("F:\\download\\压缩包\\GameLog.txt").map(_.split("\\|"))
//过滤后并缓存
    val filteredLogs = splitedLogs.filter(fields => FilterUtils.filterByTime(fields,beginTime,endTime)).cache()

    //日新增用户
    val dnu = filteredLogs.filter(fields => FilterUtils.filterByType(fields,EventType.LOGIN)).count()

    println(dnu)

    //日活跃用户
    val dau = filteredLogs.filter(fields => FilterUtils.filterByTypes(fields,EventType.REGISTER,EventType.LOGIN))
      .map(_(3))
      .distinct()
      .count()

    println(dau)

    //留存率 ：某段时间的新增用户的数记为A，经过一段时间后，仍然使用的用户占新增用户A的比例即为留存率
    val t1 = TimeUtils.getCertainDayTime(-1)
    //前一天的注册用户
    val lastDayRegUser = splitedLogs.filter(fields => FilterUtils.filterByTypeAndTime(fields,EventType.REGISTER,t1,beginTime))
      .map(x => (x(3),1))
    //今天登陆的用户
    val todayLoginUser = filteredLogs.filter(fields => FilterUtils.filterByType(fields,EventType.LOGIN))
      .map(x => (x(3),1))
      .distinct()

    val drl:Double = lastDayRegUser.join(todayLoginUser).count()

    println(drl)

    val drll = drl/lastDayRegUser.count()

    println(drll)

    sc.stop()

  }

}
