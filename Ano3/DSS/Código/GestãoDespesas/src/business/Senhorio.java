package business;

public class Senhorio extends Utilizador
{
    private String tipo;
    
    public Senhorio(String nome,String pass,int contacto)
    {
        super(nome,pass,contacto);
        tipo = "Senhorio";
    }
    public Senhorio(String nome,String pass)
    {
        super(nome,pass);
        tipo = "Senhorio";
    }
    
    
}
