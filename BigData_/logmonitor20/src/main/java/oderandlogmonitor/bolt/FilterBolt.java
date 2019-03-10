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
import oderandlogmonitor.domain.PaymentInfo;
import oderandlogmonitor.utils.LogAnalyzeHandler;
import oderandlogmonitor.utils.MonitorHandler;
import oderandlogmonitor.utils.OrderMonitorHandler;

import java.util.List;
import java.util.Map;

public class FilterBolt extends BaseRichBolt {
    OutputCollector collector;




    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {

        String word = tuple.getFields().get(0);
        if(word.equals("click")){
            String line = (String) tuple.getValueByField("click");
            LogMessage logMessage = LogAnalyzeHandler.parser(line);
            if (logMessage == null || !LogAnalyzeHandler.isValidType(logMessage.getType())) {
                return;
            }

            collector.emit(new Values(LogTypeConstant.TYPE1,logMessage,logMessage.getType()));
            //定时更新规则信息
            LogAnalyzeHandler.scheduleLoad();
        }else if(word.equals("line")){

            String line = (String) tuple.getValueByField("line");
            oderandlogmonitor.domain.Message message = MonitorHandler.parser(line);
            if (message == null) {
                return;
            }
            if (MonitorHandler.trigger(message)) {

                collector.emit(new Values(LogTypeConstant.TYPE2,message.getAppId(), message));
            }
            //定时更新规则信息
            MonitorHandler.scheduleLoad();
        }else if(word.equals("paymentInfo")){
            PaymentInfo paymentInfo = (PaymentInfo) tuple.getValueByField("paymentInfo");
            if (paymentInfo == null) {
                return;
            }
            List<String> triggerList = OrderMonitorHandler.match(paymentInfo);
//        List<String> triggerList = new ArrayList<>();
            triggerList.add("12");
            triggerList.add("13");
            if (triggerList.size() > 0) {

                collector.emit(new Values(LogTypeConstant.TYPE3,paymentInfo.getOrderId(), triggerList));
            }
        }else{

            collector.emit(new Values(LogTypeConstant.TYPE4,"null","null"));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        outputFieldsDeclarer.declare(new Fields("type","arg1","arg2"));
//        if(index == 1){
//            outputFieldsDeclarer.declare(new Fields("logMessage","type"));
//        }else if(index == 2){
//            outputFieldsDeclarer.declare(new Fields("appId","message"));
//        }else if(index == 3){
//            outputFieldsDeclarer.declare(new Fields("orderId","triggerList"));
//        }else{
//            outputFieldsDeclarer.declare(new Fields("1","2"));
//        }
    }
}
