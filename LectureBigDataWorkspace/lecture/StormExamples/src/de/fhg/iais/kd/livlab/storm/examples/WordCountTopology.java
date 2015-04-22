package de.fhg.iais.kd.livlab.storm.examples;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.testing.TestWordCounter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import de.fhg.iais.kd.livlab.storm.examples.bolts.PrintBolt;
import de.fhg.iais.kd.livlab.storm.examples.bolts.TestSplit;
import de.fhg.iais.kd.livlab.storm.examples.spouts.TestSentenceSpout;

public class WordCountTopology {

	public static void main(String[] args) throws InterruptedException {

		TopologyBuilder builder = new TopologyBuilder();

		// Create some sentences
		builder.setSpout("spout", new TestSentenceSpout(), 1);
		// Split on blanks
		builder.setBolt("split", new TestSplit(), 1).shuffleGrouping("spout");
		// Count words, already available in storm's testing package
		builder.setBolt("count", new TestWordCounter(), 1).fieldsGrouping(
				"split", new Fields("word"));
		builder.setBolt("printer", new PrintBolt(), 1).shuffleGrouping("count");

		// Create a local cluster
		LocalCluster cluster = new LocalCluster();

		// Configure it
		Config conf = new Config();
		conf.setDebug(false);

		// submit the wordcount topology
		cluster.submitTopology("wordcount", conf, builder.createTopology());

		// Wait some time, then shutdown..
		Thread.sleep(3000);
		cluster.shutdown();
	}

}
