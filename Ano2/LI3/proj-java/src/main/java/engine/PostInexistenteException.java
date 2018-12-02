package engine;

public class PostInexistenteException extends Exception{
	public PostInexistenteException(){
		super();
	}
	public PostInexistenteException(String msg){
		super(msg);
	}
}