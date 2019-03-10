package oderandlogmonitor;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import oderandlogmonitor.bolt.*;
import oderandlogmonitor.spout.ClickLogSpout;
import oderandlogmonitor.spout.OrderMqSpout;
import oderandlogmonitor.spout.StringScheme;
import oderandlogmonitor.spout.UserSafeMqLogSpout;


public class OderandlogmonitorMain {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("clickLog",new ClickLogSpout(),2);
        topologyBuilder.setSpout("orderMq",new OrderMqSpout(),2);
        topologyBuilder.setSpout("userSafeMq",new UserSafeMqLogSpout(new StringScheme()),2);

        topologyBuilder.setBolt("filterBolt",new FilterBolt(),3)
        .shuffleGrouping("clickLog")
        .shuffleGrouping("orderMq")
        .shuffleGrouping("userSafeMq");
//        topologyBuilder.setBolt("filterBolt",new FilterBolt(),3).shuffleGrouping("paymentInfoParse");
        topologyBuilder.setBolt("checkRuleBolt",new CheckRuleBolt(),3).shuffleGrouping("filterBolt");
        topologyBuilder.setBolt("notifyMailBolt",new NotifyMailBolt(),3).shuffleGrouping("checkRuleBolt");
        topologyBuilder.setBolt("notifySMSBolt",new NotifySMSBolt(),3).shuffleGrouping("checkRuleBolt");
        topologyBuilder.setBolt("save2dbBolt",new Save2dbBolt(),3).shuffleGrouping("checkRuleBolt")
        .shuffleGrouping("orderMq");


        //启动topology的配置信息
        Config topologConf = new Config();
        //TOPOLOGY_DEBUG(setDebug), 当它被设置成true的话， storm会记录下每个组件所发射的每条消息。
        //这在本地环境调试topology很有用， 但是在线上这么做的话会影响性能的。
        topologConf.setDebug(true);
        //storm的运行有两种模式: 本地模式和分布式模式.
        if (args != null && args.length > 0) {
            //定义你希望集群分配多少个工作进程给你来执行这个topology
            topologConf.setNumWorkers(2);
            //向集群提交topology
            StormSubmitter.submitTopologyWithProgressBar(args[0], topologConf, topologyBuilder.createTopology());
        } else {
            topologConf.setMaxTaskParallelism(10);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("monitor", topologConf, topologyBuilder.createTopology());
//            Utils.sleep(10000000);
//            cluster.shutdown();
        }

    }
}
