import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class ComparatorClientePago here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class ComparatorClientePago implements Comparator<Cliente> {
    
    public int compare(Cliente c1,Cliente c2) {
        
        if(c1.getTotalPago() > c2.getTotalPago())
            return -1;
        if(c1.getTotalPago() < c2.getTotalPago())
            return 1;
        return 0;
        
    }
    
}