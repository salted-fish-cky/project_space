package com.akka.implic

//视图界定   Ordered
//View bound 必须传进去一个隐式函数

//class Chooser[T <% Ordered[T]] {
//
//  def choose(first:T,second:T) = {
//
//    if(first > second) first else second
//  }
//
//}


//上下文界定 Ordering
//必须传进去一个隐式转换的值

class Chooser [T:Ordering]{

  def choose(first:T,second:T) = {

    val ord = implicitly[Ordering[T]]

    if(ord.gt(first,second)) first else second
  }
}


object Chooser{

  def main(args: Array[String]): Unit = {

    import MyPredef._
    val c = new Chooser[Girl]

    val g1 = new Girl("anglebaby",90)

    val g2 = new Girl("hatano",99)

    val g = c.choose(g1,g2)

    println(g.name)
  }
}
