public class UtilizadorExistenteException extends Exception implements java.io.Serializable {
    
    public UtilizadorExistenteException() {
        
        super("\nO utilizador ja existe!");
        
    }
    
}