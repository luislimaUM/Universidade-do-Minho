package business;

public class Morador extends Utilizador
{   
    private String tipo;
    
    public Morador(String nome,String pass,int contacto)
    {
        super(nome,pass,contacto);
        tipo = "Morador";
    }
    public Morador(String nome,String pass)
    {
        super(nome,pass);
        tipo = "Morador";
    }

    public Morador(String name, String pass, int contacto, int saldo) {
        super(name, pass, contacto, saldo);
        tipo = "Morador";
    }
}