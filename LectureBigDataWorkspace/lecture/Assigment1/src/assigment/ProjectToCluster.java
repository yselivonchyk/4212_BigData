package assigment;

import cascading.flow.FlowProcess;
import cascading.operation.BaseOperation;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

@SuppressWarnings("rawtypes")
public class ProjectToCluster extends BaseOperation implements Function {

	private static final long serialVersionUID = 1L;
	private final Fields fields;
	private final UserSetClusterModel distance;

	public ProjectToCluster(Fields in, Fields out, UserSetClusterModel distance) {
		super(1, in);
		this.fields = out;
		this.distance = distance;
	}

	@Override
	public void operate(FlowProcess flowProcess, FunctionCall functionCall) {

		TupleEntry arguments = functionCall.getArguments();

		// create a Tuple to hold our result values
		Tuple result = new Tuple();

		Object object;
		for (int i = 0; i < this.fields.size(); i++) {
			Comparable name = fields.get(i);
			object = arguments.getObject(name);
			String cluster = distance.findClosestCluster((Integer[]) object);
			result.add(cluster);
		}

		// return the result Tuple
		functionCall.getOutputCollector().add(result);
	}
}
