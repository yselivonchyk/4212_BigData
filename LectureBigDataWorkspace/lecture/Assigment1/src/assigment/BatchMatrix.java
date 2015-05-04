package assigment;

import cascading.flow.Flow;

public class BatchMatrix {
	public static void main(String[] args) {
		String infile = "src/resources/sorted_sample.tsv";
		String workDir = "recommender_flowtest/";

		Flow utilMatrix = BatchMatrixBuilder.getInteractionMatrixFlow(infile,
				workDir);
		utilMatrix.complete();
	}
}
