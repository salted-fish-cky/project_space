package com.scala.actor

import scala.actors.{Actor, Future}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source


class Task extends Actor{

  override def act(): Unit = {
    loop{
      react{
        case SubmitTask(filename) =>{

          val result = Source.fromFile(filename).getLines().flatMap(_.split(" ")).map((_,1)).toList.groupBy(_._1).mapValues(_.size)

          println(result+ "----")
          //把处理好的结果发送回去
          sender ! ResultTask(result)
        }

        case StopTask => {
          exit()
        }
      }
    }
  }

}

case class SubmitTask(filename:String)

case class ResultTask(map:Map[String,Int])

case object StopTask

object ActorWrodCount {

  def main(args: Array[String]): Unit = {

    val replySet = new mutable.HashSet[Future[Any]]()

    val resultList = new ListBuffer[ResultTask]()

    val files = Array[String]("G:\\words.txt","G:\\words.log")

    for (i <- files){

      val actor = new Task

      val reply = actor.start() !! SubmitTask(i)  //启动, 并发送消息,返回Future
      replySet += reply //把这些Future放到集合中


    }

    while (replySet.size > 0){

      val toCompute = replySet.filter(_.isSet)

      for (r <- toCompute){

        val result = r.apply().asInstanceOf[ResultTask]

        resultList += result

        replySet -= r
      }

      Thread.sleep(100)

    }

    //汇总的功能
    val fr = resultList.flatMap(_.map).groupBy(_._1).mapValues(_.foldLeft(0)(_+_._2))

    println(fr)


  }
}
