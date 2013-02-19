package it.bplus.primitive;

public class ClienteDim {
	
	long id;
	long COD_ESERCENTE;
	String COD_GESTORE;
	
	public ClienteDim(){}
	
	public ClienteDim(long id, long cOD_ESERCENTE, String cOD_GESTORE) {
		super();
		this.id = id;
		COD_ESERCENTE = cOD_ESERCENTE;
		COD_GESTORE = cOD_GESTORE;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCOD_ESERCENTE() {
		return COD_ESERCENTE;
	}

	public void setCOD_ESERCENTE(long cOD_ESERCENTE) {
		COD_ESERCENTE = cOD_ESERCENTE;
	}

	public String getCOD_GESTORE() {
		return COD_GESTORE.trim();
	}

	public void setCOD_GESTORE(String cOD_GESTORE) {
		COD_GESTORE = cOD_GESTORE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (COD_ESERCENTE ^ (COD_ESERCENTE >>> 32));
		result = prime * result
				+ ((COD_GESTORE == null) ? 0 : COD_GESTORE.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClienteDim other = (ClienteDim) obj;
		if (COD_ESERCENTE != other.COD_ESERCENTE)
			return false;
		if (COD_GESTORE == null) {
			if (other.COD_GESTORE != null)
				return false;
		} else if (!COD_GESTORE.trim().equals(other.COD_GESTORE.trim()))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClienteDim [id=" + id + ", COD_ESERCENTE=" + COD_ESERCENTE
				+ ", COD_GESTORE=" + COD_GESTORE + "]";
	}
	
	
	
	
}
