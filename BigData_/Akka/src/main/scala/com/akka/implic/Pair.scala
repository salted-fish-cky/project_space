package com.akka.implic

//上界  [T<:]
class Pair[T<:Comparable[T]] {

  def bigger(first:T,second:T) = {

    if(first.compareTo(second) > 0) first else second
  }

}

object Pair{

  def main(args: Array[String]): Unit = {

    val p = new Pair[String]

    println(p.bigger("hadoop","spark"))
  }
}
