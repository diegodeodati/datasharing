package it.bplus.exception;

public class MeterException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int code;
	String faultType;
	String exception;

	/** Costruttore della classe 
	 * 
	 * @param c Codice dell'errore
	 * @param f Testo dell'errore
	 * @param string Eccezione
	 */
	public MeterException(String f,String string){
		super();
		faultType = f;
		exception = string;
	}

	public String toString(){
		String ret=" Exception ";
		ret+="Error: "+faultType+" caused by: "+exception;
		return ret;
	}
}