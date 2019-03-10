package com.akka.rpc

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._


class Master(val host: String,val port: Int) extends Actor{


  //workerId -> WorkerInfo
  val idToWorker = new mutable.HashMap[String,WorkerInfo]()

    //WorkerInfo
  val workers = new mutable.HashSet[WorkerInfo]()

  val CHECK_INTERVAL = 15000

  println("constructor invoked")


  override def preStart(): Unit = {

    println("preStart invoked")

    //导入隐式转换
    import context.dispatcher
    context.system.scheduler.schedule(0 millis,CHECK_INTERVAL millis,self,CheckTimeOutWorker)
  }

  //用于接收消息
  override def receive :Receive = {

    case RegisterWorker(id,memory,cores) => {
      //判断一下，是否已经注册过
      if(!idToWorker.contains(id)){
        //把Worker的信息封装起来保存在内存中
        val workerInfo = new WorkerInfo(id,memory,cores)

        idToWorker(id) = workerInfo;

        workers += workerInfo

        sender ! RegisteredWorker(s"akka.tcp://MasterSystem@$host:$port/user/Master")
      }


    }

    case Heartbeat(id) => {

      if(idToWorker.contains(id)){

        val workerInfo = idToWorker(id);
        //报活
        val currentTime = System.currentTimeMillis()

        workerInfo.lastHeartbeatTime = currentTime

      }

    }

    case CheckTimeOutWorker => {

      val currentTime = System.currentTimeMillis()
      //要移除的Worker
      val toRemove = workers.filter(x=> currentTime - x.lastHeartbeatTime > CHECK_INTERVAL)

      for(worker <- toRemove){

        workers -= worker

        idToWorker -= (worker.id)
      }

      println(workers.size)

    }

    case "hello" => {
      println("hello")
    }



  }
}


object Master{

  def main(args: Array[String]): Unit = {

    val host = args(0)

    val port = args(1).toInt

    println(args(0))
    //准备配置
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config = ConfigFactory.parseString(configStr)

    //ActorSystem老大，辅助创建和监控下面的Actor，他是单例的
    val actorSystem = ActorSystem("MasterSystem",config)

    //创建actor
    val master = actorSystem.actorOf(Props(new Master(host,port)),"Master")

    //自己给自己发送消息
    master ! "hello"

//    actorSystem.awaitTermination()
  }
}
