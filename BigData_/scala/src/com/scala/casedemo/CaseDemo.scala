package com.scala.casedemo

import scala.util.Random

//模式匹配
object CaseDemo extends App {

//  val arr = Array(0,1,5)
//  //匹配
//  arr match {
//      case Array(1,x,y) => println(x + " " +y)
//
//      case Array(0,1,5) => println("only 0")
//
//      case Array(0,_*) => println("0....")
//
//      case _ => println("something else")
//  }


//  val lst = List(0,3,111)
//
//  lst match {
//
//    case 0 :: Nil => println("only 0")   //Nil 表示一个空集合
//
//    case x :: y :: Nil => println(s"x:$x y:$y")  //前面加s 能用$表示变量
//
//    case 0 :: a => println("0....")
//
//    case _ => println("something else")
//  }



  val arr = Array("hello",1,-2.0,CaseDemo)

  val elem = arr(Random.nextInt(arr.length))

  println(elem)

  elem match {

    case a : Int => println("Int "+a)

    case b : Double if(b>=0) => println("Double "+b)

    case c : String => print(c)

    case _ => println("出错了")
  }

}
