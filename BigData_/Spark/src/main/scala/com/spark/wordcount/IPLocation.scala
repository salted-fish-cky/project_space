package com.spark.wordcount


import java.sql.{Connection, Date, DriverManager, PreparedStatement}

import org.apache.spark.{SparkConf, SparkContext}

object IPLocation {


  val data2MySQL = (iterator: Iterator[(String, Int)]) => {
    var conn: Connection = null
    var ps : PreparedStatement = null
    val sql = "INSERT INTO location_info (location, counts, accesse_date) VALUES (?, ?, ?)"
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata", "root", "123456")
      iterator.foreach(line => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, line._1)
        ps.setInt(2, line._2)
        ps.setDate(3, new Date(System.currentTimeMillis()))
        ps.executeUpdate()
      })
    } catch {
      case e: Exception => println("Mysql Exception")
    } finally {
      if (ps != null)
        ps.close()
      if (conn != null)
        conn.close()
    }
  }

  def ip2Long(ip: String): Long = {
    val fragments = ip.split("[.]")
    var ipNum = 0L
    for (i <- 0 until fragments.length){
      ipNum =  fragments(i).toLong | ipNum << 8L
    }
    ipNum
  }

  //二分法
  def binarySearch(lines: Array[(String, String, String)], ip: Long) : Int = {
    var low = 0
    var high = lines.length - 1
    while (low <= high) {
      val middle = (low + high) / 2
      if ((ip >= lines(middle)._1.toLong) && (ip <= lines(middle)._2.toLong))
        return middle
      if (ip < lines(middle)._1.toLong)
        high = middle - 1
      else {
        low = middle + 1
      }
    }
    -1
  }


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("IPLocation").setMaster("local[2]")

    val sc = new SparkContext(conf)

    val ipRulesRdd = sc.textFile("F:\\download\\压缩包\\ip.txt").map(line => {

      val fields = line.split("\\|")

      val start_num = fields(2)

      val end_num = fields(3)

      val province = fields(6)

      (start_num,end_num,province)
    })

    //全部的ip映射规则
    val ipRulesArray = ipRulesRdd.collect()

    //广播规则
    val ipRulesBroadcast =  sc.broadcast(ipRulesArray)

    //加载要处理的数据
    val ipRdd = sc.textFile("F:\\download\\压缩包\\access.log").map(line => {

      val fields = line.split("\\|")

      fields(1)
    })

    val result = ipRdd.map(ip => {

      val ipNum = ip2Long(ip)

      val index = binarySearch(ipRulesBroadcast.value,ipNum)

      val info = ipRulesBroadcast.value(index)

      info
    }).map(t => (t._3,1)).reduceByKey(_+_)

    //保存到数据库中
    result.foreachPartition(data2MySQL)

    println(result.collect().toBuffer)


    sc.stop()
  }

}
