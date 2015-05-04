package assigment;

import java.util.Arrays;
import java.util.Set;

import de.fraunhofer.iais.kd.livlab.bda.clustermodel.ClusterModel;

public class UserSetClusterModel {
	private final ClusterModel clusterModel;

	public UserSetClusterModel(ClusterModel clusterModel) {
		this.clusterModel = clusterModel;
	}

	// Returns the clusterid of the cluster whos metroid i closest to the input
	public String findClosestCluster(Integer[] userset) {
		Set<String> keys = clusterModel.getKeys();

		double bestDistance = 1;
		String bestKey = "";

		for (String key : keys) {
			String userSetString = clusterModel.get(key);
			String[] usersString = userSetString.split(",");

			Integer[] users = new Integer[usersString.length];
			Arrays.fill(users, 0);

			for (int i = 0; i < users.length; i++) {
				users[i] = usersString[i].equalsIgnoreCase("1") ? 1 : 0;
			}
			double distance = UserSet.distanceTo(users, userset);

			if (distance <= bestDistance) {
				bestDistance = distance;
				bestKey = key;
			}
		}
		return bestKey;
	}
}
