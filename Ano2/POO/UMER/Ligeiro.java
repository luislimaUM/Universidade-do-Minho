import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Escreva a descricao da classe Ligeiro aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class Ligeiro extends Taxi implements java.io.Serializable {
    
    public Ligeiro() {
        
        super();
        
    }

    public Ligeiro(int id, double v, double pKm, double pHora, Ponto2D c, List<Viagem> h, boolean tF, List<Viagem> f) {

        super(id,v,pKm,pHora,c,h,tF,f);
        
    }
    
    public Ligeiro(Ligeiro l) {
        
        super(l);
        
    }
        
}
