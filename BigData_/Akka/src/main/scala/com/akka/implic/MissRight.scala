package com.akka.implic

class MissRight[T] {

  //相当于ViewBound
  def choose(first:T,second: T)(implicit  ord: T => Ordered[T]): T = {

    if(first > second) first else second
  }

  //相当于 ContextBound
  def select (first:T,second: T)(implicit ord: Ordering[T]): T = {

    if(ord.gt(first,second)) first else second
  }

  def random (first:T,second:T)(implicit  ord: Ordering[T]): T = {

    //把Ordering 转换成Ordered
    import Ordered.orderingToOrdered

    if(first > second) first else second

  }

}


object MissRight{

  def main(args: Array[String]): Unit = {

    import MyPredef._
    val mr = new MissRight[Girl]

    val g1 = new Girl("hatanao",95)

    val g2 = new Girl("canglaoshi",98)

//    val g = mr.choose(g1,g2)
    val g = mr.select(g1,g2)


    println(g.name)
  }
}
