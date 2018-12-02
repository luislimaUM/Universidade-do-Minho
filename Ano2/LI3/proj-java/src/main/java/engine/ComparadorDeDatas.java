package engine;
import java.util.Comparator;
import java.time.LocalDate;

class ComparadorDeDatas implements Comparator<LocalDate> {
	public int compare (LocalDate l1,LocalDate l2) {
		if(l1.isBefore(l2))
			return -1;
		else {
			if(l2.isBefore(l1))
				return 1;
			else return 0;
		}
	}
}