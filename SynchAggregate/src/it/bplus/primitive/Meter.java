package it.bplus.primitive;

public abstract class Meter{

	MeterSogei m;	
	MeterInfo e;
	
	
	public Meter(){}
	
	public Meter(MeterSogei m, MeterInfo e) {
		super();
		this.m = m;
		this.e = e;
	}
	
	public MeterSogei getM() {
		return m;
	}
	public void setM(MeterSogei m) {
		this.m = m;
	}
	public MeterInfo getE() {
		return e;
	}
	public void setE(MeterInfo e) {
		this.e = e;
	}
	
	
	
}
