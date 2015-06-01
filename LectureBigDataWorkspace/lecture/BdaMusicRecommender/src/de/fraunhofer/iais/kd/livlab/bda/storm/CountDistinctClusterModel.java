package de.fraunhofer.iais.kd.livlab.bda.storm;

import java.util.Set;

import de.fraunhofer.iais.kd.livlab.bda.clustermodel.ClusterModel;

public class CountDistinctClusterModel {
	private final ClusterModel model;

	public CountDistinctClusterModel(ClusterModel model) {
		this.model = model;
	}

	public String getClosest(CountDistinctSketch sketch) {
		Set<String> keys = model.getKeys();

		double bestDistance = 1;
		String bestKey = "";

		for (String key : keys) {
			String userSetString = model.get(key);
			String[] usersString = userSetString.split(",");
			CountDistinctSketch candidate = new CountDistinctSketch(50);

			for (int i = 0; i < usersString.length; i++) {
				if (usersString[i].equalsIgnoreCase("1")) {
					candidate.addUser(i);
				}
			}

			double distance = candidate.distanceTo(sketch);

			if (distance <= bestDistance) {
				bestDistance = distance;
				bestKey = key;
			}
		}
		return bestKey;
	}
}
