package business;

import java.util.*;
import java.util.stream.Collectors;
public abstract class Utilizador
{
    private String nome;
    private String pass;
    private int contacto;
    private int saldo;

    public Utilizador(String nome,String pass,int contacto, int saldo)
    {
        this.nome = nome;
        this.pass = pass;
        this.contacto = contacto;
        this.saldo = saldo;
    }
    
    public Utilizador(String nome,String pass,int contacto)
    {
        this.nome = nome;
        this.pass = pass;
        this.contacto = contacto;
        this.saldo = 0;
    }
    public Utilizador(String nome,String pass)
    {
        this.nome = nome;
        this.pass = pass;
        this.contacto = 0;
        this.saldo = 0;
    }
    
    public String getNome(){
        return nome;
    }
    public int getContacto(){
        return contacto;
    }
    
    public int getSaldo(){
        return saldo;
    }
    
    public String getPass () {
        return pass;
    }
    
    public String getTipo () {
        if (this instanceof Senhorio) return "Senhorio";
        return "Morador";
    }
   
 
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("NOME: "+nome).append("\n");
        return sb.toString();
    }
}
