package engine;

public class UserInexisteException extends Exception{
	public UserInexisteException(){
		super();
	}
	public UserInexisteException(String msg){
		super(msg);
	}

}