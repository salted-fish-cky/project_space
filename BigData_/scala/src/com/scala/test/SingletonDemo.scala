package com.scala.test

class SingletonDemo {

}

object SingletonDemo{

  //会默认根据构造器的参数调相同参数的apply
  def apply():Unit = {
    println("apply")
  }


  def main(args: Array[String]): Unit = {
    val s = new SingletonDemo()
    println(s)
  }
}
