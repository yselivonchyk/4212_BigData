package de.fhg.iais.kd.hadoop.examples;


import cascading.flow.FlowProcess;
import cascading.operation.Aggregator;
import cascading.operation.AggregatorCall;
import cascading.operation.BaseOperation;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.tuple.TupleEntry;

public class MyCount extends
		BaseOperation<MyCount.Context> implements
		Aggregator<MyCount.Context> {

	private static final long serialVersionUID = 1L;

	public class Context {
		int count;
	}

	public MyCount(Fields fields) {
		super(1, fields);  //one input field, output fields named as in constructor call
	}


	@Override
	public void start(@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		Context context = new Context();
		context.count = 0;
		aggregatorCall.setContext(context);
	}

	@Override
	public void aggregate(
			@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {

		TupleEntry arguments = aggregatorCall.getArguments();
		Context context = aggregatorCall.getContext();
		context.count++;

	}

	@Override
	public void complete(@SuppressWarnings("rawtypes") FlowProcess flowProcess,
			AggregatorCall<Context> aggregatorCall) {
		Context context = aggregatorCall.getContext();
		Tuple result = new Tuple();
		result.add(context.count);
		aggregatorCall.getOutputCollector().add(result);

	}
}

