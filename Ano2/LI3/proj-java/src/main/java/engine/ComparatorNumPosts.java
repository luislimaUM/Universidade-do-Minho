package engine;
import java.util.Comparator;

class ComparatorNumPosts implements Comparator<User> {
	public int compare (User u1, User u2){
		if(u1.getNumPosts() > u2.getNumPosts()){
			return 1;
		}
		if(u1.getNumPosts() < u2.getNumPosts()){
			return -1;
		}
		return 0;
	}
}