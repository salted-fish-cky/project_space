package com.akka.func

//高阶函数    柯里化     隐式转换
object HighFunc {

  val func: Int => Int = {x => x*x}

  def m0(x: Int) : Int = x*x

  def m1(x: Int)(y: Int) = x*y

  def m2(x: Int) = (y: Int) => x*y

  def m3 = (x: Int) => x*x

  def main(args: Array[String]): Unit = {

    val arr = Array(1,2,3,4,5)

    val res = arr.map(m3)

    println(res.toBuffer)
  }
}
