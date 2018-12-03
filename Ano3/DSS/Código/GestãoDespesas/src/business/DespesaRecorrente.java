package business;

import java.util.Date;
public class DespesaRecorrente extends Despesa
{
    private String tipo;
    
    public DespesaRecorrente(int montante,String data, String estado, String tipo)
    {
       super(montante,data, estado);
       this.tipo = tipo;
    }
    public DespesaRecorrente(Despesa d,String tipo)
    {
       super(d);
       this.tipo = tipo;
    }
    public DespesaRecorrente(DespesaRecorrente dR)
    {
       super(dR.getMontante(),dR.getData(), dR.getEstado());
       this.tipo = dR.getTipo();
    }
    
    public String getTipo(){
        return tipo;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(super.toString()).append("                              TIPO: " + tipo +"\n");
        return s.toString();
    }
    public DespesaRecorrente clone(){
        return new DespesaRecorrente(this);
    }
    public boolean equals(Object o){
        if(this == o)
            return true;
        if((o == null) || (this.getClass()!=o.getClass()))
            return false;
            
        boolean encontrado = false;
        DespesaRecorrente d = (DespesaRecorrente) o;
        if(d.getTipo()!=tipo)
            return false;
        return super.equals(o);
    }
}
