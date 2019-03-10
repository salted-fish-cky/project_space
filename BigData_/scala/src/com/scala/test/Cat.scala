package com.scala.test

class Cat(val id:String) {



  var name: String = _;

  //用this关键字定义辅助构造器
  def this(id:String,name:String){
    //每个辅助构造器必须以主构造器或其他的辅助构造器的调用开始
    this(id)

    this.name = name


  }


}

object Cat{

  def main(args: Array[String]): Unit = {

    val c = new Cat("1","zz")

    println(c.name)
  }
}

