package de.fraunhofer.iais.kd.livlab.bda.storm;

public class CountDistinctContainer {
	private final CountDistinctSketch sketch;
	private Boolean increased;
	private String artname;

	public CountDistinctContainer(String artname) {
		sketch = new CountDistinctSketch(50);
		artname = artname;
	}

	// add a user to the CountDistinctSketch
	public void addUser(String username) {
		int initEstimate = this.sketch.getEstimate();
		this.sketch.addUser(username);
		increased = this.sketch.getEstimate() == initEstimate;
	}

	// get estimation of how many distinct users are added in the sketch
	public int getCount() {
		return this.sketch.getEstimate();
	}

	// Returns true iff last addUser was increasing the estimate
	// (may not be thread safe, don’t care about that)
	public Boolean isIncreased() {
		return increased;
	}

	// gets the contained sketch
	public CountDistinctSketch getSketch() {
		return this.sketch;
	}

	// gets the artname
	public String getArtname() {
		return this.artname;
	}
}
