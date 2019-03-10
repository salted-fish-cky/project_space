package oderandlogmonitor.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import oderandlogmonitor.constant.LogTypeConstant;
import oderandlogmonitor.domain.LogMessage;
import oderandlogmonitor.domain.Message;
import oderandlogmonitor.domain.PaymentInfo;
import oderandlogmonitor.domain.Record;
import oderandlogmonitor.utils.LogAnalyzeHandler;
import oderandlogmonitor.utils.MonitorHandler;
import oderandlogmonitor.utils.OrderMonitorHandler;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;
import java.util.Map;

public class Save2dbBolt extends BaseRichBolt {
    OutputCollector collector;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {



        String word = tuple.getFields().get(0);
        if(word.equals("paymentInfo")){
            System.out.println("444444444444444444444444444444444");
            OrderMonitorHandler.savePaymentInfo((PaymentInfo) tuple.getValueByField("paymentInfo"));

        }else if(word.equals("type")){
            String type = (String) tuple.getValueByField("type");
            System.out.println(type + "------------------------");
            if(type.equals(LogTypeConstant.TYPE1)) {
                System.out.println("1111111111111111111111111111111");
                LogMessage msg = (LogMessage) tuple.getValueByField("arg1");
                LogAnalyzeHandler.process(msg);
            }else if(type.equals(LogTypeConstant.TYPE2)){
                System.out.println("2222222222222222222222222222222");
                Message message = (Message) tuple.getValueByField("arg2");
                String appId = tuple.getStringByField("arg1");
                //将触发规则的信息进行通知
                MonitorHandler.notifly(appId, message);
                Record record = new Record();
                try {
                    BeanUtils.copyProperties(record, message);
                    collector.emit(new Values(record));
                    MonitorHandler.save(record);
                } catch (Exception e) {

                }
            }else if(type.equals(LogTypeConstant.TYPE3)) {
                System.out.println("333333333333333333333333333333333");
                OrderMonitorHandler.saveTrigger(tuple.getStringByField("arg1"), (List<String>) tuple.getValueByField("arg2"));

            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
