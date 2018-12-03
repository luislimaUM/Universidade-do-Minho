package business;

public class DespesaExtraordinaria extends Despesa
{
    private String descricao;
    
    public DespesaExtraordinaria(int montante,String data, String estado, String descricao)
    {
       super(montante,data, estado);
       this.descricao = descricao;
    }
    public DespesaExtraordinaria(Despesa d,String descricao)
    {
       super(d);
       this.descricao = descricao;
    }
    public DespesaExtraordinaria(DespesaExtraordinaria dEx)
    {
       super(dEx.getMontante(),dEx.getData(), dEx.getEstado());
       this.descricao = dEx.getDescricao();
    }
    
    public String getDescricao(){
        return descricao;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(super.toString()).append("                                   ")
                                  .append("TIPO: " + descricao +"\n");
        return s.toString();
    }
    public DespesaExtraordinaria clone(){
        return new DespesaExtraordinaria(this);
    }
    public boolean equals(Object o){
        if(this == o)
            return true;
        if((o == null) || (this.getClass()!=o.getClass()))
            return false;
            
        boolean encontrado = false;
        DespesaExtraordinaria d = (DespesaExtraordinaria) o;
        if(d.getDescricao()!=descricao)
            return false;
        return super.equals(o);
    }
}
