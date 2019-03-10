package com.scala.wordcount

object WordCount {

  def main(args: Array[String]): Unit = {

    val lines = List("hello tom hello jerry", "hello jerry", "hello kitty")

    val res = lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).mapValues(_.size).toList.sortBy(_._2).reverse;

    println(res)
  }
}
