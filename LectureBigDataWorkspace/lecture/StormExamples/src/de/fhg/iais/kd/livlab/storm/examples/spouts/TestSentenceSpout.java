// COPY FROM TestWordSpout, replaced nextTuple()
package de.fhg.iais.kd.livlab.storm.examples.spouts;
import backtype.storm.Config;
import backtype.storm.topology.OutputFieldsDeclarer;
import java.util.Map;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import java.util.HashMap;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSentenceSpout extends BaseRichSpout {
	public static Logger LOG = LoggerFactory.getLogger(TestSentenceSpout.class);
	boolean _isDistributed;
	SpoutOutputCollector _collector;

	public TestSentenceSpout() {
		this(true);
	}

	public TestSentenceSpout(boolean isDistributed) {
		_isDistributed = isDistributed;
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
	}

	public void close() {

	}

	public void nextTuple() {
		Utils.sleep(100);
		final String[] sentences = new String[] {
				"The quick brown fox jumps over the lazy dog",
				"The quick brown fox jumps over the lazy dog" };
		final Random rand = new Random();
		final String sentence = sentences[rand.nextInt(sentences.length)];
		_collector.emit(new Values(sentence));
	}

	public void ack(Object msgId) {

	}

	public void fail(Object msgId) {

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		if (!_isDistributed) {
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
			return ret;
		} else {
			return null;
		}
	}
}