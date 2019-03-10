package com.akka.implic
//隐式转换的调用

//所有的隐式值和隐式方法必须放在object中
object Context{
  implicit val name = "cky"
  implicit val i = 1
}

object ImplicitValue {


  def sayHi()(implicit name:String = "Zz"):Unit = {

    println(s"hi~:$name")
  }

  def main(args: Array[String]): Unit = {
    //导入隐式方法
    import Context._
    sayHi()
  }
}
