import java.util.*;
import java.io.Serializable;

/**
 * Escreva a descricao da classe Cliente aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class Cliente extends Ator implements Serializable {
    
    private List<Viagem> historico;
    
    public Cliente() {
        
        super();
        this.historico   = new ArrayList<Viagem>();
    
    }
    
    public Cliente(String e, String n, String p, String m, GregorianCalendar d) {
        
        super(e,n,p,m,d);
        this.historico   = new ArrayList<Viagem>();
        
    }
    
    public Cliente(Cliente c) {
        
        super(c);
        this.historico   = c.getHistorico();
        
    }
    
    public List<Viagem> getHistorico() {
        
        ArrayList<Viagem> aux = new ArrayList<Viagem>(this.historico.size());
        if (this.historico!=null) {
            for(Viagem v: this.historico) {
                if (v!=null)
                    aux.add(v.clone());
            }
        }
        return aux;
        
    }
    
    public double getTotalPago() {
        
        double total  = 0;
        
        for(Viagem v: historico){
            total += v.getCusto();
        }
        return total;
        
    }
    
    public void setHistorico(List<Viagem> historico) {
        
        ArrayList<Viagem> aux = new ArrayList<Viagem>(historico.size());
        if (historico!=null) {
            for(Viagem v: historico) {
                if (v!=null)
                    aux.add(v.clone());
            }
        }
        this.historico = aux;
        
    }
    
    public Viagem getViagem() {
        
        Viagem viagem = new Viagem();
        
        for(Viagem v: this.historico) 
            viagem = v.clone();
        return viagem;
        
    }
    
    public Cliente clone(){
        return new Cliente(this);
    }
    
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Cliente c = (Cliente)o;
        return (super.equals(c) && c.getHistorico().equals(this.historico));
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        return sb.toString();
    }
    
}
