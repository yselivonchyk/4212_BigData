package de.fraunhofer.iais.kd.livlab.bda.storm;

import de.fraunhofer.iais.kd.livlab.bda.countdistinct.Sketch;

public class CountDistinctSketch extends Sketch {

	public CountDistinctSketch(int sketchsize) {
		super(sketchsize);
		// TODO Auto-generated constructor stub
	}

	// add a user to the CountDistinctStetch
	public void addUser(String username) {
		addUser(getUserID(username));
	}

	public void addUser(int id) {
		this.setBit(id % this.getSketchsize());
	}

	private int getUserID(String user) {
		return Integer.parseInt(user.substring(user.length() - 4)) - 1;
	}

	// get estimation of how many distinct users are added in the sketch
	public int getEstimate() {
		return estimateCount(this.getSketchsize(), this.cardinality());
	}

	private static int estimateCount(int size, Sketch sketch) {
		return estimateCount(size, sketch.cardinality());
	}

	private static int estimateCount(int size, int filled) {
		return (int) Math.round(-1.0 * size * Math.log((size - filled) / size)
				/ Math.log(Math.E));
	}

	// compute Jaccard-Distance to another CountDistinctSketch
	public double distanceTo(CountDistinctSketch other) {
		Sketch union = this.orSketch(other);
		int unionSize = estimateCount(this.getSketchsize(), union);
		int intersectionSize = this.getEstimate() + other.getEstimate()
				- unionSize;

		return 1 - intersectionSize / unionSize;
	}

}
