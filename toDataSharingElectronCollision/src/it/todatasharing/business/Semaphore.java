package it.todatasharing.business;


public class Semaphore {
	
	private boolean signal;
	
	private static Semaphore instance;

	private Semaphore() {		
		super();
		signal = false;
	}

	public static synchronized Semaphore getInstance() {
		if (instance == null) {
			synchronized (Semaphore.class) {
				instance = new Semaphore();
			}
		}
		return instance;
	}

	public boolean isSignal() {
		return signal;
	}

	public void setSignal(boolean signal) {
		this.signal = signal;
	}

	public void take(){
		this.signal=true;
	}
	
	public void release(){
		this.signal=false;
	}
}
