package oderandlogmonitor.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.google.gson.Gson;
import oderandlogmonitor.domain.LogMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ClickLogSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private TopologyContext context;
    private List<LogMessage> list ;
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.context = topologyContext;
        this.collector = spoutOutputCollector;
        list = new ArrayList();
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
    }

    @Override
    public void nextTuple() {
        Random random = new Random();
        LogMessage logMessage = list.get(random.nextInt(list.size()));
        collector.emit(new Values(new Gson().toJson(logMessage)));

        Utils.sleep(1000);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        outputFieldsDeclarer.declare(new Fields("click"));
    }
}
