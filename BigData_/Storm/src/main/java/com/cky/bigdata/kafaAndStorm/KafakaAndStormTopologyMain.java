package com.cky.bigdata.kafaAndStorm;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class KafakaAndStormTopologyMain {

    public static void main(String[] args) throws InvalidTopologyException, AlreadyAliveException{

        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("kafkaSpout",new KafkaSpout(new SpoutConfig(
                new ZkHosts("number3:2181,number4:2181,number5:2181"),
                "orderMq",
                "/myKafka",
                "kafkaSpout")),1);

        topologyBuilder.setBolt("mybolt1",new ParserOrderMqBolt(),1).shuffleGrouping("kafkaSpout");


        Config config = new Config();
        //3.提交任务i-----两种模式  本地模式和集群模式

        if(args.length>0){
            //集群模式
        StormSubmitter.submitTopology(args[0],config,topologyBuilder.createTopology());
        }else {

            //本地模式
            LocalCluster localCluster = new LocalCluster();

            localCluster.submitTopology("kafkaAndStorm",config,topologyBuilder.createTopology());
        }

    }
}
