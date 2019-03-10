package com.akka.implic

import java.io.File

import scala.io.Source

//隐式增强
//object MyPredef {
//
//
//}


class RichFile(val f: File) {


  def read():String= {
    Source.fromFile(f).mkString
  }
}


object RichFile{


  def main(args: Array[String]): Unit = {

    import MyPredef._

    val f = new File("G:\\words.txt")

//    val richFile = new RichFile(f)
//      //装饰，显示的增强
//    val contents = richFile.read()

    //隐式增强
    val contents = f.read()

    println(contents)
  }
}
