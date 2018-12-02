import java.util.*;
import java.io.Serializable;

/**
 * Escreva a descricao da classe Motorista aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class Motorista extends Ator implements Serializable {
    
    private int     fator;
    private int     classificacao;
    private double  kmsTotal;
    private boolean disponibilidade;
    private List<Viagem> historico;
    private int          idTaxi;
    private Viagem       viagem;
    private int          totalViagens;
    
    public Motorista() {
        
        super();
        this.fator           = 0;
        this.classificacao   = 0;
        this.kmsTotal        = 0;
        this.disponibilidade = false;
        this.historico       = new ArrayList<Viagem>();
        this.idTaxi          = 0;
        this.viagem          = new Viagem();
        this.totalViagens    = 0;
        
    }
    
    public Motorista(String e, String n, String p, String m, GregorianCalendar d) {
        
        super(e,n,p,m,d);
        this.fator           = 0;
        this.classificacao   = 0;
        this.kmsTotal        = 0;
        this.disponibilidade = false;
        this.historico       = new ArrayList<Viagem>();
        this.idTaxi          = 0;
        this.viagem          = new Viagem();
        this.totalViagens    = 0;
        
    }
    
    public Motorista(String e, String n, String p, String m, GregorianCalendar d, int f, int cla, double kms, boolean disp, List<Viagem> historico, int idTaxi, Viagem viagem, int total) {
       
        super(e,n,p,m,d);
        this.fator           = f;
        this.classificacao   = cla;
        this.kmsTotal        = kms;
        this.disponibilidade = disp;
        this.historico       = new ArrayList<Viagem>();
        for(Viagem v: historico)
            this.historico.add(v.clone());
        this.idTaxi          = idTaxi;
        this.viagem          = new Viagem(viagem);
        this.totalViagens    = total;
            
    }
    
    public Motorista(Motorista m) {
        
        super(m);
        this.fator           = m.getFator();
        this.classificacao   = m.getClassificacao();
        this.kmsTotal        = m.getKms();
        this.disponibilidade = m.getDisponibilidade();
        this.historico       = m.getHistorico();
        this.idTaxi          = m.getIDTaxi();
        this.viagem          = m.getViagem();
        this.totalViagens    = m.getTotalViagens();
        
    }
    
    public int getFator() {
        
        return this.fator;
        
    }
    
    public int getClassificacao() {
        
        return this.classificacao;
    
    }
    
    public double getKms() {
        
        return this.kmsTotal;
        
    }
    
    public boolean getDisponibilidade() {
        
        return this.disponibilidade;
        
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
    
    public int getIDTaxi() {
        
        return this.idTaxi;
        
    }
    
    public Viagem getViagem() {
        
        return this.viagem;
        
    }
    
    public int getTotalViagens() {
        
        return this.totalViagens;
        
    }
    
    public void setFator(int fator){
    
        this.fator = fator;
        
    }
    
    public void setClassificacao(int classificacao){
    
        this.classificacao = classificacao;
        
    }
    
    public void setKms(double kmsTotal){
        
        this.kmsTotal = kmsTotal;
        
    }
    
    public void setDisponibilidade(boolean disponibilidade){
       
        this.disponibilidade = disponibilidade;
        
    }
    
    // setHistorico;
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
    
    public void setIDTaxi(int idTaxi){
        
        this.idTaxi = idTaxi;
        
    }
    
    public void setViagem(Viagem v){
        
        this.viagem = v.clone();
        
    }
    
    public void setTotalViagens(int t) {
        
        this.totalViagens = t;
        
    }
    
    public Motorista clone(){
        return new Motorista(this);
    }
    
    public boolean equals(Object o){
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Motorista m = (Motorista)o;
        return (super.equals(m) && m.getFator() == this.fator  &&
                m.getClassificacao()   == this.classificacao   && 
                m.getKms()             == this.kmsTotal        && 
                m.getDisponibilidade() == this.disponibilidade && 
                m.getHistorico().equals(this.historico)        &&
                m.getIDTaxi()          == this.idTaxi          &&
                m.getViagem()          == this.viagem          &&
                m.getTotalViagens()    == this.totalViagens);
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("\tFATOR:           ");
        sb.append(this.fator);
        sb.append("\n");
        sb.append("\tCLASSIFICACAO:   ");
        sb.append(this.classificacao);
        sb.append("\n");
        sb.append("\tKMS TOTAIS:      ");
        sb.append(this.kmsTotal);
        sb.append("\n");
        sb.append("\tDISPONIBILIDADE: ");
        sb.append(this.disponibilidade);
        sb.append("\n");
        return sb.toString();
    }
    
}
