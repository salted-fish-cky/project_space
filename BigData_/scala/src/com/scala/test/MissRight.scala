package com.scala.test

import scala.io.Source

class MissRight {


  println(123)


  try{
    //读取文件
    val contexts = Source.fromFile("C:\\Users\\cky\\Desktop\\zzz.sql").mkString
    println(contexts)
  }catch {
    case e :Exception => e.printStackTrace()
  }finally {

  }

}


object MissRight{
  def main(args: Array[String]): Unit = {
    val m = new MissRight
    println(m)
  }
}
