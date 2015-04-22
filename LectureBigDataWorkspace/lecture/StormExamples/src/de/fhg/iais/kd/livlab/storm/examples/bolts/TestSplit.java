package de.fhg.iais.kd.livlab.storm.examples.bolts;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TestSplit extends BaseBasicBolt {
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		for (String word : tuple.getString(0).split(" ")) {
			if (word.length() > 0) {
				collector.emit(new Values(word));
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
