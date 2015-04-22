// Original example code from http://docs.cascading.org/cascading/2.2/userguide/html/ch02.html
/**
 * invokde with arguments 
 * src/resources/test.txt /tmp/wordcount
 *
 */
package de.fhg.iais.kd.hadoop.examples;


/**
 * invokde with arguments 
 * src/resources/test.txt /tmp/wordcount
 *
 */
import java.util.Properties;

import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.Aggregator;
import cascading.operation.Function;
import cascading.operation.aggregator.Count;
import cascading.operation.regex.RegexGenerator;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.property.AppProps;
import cascading.scheme.Scheme;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class CopyOfCascadingWordCount {

	public static void main(String[] args) {
		String inputPath = args[0];
		String outputPath = args[1];

		// define source and sink Taps.
		Scheme sourceScheme = new TextLine(new Fields("line"));
		Tap source = new Hfs(sourceScheme, inputPath);

		Scheme sinkScheme = new TextLine(new Fields("word", "count"));
		Tap sink = new Hfs(sinkScheme, outputPath, SinkMode.REPLACE);

		// the 'head' of the pipe assembly
		Pipe assembly = new Pipe("wordcount");

		// For each input Tuple
		// parse out each word into a new Tuple with the field name "word"
		// regular expressions are optional in Cascading
		String regex = "(?<!\\pL)(?=\\pL)[^ ]*(?<=\\pL)(?!\\pL)";
		Function function = new RegexGenerator(new Fields("word"), regex);
		assembly = new Each(assembly, new Fields("line"), function);

		// group the Tuple stream by the "word" value
		assembly = new GroupBy(assembly, new Fields("word"));

		// For every Tuple group
		// count the number of occurrences of "word" and store result in
		// a field named "count"
		Aggregator count = new MyCount(new Fields("count"));
		assembly = new Every(assembly, count);

		// initialize app properties, tell Hadoop which jar file to use
		Properties properties = new Properties();
		AppProps.setApplicationJarClass(properties, CopyOfCascadingWordCount.class);

		// plan a new Flow from the assembly using the source and sink Taps
		// with the above properties
		FlowConnector flowConnector = new HadoopFlowConnector(properties);
		Flow flow = flowConnector.connect("word-count", source, sink, assembly);

		// execute the flow, block until complete
//		flow.writeDOT("/tmp/debugging/plan.dot");
		flow.complete();
		
	}

}
