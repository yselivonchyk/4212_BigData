package de.fhg.iais.kd.livlab.storm.examples.bolts;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrintBolt extends BaseBasicBolt {

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// Nothing to declare, duty free..
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		System.out.println(input.toString());
	}
}
