import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Escreva a descricao da classe Moto aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class Moto extends Taxi implements java.io.Serializable {
    
    public Moto() {
        
        super();
        
    }

    public Moto(int id, double v, double pKm, double pHora, Ponto2D c, List<Viagem> h, boolean tF, List<Viagem> f) {

        super(id,v,pKm,pHora,c,h,tF,f);
        
    }
    
    public Moto(Moto m) {
        
        super(m);
        
    }
    
}
