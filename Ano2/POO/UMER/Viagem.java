import java.util.GregorianCalendar;
import java.io.Serializable;

/**
 * Escreva a descricao da classe Viagem aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class Viagem implements Serializable {
    
    private Ponto2D origem;
    private Ponto2D destino;
    private String  motorista;
    private String  cliente;
    private double  duracao;
    private double  custo;
    private double  distancia;
    private GregorianCalendar data; 
    
    public Viagem() {
        
        this.origem    = new Ponto2D();
        this.destino   = new Ponto2D();
        this.motorista = "";
        this.cliente   = "";
        this.duracao   = 0;
        this.custo     = 0;
        this.distancia = 0;
        this.data      = new GregorianCalendar();
        
    }
    
    public Viagem(Ponto2D o, Ponto2D d, String m, String c, double duracao, double custo, double distancia, GregorianCalendar data) {
       
        this.origem    = o;
        this.destino   = d;
        this.motorista = m;
        this.cliente   = c;
        this.duracao   = duracao;
        this.custo     = custo;
        this.distancia = distancia;
        this.data      = data;
        
    }
    
    public Viagem(Viagem v) {
        
        this.origem    = v.getOrigem();
        this.destino   = v.getDestino();
        this.motorista = v.getMotorista();
        this.cliente   = v.getCliente();
        this.duracao   = v.getDuracao();
        this.custo     = v.getCusto();
        this.distancia = v.getDistancia();
        this.data      = v.getData();
        
    }
    
    public Ponto2D getOrigem() {
        
        return this.origem;
        
    }
    
    public Ponto2D getDestino() {
        
        return this.destino;
        
    }
    
    public String getMotorista() {
        
        return this.motorista;
        
    }
    
    public String getCliente() {
        
        return this.cliente;
        
    }
    
    public double getDuracao() {
        
        return this.duracao;
        
    }
    
    public double getCusto() {
        
        return this.custo;
        
    }
    
    public double getDistancia() {
        
        return this.distancia;
        
    }
    
    public GregorianCalendar getData() {
        
        return this.data;
        
    }
    
    public Viagem clone() {
        
        return new Viagem(this);  
        
    }
    
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("Origem: ");
       sb.append(this.origem.toString());
       sb.append("\n");
       sb.append("Destino: ");
       sb.append(this.destino.toString());
       sb.append("\n");
       sb.append("Motorista: ");
       sb.append(this.motorista.toString());
       sb.append("\n");
       sb.append("Cliente: ");
       sb.append(this.cliente.toString());
       sb.append("\n");
       sb.append("Data: ");
       sb.append(this.data.toString());
       sb.append("\n");
       sb.append("Duracao: ");
       sb.append(this.duracao);
       sb.append("\n");
       sb.append("Custo: ");
       sb.append(this.custo);
       sb.append("\n");
       return sb.toString();
    }
    
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Viagem v = (Viagem)o;
        return (v.getOrigem().equals(this.origem) && v.getDestino().equals(this.destino) && v.getMotorista().equals(this.motorista) && v.getCliente().equals(this.cliente) && v.getData().equals(this.data) && v.getDuracao() == this.duracao && v.getCusto() == this.custo);
    }
   
}
