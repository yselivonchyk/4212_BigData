package assigment;

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
import de.fraunhofer.iais.kd.livlab.bda.clustermodel.ClusterModel;
import de.fraunhofer.iais.kd.livlab.bda.clustermodel.ClusterModelFactory;

public class UserSetCluster {
	private static final String DELIMITER = ",";

	public static void main(String[] args) {
		String infile = getStringFromArgs(args, 0,
				"src/resources/sorted_sample.tsv");
		String workDir = getStringFromArgs(args, 1, "recommender_flowtest/");
		String clusterfile = getStringFromArgs(args, 2, "recommender_flowtest/");

		Flow utilMatrix = getFlow(infile, workDir, clusterfile);
		utilMatrix.complete();
	}

	public static String getStringFromArgs(String[] args, int position,
			String defaultResult) {
		if (args.length < position) {
			return defaultResult;
		}
		return args[position];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Flow getFlow(String infile, String outfile, String clusterfile) {
		Aggregator collect = new UserArrayAggregator(new Fields("uid"));
		return getFlow(infile, outfile, clusterfile, collect);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Flow getFlow(String infile, String outfile,
			String clusterfile, Aggregator collect) {

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
		Fields targetFields = new Fields("uid", "artist_name");
		ProjectToFields projector = new ProjectToFields(targetFields);
		pipe = new Each(pipe, targetFields, projector);

		// select only unique user<->artist preferences
		pipe = new Unique(pipe, new Fields("uid", "artist_name"));
		// group by artist
		pipe = new GroupBy(pipe, new Fields("artist_name"));
		pipe = new Every(pipe, collect, new Fields("users"));

		// Convert user array into cluster data
		ClusterModel model = ClusterModelFactory
				.readFromCsvResource(clusterfile);
		UserSetClusterModel clusterModel = new UserSetClusterModel(model);
		ProjectToCluster clusterProjection = new ProjectToCluster(new Fields(
				"users"), new Fields("cluster"), clusterModel);
		pipe = new Each(pipe, targetFields, clusterProjection);

		Properties properties = new Properties();
		AppProps.setApplicationJarClass(properties, UserSetMatrixFlow.class);

		FlowConnector flowConnector = new HadoopFlowConnector(properties);
		// FlowConnector flowConnector = new HadoopFlowConnector();

		Map<String, Tap> endPoints = new HashMap<>();
		endPoints.put("listenEvts", matrix);

		Flow flow = flowConnector.connect("uitlityMatrix", source, endPoints,
				pipe);
		return flow;
	}
}
