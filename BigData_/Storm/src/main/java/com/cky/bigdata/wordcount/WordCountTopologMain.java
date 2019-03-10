package com.cky.bigdata.wordcount;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class WordCountTopologMain {


    public static void main(String[] args) throws InvalidTopologyException, AlreadyAliveException {

        //1.准备一个ToplologyBuilder
        TopologyBuilder topologyBuilder = new TopologyBuilder();

//        topologyBuilder.setSpout("mySpout",new MySpout(),1);
        topologyBuilder.setSpout("mySpout",new TestSpout(),1);

        topologyBuilder.setBolt("mybolt1",new MySplitBolt(),10).shuffleGrouping("mySpout");

        topologyBuilder.setBolt("mybolt2",new MyCountBolt(),2).fieldsGrouping("mybolt1",new Fields("word"));

        //2.创建一个configuration，用来指定当前topology 需要的worker的数量
        Config config = new Config();

        config.setNumWorkers(2);

        //3.提交任务i-----两种模式  本地模式和集群模式

        //集群模式
//        StormSubmitter.submitTopology("mywordcount",config,topologyBuilder.createTopology());

        //本地模式
        LocalCluster localCluster = new LocalCluster();

        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }
}
