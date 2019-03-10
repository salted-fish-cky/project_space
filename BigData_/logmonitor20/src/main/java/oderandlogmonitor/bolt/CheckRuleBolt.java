package oderandlogmonitor.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import oderandlogmonitor.constant.LogTypeConstant;
import oderandlogmonitor.domain.LogMessage;
import oderandlogmonitor.domain.Message;

import java.util.List;
import java.util.Map;

public class CheckRuleBolt extends BaseRichBolt {

    OutputCollector collector;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;

    }

    @Override
    public void execute(Tuple tuple) {
        String type = (String) tuple.getValueByField("type");
        System.out.println(type + "+++++++++++++++++++++++");
        if(type.equals(LogTypeConstant.TYPE1)){
            LogMessage logMessage = (LogMessage) tuple.getValueByField("arg1");
            collector.emit(new Values(type,logMessage,logMessage.getType()));
        }
        else if(type.equals(LogTypeConstant.TYPE2)){
            Message message = (Message) tuple.getValueByField("arg2");
            String appId = tuple.getStringByField("arg1");

            collector.emit(new Values(type,appId,message));

        }else if(type.equals(LogTypeConstant.TYPE2)){
            String orderId = (String) tuple.getValueByField("arg1");
            List triggerList = (List) tuple.getValueByField("arg2");

            collector.emit(new Values(type,orderId,triggerList));
        }else{
            collector.emit(new Values(LogTypeConstant.TYPE4,"null","null"));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("type","arg1","arg2"));
//        if(index == 1){
//            outputFieldsDeclarer.declare(new Fields("logMessage","type"));
//
//        }else if(index == 2){
//            outputFieldsDeclarer.declare(new Fields("appId","message"));
//        }else if (index == 3){
//            outputFieldsDeclarer.declare(new Fields("orderId","triggerList"));
//        }else{
//            outputFieldsDeclarer.declare(new Fields("3","4"));
//        }
    }
}
