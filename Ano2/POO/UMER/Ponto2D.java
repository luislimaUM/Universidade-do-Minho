import java.lang.*;
import java.io.Serializable;

/**
 * Escreva a descricao da classe Ponto2D aqui.
 * 
 * @author (seu nome) 
 * @version (numero de versao ou data)
 */

public class Ponto2D implements Serializable {
    
    private int x; // ou double??
    private int y;
    
    public Ponto2D() { 
        
        this.x = 0;
        this.y = 0;
        
    }
    
    public Ponto2D(int x, int y) { 
        
        this.x = x; this.y = y; 
        
    }
    
    public Ponto2D(Ponto2D p) { 
        
        x = p.getX();
        y = p.getY(); 
        
    }
    
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    
    public Ponto2D clone() {
        
        return new Ponto2D(this);
    
    }
    
    public boolean equals(Object o) {
        
        if(o == this)
            return true;
        if(o == null || o.getClass() != this.getClass())
            return false;
        Ponto2D p = (Ponto2D)o;
        return (p.getX() == this.x && p.getY() == this.y);
        
    }
    
    public String toString() {
        
        return ("x = " + this.x + " y = " + this.y);
        
    }
    
    public static double calculaDistancia(int xo, int yo, Ponto2D d) {
        
        int xd, yd;
        
        xd = d.getX();
        yd = d.getY();
        
        return Math.sqrt(Math.pow((xd-xo), 2)+Math.pow((yd-yo), 2));
        
    }
    
}
