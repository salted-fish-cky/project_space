package com.cky.bigdata.wordcount;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

public class TestSpout extends BaseRichSpout {
    SpoutOutputCollector spoutOutputCollector;

    //初始化方法
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    //storm 框架在while(true)调用nextTuple方法
    public void nextTuple() {
        spoutOutputCollector.emit(new Values("i am lilei zzzz love hanmeimei"));
        System.out.println("发射——————————————————————————");

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));

    }
}
