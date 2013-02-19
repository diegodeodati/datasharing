package it.bplus.primitive;


public class VltExtra {
	
	Vlt v;
	Double pct_supplier;
	
		
	public VltExtra(){}
	
	public VltExtra(Vlt v, Double pct_supplier) {
		super();
		this.v = v;
		this.pct_supplier = pct_supplier;
	}


	public Vlt getV() {
		return v;
	}


	public void setV(Vlt v) {
		this.v = v;
	}


	public Double getPct_supplier() {
		return pct_supplier;
	}


	public void setPct_supplier(Double pct_supplier) {
		this.pct_supplier = pct_supplier;
	}

	@Override
	public String toString() {
		return "VltExtra [v=" + v + ", pct_supplier=" + pct_supplier + "]";
	}
	
	

}
