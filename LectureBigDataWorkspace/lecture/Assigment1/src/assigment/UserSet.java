package assigment;

import java.util.Arrays;
import java.util.HashSet;

//This is the ARTIST
public class UserSet {

	public HashSet<String> listeners = new HashSet<String>();
	public String name; // artistname
	public int id; // artistid

	public void add(String username) {
		listeners.add(username);
	}

	public Integer[] getUserArray() {
		Integer[] result = new Integer[1000];
		Arrays.fill(result, 0);
		for (String userName : listeners) {
			int userId = Integer
					.parseInt(userName.substring(userName.length() - 4)) - 1;
			if (userId > 999 || userId < 0) {
				userId = 2;
			}
			result[userId] = 1;
		}
		return result;
	}

	public double distanceTo(UserSet otherArtistsListeners) {
		if (!(otherArtistsListeners.listeners.isEmpty() && this.listeners
				.isEmpty())) {
			HashSet<String> union = otherArtistsListeners.listeners;
			HashSet<String> intersection = otherArtistsListeners.listeners;
			union.addAll(this.listeners);
			intersection.retainAll(this.listeners);
			double similarity = (double) intersection.size() / union.size();
			return 1 - similarity;
		}
		return 0;
	}

	public double distanceTo(Integer[] right) {
		return distanceTo(this.getUserArray(), right);
	}

	public static double distanceTo(Integer[] left, Integer[] right) {
		int union = 0;
		int unique = 0;

		for (int i = 0; i < right.length; i++) {
			if (left[i] != 0 || right[i] != 0) {
				if (left[i] == right[i]) {
					union++;
				} else {
					unique++;
				}
			}
		}
		if (union == 0 && unique == 0) {
			return 0;
		}

		double similarity = (double) union / (union + unique);
		return 1 - similarity;
	}
}
