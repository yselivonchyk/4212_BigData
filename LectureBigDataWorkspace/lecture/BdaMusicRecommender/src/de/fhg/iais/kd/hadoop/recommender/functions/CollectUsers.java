package de.fhg.iais.kd.hadoop.recommender.functions;

import java.util.LinkedList;

import cascading.flow.FlowProcess;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class CollectUsers extends BaseOperation<CollectUsers.Context> implements
		Aggregator<CollectUsers.Context> {

	private static final long serialVersionUID = 1L;

	public class Context extends LinkedList<String> {
	}

	public CollectUsers(Fields fields) {
		super(1, fields); // one input field, output fields named as in
							// constructor call
	}

	@Override
	public void start(@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		Context context = new Context();
		aggregatorCall.setContext(context);
	}

	@Override
	public void aggregate(
			@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {

		TupleEntry arguments = aggregatorCall.getArguments();
		Context context = aggregatorCall.getContext();
		context.add(arguments.getString("uid"));
	}

	@Override
	public void complete(@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		Context context = aggregatorCall.getContext();
		Tuple result = new Tuple();
		result.add(context.toString());
		aggregatorCall.getOutputCollector().add(result);

	}
}
