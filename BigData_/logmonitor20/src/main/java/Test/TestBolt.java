package Test;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.Map;

public class TestBolt extends BaseRichBolt{
    OutputCollector collector;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
//        String line1 = (String) tuple.getValueByField("word1");
//
//        String line2 = (String) tuple.getValueByField("word2");
        String word = tuple.getFields().get(0);
        String line = null;
        if(word.equals("word1")){
            line = (String) tuple.getValueByField("word1");
        }else if(word.equals("word2")){
            line = (String) tuple.getValueByField("word2");
        }
        System.out.println(line);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
