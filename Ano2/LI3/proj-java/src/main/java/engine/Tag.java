package engine;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.lang.StringBuilder;

public class Tag {
    
    private long id;
    private String tagName;
    private long count;

    public Tag(){
       this.id = 0;
       this.tagName = "N/A";
       this.count = 0;       
    }

    public Tag(long id, String t, long c) {
       this.id = id;
       this.tagName = t;
       this.count = c;      
    }
    
    public Tag(Tag p) {
       this.id = p.getId();
       this.tagName = p.getTagName();
       this.count = p.getCount();       
    }

    public long getId () {
        return this.id;
    }    
    
    public String getTagName () {
        return this.tagName;
    }

    public long getCount () {
        return this.count;
    }    
    
    public void setId (long id) {
        this.id = id;
    }
    
    public void setTagName (String t) {
        this.tagName = t;
    }
    
    public void setCount (long i) {
        this.count = i;
    }

    public boolean equals (Object e) {
        if(e == this)
            return true;
        if(e == null || e.getClass() != this.getClass())
            return false;
        Tag p = (Tag) e;
        return (this.id == p.getId() &&
                this.tagName.equals(p.getTagName()) &&
                this.count == p.getCount());
    }

    public Tag clone () {
        return new Tag(this);
    }

    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("ID da tag: ");
        sb.append(this.id);
        sb.append("\n");
        sb.append("Tag: ");
        sb.append(this.tagName);
        sb.append("\n");
        sb.append("Número de ocorrências: ");
        sb.append(this.count);
        sb.append("\n");
        return sb.toString();
    }
}