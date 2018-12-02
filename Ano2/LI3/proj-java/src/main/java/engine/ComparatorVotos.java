package engine;
import java.util.Comparator;

class ComparatorVotos implements Comparator<Post> {
	public int compare(Post p1, Post p2){
		if(p1.getScore() > p2.getScore()){
			return -1;
		}
		else {
			if(p1.getScore() < p2.getScore()){
				return 1;
			}else return 0;
		}
	}
}