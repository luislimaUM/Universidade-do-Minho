import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class Taxi implements Serializable{
    
    private int    idTaxi;
    private double velocidade;
    private double precoKm;
    private double precoHora;
    private Ponto2D coordenadas;
    private List<Viagem> historico;
    private boolean temFila;
    private List<Viagem> filaDeEspera;
    
    public Taxi() {
        
        this.idTaxi       = 0; 
        this.velocidade   = 0;
        this.precoKm      = 0;
        this.precoHora    = 0;
        this.coordenadas  = new Ponto2D();
        this.historico    = new ArrayList<Viagem>();
        this.temFila      = false;
        this.filaDeEspera = new ArrayList<Viagem>();
        
    }
    
    public Taxi(int id, double ve, double pKm, double pHora, Ponto2D c, List<Viagem> historico, boolean temFila, List<Viagem> filaDeEspera) {
        
        this.idTaxi      = id;
        this.velocidade  = ve;
        this.precoKm     = pKm;
        this.precoHora   = pHora;
        this.coordenadas = c;
        for(Viagem v: historico)
            this.historico.add(v.clone());
        this.temFila     = temFila;
        for(Viagem v: filaDeEspera)
            this.filaDeEspera.add(v.clone());
            
    }
    
    public Taxi(Taxi t) {
        
        this.idTaxi       = t.getIDTaxi();
        this.velocidade   = t.getVelocidade();
        this.precoKm      = t.getPrecoKm();
        this.precoHora    = t.getPrecoHora();
        this.coordenadas  = t.getCoordenadas();
        this.historico    = t.getHistorico();
        this.temFila      = t.getTemFila();
        this.filaDeEspera = t.getFilaDeEspera();
        
    }
    
    public int getIDTaxi(){
        
        return this.idTaxi;
        
    }
    
    public double getVelocidade() {
        
        return this.velocidade;
        
    }
    
    public double getPrecoKm() {
        
        return this.precoKm;
        
    }
    
    public double getPrecoHora() {
        
        return this.precoHora;
        
    }
    
    public Ponto2D getCoordenadas() {
        
        return this.coordenadas;
        
    }
    
    public List<Viagem> getHistorico() {

        ArrayList<Viagem> aux = new ArrayList<Viagem>();
        if (this.historico!=null) {
            for(Viagem v: this.historico) {
                if (v!=null)
                    aux.add(v.clone());
            }
        }
        return aux;
        
    }

    public boolean getTemFila() {
    
        return this.temFila;
        
    }
    
    public List<Viagem> getFilaDeEspera() {
        
        ArrayList<Viagem> aux = new ArrayList<Viagem>();
        if (this.filaDeEspera!=null) {
            for(Viagem v: this.filaDeEspera) {
                if (v!=null)
                    aux.add(v.clone());
            }
        }
        return aux;
        
    }
    
    public void setCoordenadas(Ponto2D p) {
        
        this.coordenadas = p.clone();
        
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
    
    public void setFilaDeEspera(List<Viagem> filaDeEspera) {
        
        ArrayList<Viagem> aux = new ArrayList<Viagem>(filaDeEspera.size());
        if (filaDeEspera!=null) {
            for(Viagem v: filaDeEspera) {
                if (v!=null)
                    aux.add(v.clone());
            }
        }
        this.filaDeEspera = aux;
        
    }
    
    public Taxi clone(){
        
        return new Taxi(this);
    
    }
    
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Taxi t = (Taxi)o;
        return (t.getIDTaxi()  == this.idTaxi  && t.getVelocidade() == this.velocidade
             && t.getPrecoKm() == this.precoKm && t.getPrecoHora()  == this.precoHora
             && t.getCoordenadas().equals(this.coordenadas) 
             && t.getHistorico().equals(this.historico)
             && t.getTemFila() == this.temFila
             && t.getFilaDeEspera().equals(this.filaDeEspera));
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ");
        sb.append(this.idTaxi);
        sb.append("\n");
        sb.append("Velocidade: ");
        sb.append(this.velocidade);
        sb.append("\n");
        sb.append("Preco por km: ");
        sb.append(this.precoKm);
        sb.append("\n");
        sb.append("Preco por hora: ");
        sb.append(this.precoHora);
        sb.append("\n");
        sb.append("Coordenadas: ");
        sb.append(this.coordenadas.toString());
        sb.append("\n");
        sb.append("Historico: ");
        sb.append(this.historico.toString());
        sb.append("\n");
        sb.append("Fila de Espera disponivel: ");
        sb.append(this.temFila);
        sb.append("\n");
        return sb.toString();
    }
    
}