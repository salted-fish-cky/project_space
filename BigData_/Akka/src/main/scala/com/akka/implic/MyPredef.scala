package com.akka.implic

import java.io.File

object MyPredef {

  implicit def FileToRichFile(f:File) = new RichFile(f)

//  implicit val girl2Ordered = (g:Girl) => new Ordered[Girl] {
//
//    override def compare(that: Girl) = {
//
//      g.faceValue - that.faceValue
//    }
//  }

//  implicit val girl2Ordering = new Ordering[Girl] {
//    override def compare(x: Girl, y: Girl) = {
//
//      x.faceValue - y.faceValue
//    }
//  }

  trait girl2Ordering extends Ordering[Girl]{

    override def compare(x: Girl, y: Girl) = {

      x.faceValue - y.faceValue
    }
  }

  implicit object Girl extends girl2Ordering


}
