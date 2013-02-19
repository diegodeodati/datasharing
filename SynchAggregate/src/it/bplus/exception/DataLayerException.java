package it.bplus.exception;

public class DataLayerException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String faultType;
	Exception exception;
	
	
	/** Costruttore della classe 
	 * 
	 * @param c Codice dell'errore
	 * @param f Testo dell'errore
	 * @param string Eccezione
	 */
	public DataLayerException(String f,Exception string){
		super();
		faultType = f;
		exception = string;
	}
	
	
	
	public DataLayerException(String f){
		super();
		faultType = f;
	}	
	

	public String toString(){
		String ret=" Exception ";
		ret+="Error: "+faultType+" caused by: "+exception;
		return ret;
	}
}
