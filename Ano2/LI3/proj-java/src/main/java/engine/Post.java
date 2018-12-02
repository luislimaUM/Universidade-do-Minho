package engine;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.lang.StringBuilder;

public class Post {
    // Variáveis de instância  
    private long idPost;          //Identificador do post
    private String title;           //Nome do título do post. Só para perguntas
    private int typePost;        //Tipo do post. 1 se for pergunta, 2 se for resposta
    private long idUser;          //Identificador do utilizador que publicou o post
    private long idAnswer;        //Só para respostas
    private LocalDate date;            //Timestamp do post
    private String tags;            //Tags do post
    private int score;           //Up votes - Down Votes
    private int nComments;       //Só para respostas - perguntas contem -1 

    
    public Post(){
       this.idPost = 0;
       this.title = "N/A";
       this.typePost = 0;
       this.idUser = 0;
       this.idAnswer = 0;
       this.date = null;
       this.tags = "N/A";
       this.score = 0;
       this.nComments = 0;
    }

    public Post(long id, String tit, int type, long idU, long idA, LocalDate d, String t, int s, int nC) {
       this.idPost = id;
       this.title = tit;
       this.typePost = type;
       this.idUser = idU;
       this.idAnswer = idA;
       this.date = d; 
       this.tags = t;
       this.score = s;
       this.nComments = nC;
    }
    
    public Post(Post p) {
       this.idPost = p.getIdPost();
       this.title = p.getTitle();
       this.typePost = p.getTypePost();
       this.idUser = p.getIdUser();
       this.idAnswer = p.getIdAnswer();
       this.date = p.getData(); 
       this.tags = p.getTags();
       this.score = p.getScore();
       this.nComments = p.getNComments();
    }
    
    public long getIdPost () {
        return this.idPost;
    }
    
    
    public String getTitle () {
        return this.title;
    }
    
    public int getTypePost () {
        return this.typePost;
    }
    
    
    public long getIdUser () {
        return this.idUser;
    }
    
    
    public long getIdAnswer () {
        return this.idAnswer;
    }
    
    public LocalDate getData () {
        return this.date;
    }
    
    public String getTags () {
        return this.tags;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getNComments() {
        return this.nComments;
    }
    
    public void setIdPost (long id) {
        this.idPost = id;
    }
    
    public void setTitle (String t) {
        this.title = t;
    }
    
    public void setTypePost (int i) {
        this.typePost = i;
    }
    
    public void setIdUser(long id) {
        this.idUser = id;
    }
    
    public void setIdAnswer (long id) {
        this.idAnswer = id;
    }
    
    public void setData (LocalDate d) {
        this.date = d;
    }
    
    public void setTags (String t) {
        this.tags = t;
    }
    
    public void setScore (int s) {
        this.score = s;
    }
    
    public void setNComments(int n) {
        this.nComments = n;
    }
    
    
     public boolean equals (Object e) {
        if(e == this)
            return true;
        if(e == null || e.getClass() != this.getClass())
            return false;
        Post p = (Post) e;
        return (this.idPost == p.getIdPost() &&
                this.title.equals(p.getTitle()) &&
                this.typePost == p.getTypePost() &&
                this.idUser == p.getIdUser() &&
                this.idAnswer == p.getIdAnswer() &&
                this.date.equals(p.getData()) &&
                this.tags.equals(p.getTags()) &&
                this.score == p.getScore() &&
                this.nComments == p.getNComments());
    }
    
    
    public Post clone () {
        return new Post(this);
    }
    
    
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("ID do post: ");
        sb.append(this.idPost);
        sb.append("\n");
        sb.append("Título: ");
        sb.append(this.title);
        sb.append("\n");
        sb.append("Tipo do post: ");
        sb.append(this.typePost);
        sb.append("\n");
        sb.append("ID do utilizador: ");
        sb.append(this.idUser);
        sb.append("\n");
        sb.append("ID da resposta: ");
        sb.append(this.idAnswer);
        sb.append("\n");
        sb.append("Data: ");
        sb.append(this.date);
        sb.append("Tags do post: ");
        sb.append(this.tags);
        sb.append("Score: ");
        sb.append(this.score);
        sb.append("Número de comentários: ");
        sb.append(this.nComments);
        sb.append("\n");
        return sb.toString();
    }
}