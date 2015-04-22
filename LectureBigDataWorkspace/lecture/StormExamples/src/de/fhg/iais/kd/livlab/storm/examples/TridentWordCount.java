// Original source at https://github.com/nathanmarz/storm/wiki/Trident-tutorial
package de.fhg.iais.kd.livlab.storm.examples;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.FilterNull;
import storm.trident.operation.builtin.MapGet;
import storm.trident.operation.builtin.Sum;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.testing.MemoryMapState;
import storm.trident.testing.Split;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TridentWordCount {
	public static void main(String[] args) throws InterruptedException {
		// Create a source spout
		@SuppressWarnings("unchecked")
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("the cow jumped over the moon"), new Values(
						"the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values(
						"how many apples can you eat"));
		spout.setCycle(true);

		// Create a topology that stores current word counts
		TridentTopology topology = new TridentTopology();
		TridentState wordCounts = topology
				.newStream("spout1", spout)
				.each(new Fields("sentence"), new Split(), new Fields("word"))
				.groupBy(new Fields("word"))
				.persistentAggregate(new MemoryMapState.Factory(), new Count(),
						new Fields("count")).parallelismHint(2);

		// Create a local drpc service
		LocalDRPC ldrpc = new LocalDRPC();
		// Create a drpc to query current counts
		topology.newDRPCStream("words", ldrpc)
				.each(new Fields("args"), new Split(), new Fields("word"))
				.groupBy(new Fields("word"))
				.stateQuery(wordCounts, new Fields("word"), new MapGet(),
						new Fields("count"))
				.each(new Fields("count"), new FilterNull())
				.aggregate(new Fields("count"), new Sum(), new Fields("sum"));

		// Create a local cluster
		LocalCluster cluster = new LocalCluster();

		// Configure it
		Config conf = new Config();
		conf.setDebug(false);

		// submit the word count topology
		cluster.submitTopology("wordcount", conf, topology.build());

		// Query a couple of times for the sum of counts for all of the words we
		// are interested in
		for (int i = 0; i < 10; ++i) {
			Thread.sleep(1000);
			System.out.println("========"
					+ ldrpc.execute("words", "cat dog the man"));
		}

		// Wait some time, then shutdown..
		Thread.sleep(3000);
		cluster.shutdown();
		ldrpc.shutdown();

	}
}
