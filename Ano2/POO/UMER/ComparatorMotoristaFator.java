import java.util.Comparator;

/**
 * Write a description of class ComparatorMotoristaFator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class ComparatorMotoristaFator implements Comparator<Motorista> {
    
    public int compare(Motorista m1,Motorista m2) {
        
        if(m1.getFator() < m2.getFator()){
            return -1;
        }
        if(m1.getFator() > m2.getFator()){
            return 1;
        }
        return 0;
        
    }
    
}
