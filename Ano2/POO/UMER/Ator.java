import java.util.GregorianCalendar;
import java.io.Serializable;

/**
 * Escreva a descricao da classe Ator aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public abstract class Ator implements Serializable {
    
    // Variaveis de instancia
    private String email;
    private String nome;
    private String pass;
    private String morada;
    private GregorianCalendar dataNasc;
   
    // Construtores
    public Ator() {
       
       this.email    = "";
       this.nome     = "";
       this.pass     = "";
       this.morada   = "";
       this.dataNasc = new GregorianCalendar();
   
    } 
   
    public Ator(String email, String nome, String pass, String morada, GregorianCalendar dataNasc) {
      
        this.email    = email;
        this.nome     = nome;
        this.pass     = pass;
        this.morada   = morada;
        this.dataNasc = dataNasc;
        
    } 
   
    public Ator(Ator a) {
        
        this.email    = a.getEmail();
        this.nome     = a.getNome();
        this.pass     = a.getPass();
        this.morada   = a.getMorada();
        this.dataNasc = a.getDataNasc();
        
    }
    
    // Outros metodos
    public String getEmail() {
       
        return this.email;
       
    }
    
    public String getNome() {
       
        return this.nome;
        
    }
    
    public String getPass() {
       
        return this.pass;
       
    } 
    
    public String getMorada() {
        
        return this.morada;
        
    } 
    
    public GregorianCalendar getDataNasc() {
        
        return this.dataNasc;
        
    }
    
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        s.append("Utilizador:\n\n");
        s.append("\tEMAIL:    "           + this.email    + "\n");
        s.append("\tPASSWORD: "        + this.pass     + "\n");
        s.append("\tNOME:     "            + this.nome     + "\n");
        s.append("\tMORADA:   "          + this.morada   + "\n");
        //s.append("\tDATA NASCIMENTO: " + this.dataNasc + "\n");
       
        return s.toString();
        
    }
    
    public boolean equals (Object o) {
        
        if(this == o) 
            return true;
        if((o==null || (o.getClass()!=this.getClass()))) 
            return false;
        else { 
            Ator a = (Ator) o;
            return (a.getEmail() == this.email && a.getNome()  == this.nome   &&
                    a.getPass()  == this.pass  && a.getMorada()== this.morada && 
                    a.getDataNasc()==this.dataNasc);
        }
        
    }
    
    public Ator clone() {
        
        return new Ator(this) {};  
        
    }

}
