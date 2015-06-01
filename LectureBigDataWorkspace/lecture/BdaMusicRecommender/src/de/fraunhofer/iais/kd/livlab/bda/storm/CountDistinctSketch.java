package de.fraunhofer.iais.kd.livlab.bda.storm;

import de.fraunhofer.iais.kd.livlab.bda.countdistinct.Sketch;

public class CountDistinctSketch extends Sketch {

	public CountDistinctSketch(int sketchsize) {
		super(sketchsize);
		// TODO Auto-generated constructor stub
	}

	// add a user to the CountDistinctStetch
	public void addUser(String username) {
		this.setBit(getUserID(username));
	}

	private int getUserID(String user) {
		return Integer.parseInt(user.substring(user.length() - 4)) - 1;
	}

	// get estimation of how many distinct users are added in the sketch
	public int getEstimate() {
		int coord = this.cardinality();
		int size = this.getSketchsize();
		return (int) Math.round(-1.0 * size * Math.log((size - coord) / size)
				/ Math.log(Math.E));
	}

	// compute Jaccard-Distance to another CountDistinctSketch
	public double distanceTo(CountDistinctSketch other) {
		return 0;
	}

}
