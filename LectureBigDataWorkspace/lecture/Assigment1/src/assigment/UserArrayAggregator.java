package assigment;

import java.util.ArrayList;
import java.util.Arrays;

import cascading.flow.FlowProcess;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class UserArrayAggregator extends
		BaseOperation<UserArrayAggregator.Context> implements
		Aggregator<UserArrayAggregator.Context> {

	private static final long serialVersionUID = 1L;

	public class Context {
		public ArrayList<Integer> users;
	}

	public UserArrayAggregator(Fields fields) {
		super(1, fields); // one input field, output fields named as in
							// constructor call
	}

	@Override
	public void start(@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		Context context = new Context();
		Integer[] seed = new Integer[1000];
		Arrays.fill(seed, 0);
		context.users = new ArrayList<Integer>(Arrays.asList(seed));

		aggregatorCall.setContext(context);
	}

	@Override
	public void aggregate(
			@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {

		TupleEntry arguments = aggregatorCall.getArguments();
		Context context = aggregatorCall.getContext();
		String username = arguments.getString("uid");
		int userId = Integer
				.parseInt(username.substring(username.length() - 4)) - 1;
		if (userId > 999 || userId < 0) {
			userId = 2;
		}
		context.users.set(userId, 1);
	}

	@Override
	public void complete(@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		Context context = aggregatorCall.getContext();
		Tuple result = new Tuple();
		result.add(context.users.toString());
		aggregatorCall.getOutputCollector().add(result);
	}
}
