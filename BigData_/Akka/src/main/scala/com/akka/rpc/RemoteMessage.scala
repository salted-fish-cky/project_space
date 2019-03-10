package com.akka.rpc

trait RemoteMessage extends Serializable

//Worker -> Master
case class RegisterWorker(id: String, memory: Int, cores: Int) extends RemoteMessage

//Master -> Worker
case class RegisteredWorker(masterUrl: String) extends RemoteMessage

//Worker -> self
case object SendHeartbeat

//发送心跳 Worker -> Master
case class Heartbeat(id: String) extends RemoteMessage

//Master -> Master
case object CheckTimeOutWorker
