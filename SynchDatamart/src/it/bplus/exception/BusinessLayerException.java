package it.bplus.exception;

public class BusinessLayerException extends Exception{
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
	 * @param e Eccezione
	 */
	public BusinessLayerException(String f,Exception e){
		super();
		faultType = f;
		exception = e;
	}
	

	

	

	public String toString(){
		String ret=" Exception ";
		ret+="Error: "+faultType+" caused by: "+exception;
		return ret;
	}
}
