package com.scala.test

//继承类 可以实现多个接口
class People extends Human with Animal {

  //重写接口的方法
  override def run(): Unit = {


    println("People run")
  }

  override def eat():String = {
    "吃"
  }
}

object People{

  def main(args: Array[String]): Unit = {

    val p = new People

    val e = p.eat()

    p.run()

    println(e)
  }
}
