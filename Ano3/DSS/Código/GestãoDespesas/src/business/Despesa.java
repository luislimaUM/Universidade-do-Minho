package business;
import java.util.Date;

public abstract class Despesa
{
    private int montante;
    private String data;
    private String estado;
    
    public Despesa(Despesa d){
        montante = d.getMontante();
        data = d.getData();
        estado = d.getEstado();
    }
    
    public Despesa(int montante,String data, String estado){
        this.montante = montante;
        this.data = data;
        this.estado = estado;
    }
    
    public int getMontante(){return montante;}
    
    public String getData(){return data;}
    

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("MONTANTE: " + montante + "                              DATA: "+data+"                              ESTADO: "+estado);
        return s.toString();
    }
    
    public boolean equals(Object o){
        if(this == o)
            return true;
        if((o == null) || (this.getClass()!=o.getClass()))
            return false;
            
        boolean encontrado = false;
        Despesa d = (Despesa) o;
        if(d.getMontante()!=montante)
            return false;
        // if(d.getDate().getTime() != data.getTime())
           // return false;
        return true;
    }

    public String getEstado() {
        return estado;
    }
}
