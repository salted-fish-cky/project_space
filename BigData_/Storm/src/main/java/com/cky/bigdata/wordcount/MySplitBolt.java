package com.cky.bigdata.wordcount;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

public class MySplitBolt extends BaseRichBolt {

    OutputCollector outputCollector;
    //初始化方法
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    //被storm框架while（true）循环调用，  出入参数tuple
    public void execute(Tuple tuple) {

        String line = tuple.getString(0);

        String[] arrWrods = line.split(" ");

        for (String word: arrWrods){

            outputCollector.emit(new Values(word,1));
        }

        System.out.println("split____________________");

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        outputFieldsDeclarer.declare(new Fields("word","num"));
    }
}
