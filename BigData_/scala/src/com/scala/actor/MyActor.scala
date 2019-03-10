package com.scala.actor

import scala.actors.Actor


class MyActor extends Actor{
  override def act(): Unit = {
    while (true){
      receive {
        case "start" => {
          println("starting ...")
          Thread.sleep(5000)
          println("started")
        }
        case "stop" => {
          println("stopping ...")
          Thread.sleep(5000)
          println("stopped ...")
        }
      }
    }
  }
}


object MyActor{

  def main(args: Array[String]): Unit = {

    val a = new MyActor

    a.start()
    a ! "start"
    a ! "stop"
    println("消息发送完成！")

  }
}
