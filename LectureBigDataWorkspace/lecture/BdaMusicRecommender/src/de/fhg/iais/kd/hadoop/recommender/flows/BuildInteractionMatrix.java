package de.fhg.iais.kd.hadoop.recommender.flows;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.Aggregator;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.pipe.assembly.Unique;
import cascading.property.AppProps;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;
import de.fhg.iais.kd.hadoop.recommender.functions.CollectUsers;
import de.fhg.iais.kd.hadoop.recommender.functions.ProjectToFields;

public class BuildInteractionMatrix {

	private static final String DELIMITER = "|";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Flow getInteractionMatrixFlow(String infile, String outfile) {

		String inputPath = infile;
		String outputPath = outfile;

		// define source and sink Taps.
		Fields sourceFields = new Fields("uid", "datetime", "artist_mbid",
				"artist_name", "track_mbid", "track_name");
		Scheme sourceScheme = new TextDelimited(sourceFields);
		Tap source = new Hfs(sourceScheme, inputPath);

		Scheme outputSehema = new TextDelimited(false, DELIMITER);

		Tap matrix = new Hfs(outputSehema, outputPath + "/matrix",
				SinkMode.REPLACE);

		Pipe pipe = new Pipe("listenEvts");

		// Filter out empty mbids
		Fields targetFields = new Fields("uid", "artist_mbid");
		ProjectToFields projector = new ProjectToFields(targetFields);
		pipe = new Each(pipe, targetFields, projector);

		// select only unique user<->artist preferences
		pipe = new Unique(pipe, new Fields("uid", "artist_mbid"));
		// group by artist
		pipe = new GroupBy(pipe, new Fields("artist_mbid"));

		Aggregator collect = new CollectUsers(new Fields("uid"));
		pipe = new Every(pipe, collect);

		Properties properties = new Properties();
		AppProps.setApplicationJarClass(properties,
				BuildInteractionMatrix.class);

		FlowConnector flowConnector = new HadoopFlowConnector(properties);
		// FlowConnector flowConnector = new HadoopFlowConnector();

		Map<String, Tap> endPoints = new HashMap<>();
		endPoints.put("listenEvts", matrix);

		Flow flow = flowConnector.connect("uitlityMatrix", source, endPoints,
				pipe);
		return flow;

	}
}
