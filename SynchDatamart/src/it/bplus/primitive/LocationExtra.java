package it.bplus.primitive;


public class LocationExtra {
	
	Location l;
	Double pct_gestore;
	Double pct_esercente;
	Double pct_concessionario;
	
		
	public LocationExtra(){}


	public LocationExtra(Location l, Double pct_gestore, Double pct_esercente,
			Double pct_concessionario) {
		super();
		this.l = l;
		this.pct_gestore = pct_gestore;
		this.pct_esercente = pct_esercente;
		this.pct_concessionario = pct_concessionario;
	}


	public Location getL() {
		return l;
	}


	public void setL(Location l) {
		this.l = l;
	}


	public Double getPct_gestore() {
		return pct_gestore;
	}


	public void setPct_gestore(Double pct_gestore) {
		this.pct_gestore = pct_gestore;
	}


	public Double getPct_esercente() {
		return pct_esercente;
	}


	public void setPct_esercente(Double pct_esercente) {
		this.pct_esercente = pct_esercente;
	}


	public Double getPct_concessionario() {
		return pct_concessionario;
	}


	public void setPct_concessionario(Double pct_concessionario) {
		this.pct_concessionario = pct_concessionario;
	}


	@Override
	public String toString() {
		return "LocationExtra [l=" + l + ", pct_gestore=" + pct_gestore
				+ ", pct_esercente=" + pct_esercente + ", pct_concessionario="
				+ pct_concessionario + "]";
	}
	
	
}
