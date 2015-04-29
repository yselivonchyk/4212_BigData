import java.util.HashSet;

//This is the ARTIST
public class UserSet {

	public HashSet<String> listeners = new HashSet<String>();
	public String name; // artistname
	public int id; // artistid

	public void add(String username) {
		listeners.add(username);
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
}
