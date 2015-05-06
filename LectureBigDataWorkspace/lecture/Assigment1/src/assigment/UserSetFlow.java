package assigment;

import cascading.flow.Flow;
import cascading.operation.Aggregator;
import cascading.tuple.Fields;

public class UserSetFlow extends UserFlowBase {
	// Start up method
	public static void main(String[] args) {
		String infile = "src/resources/sorted_sample.tsv";
		String workDir = "recommender_flowtest/";

		Flow utilMatrix = UserSetFlow.getFlow(infile, workDir);
		utilMatrix.complete();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Flow getFlow(String infile, String outfile) {
		Aggregator collect = new UserListAggregator(new Fields("uid"));
		return UserFlowBase.getFlow(infile, outfile, collect, "userFlow");
	}
}
