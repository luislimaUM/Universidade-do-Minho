package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.lang.StringBuilder;

public class User {
    
    // Variáveis de instância
    private long idU;                             //Identificador do utilizador
    private String name;                          //Nome do utilizador
    private int numPosts;                         //Número total de posts do utilizador
    private String bio;                           //Biografia do utilizador
    private int reputation;
    
    
    public User(){
        this.idU = 0;
        this.name = "N/A";
        this.numPosts = 0;
        this.bio = "N/A";
        this.reputation = 0;
    }
     
    public User(long id, String n, int nr, String bioUser, int r) {
        this.idU = idU;
        this.name = n;
        this.numPosts = nr;
        this.bio = bioUser;
        this.reputation = r;
    }
    
    
    public User(User u) {
        this.idU = u.getIdU();
        this.name = u.getName();
        this.numPosts = u.getNumPosts();
        this.bio = u.getBio();
        this.reputation = u.getReputation();
    }
    
    public long getIdU () {
        return this.idU;
    }
    
    public String getName () {
        return this.name;
    }
    
    public int getNumPosts () {
        return this.numPosts;
    }
    
    public String getBio () {
        return this.bio;
    }
   
    public int getReputation () {
        return this.reputation;
    }
    
    public void setIdU (long id) {
        this.idU = id;
    }
    
    public void setName (String n) {
        this.name = n;
    }
    
    public void setNumPosts (int nr) {
        this.numPosts = nr;
    }
    
    public void setBio (String b) {
        this.bio = b;
    }
 
    public void setReputation (int r) {
        this.reputation = r;
    }
       
     public boolean equals (Object e) {
        if(e == this)
            return true;
        if(e == null || e.getClass() != this.getClass())
            return false;
        User p = (User) e;
        return (this.idU == p.getIdU() &&
                this.name.equals(p.getName()) &&
                this.numPosts == p.getNumPosts() &&
                this.bio.equals(p.getBio()) &&
                this.reputation == p.getReputation());
    }
    
    public User clone () {
        return new User(this);
    }
    
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("ID do utilizador: ");
        sb.append(this.idU);
        sb.append("\n");
        sb.append("Nome: ");
        sb.append(this.name);
        sb.append("\n");
        sb.append("Número de posts: ");
        sb.append(this.numPosts);
        sb.append("\n");
        sb.append("Bio: ");
        sb.append(this.bio);
        sb.append("\n");
        sb.append("Reputação:");
        sb.append(this.reputation);
        sb.append("\n");
        return sb.toString();
    }
}