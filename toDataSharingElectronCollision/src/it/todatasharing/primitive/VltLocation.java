package it.todatasharing.primitive;

public class VltLocation {
	
	String vlt_id;
	String code_id;
	String location_id;
	
	
	public VltLocation(){}
	
	public VltLocation(String vlt_id, String code_id, String location_id) {
		super();
		this.vlt_id = vlt_id;
		this.code_id = code_id;
		this.location_id = location_id;
	}

	public String getVlt_id() {
		return vlt_id;
	}

	public void setVlt_id(String vlt_id) {
		this.vlt_id = vlt_id;
	}

	public String getCode_id() {
		return code_id;
	}

	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}

	public String getLocation_id() {
		return location_id;
	}

	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code_id == null) ? 0 : code_id.hashCode());
		result = prime * result
				+ ((location_id == null) ? 0 : location_id.hashCode());
		result = prime * result + ((vlt_id == null) ? 0 : vlt_id.hashCode());
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
		VltLocation other = (VltLocation) obj;
		if (code_id == null) {
			if (other.code_id != null)
				return false;
		} else if (!code_id.equals(other.code_id))
			return false;
		if (location_id == null) {
			if (other.location_id != null)
				return false;
		} else if (!location_id.equals(other.location_id))
			return false;
		if (vlt_id == null) {
			if (other.vlt_id != null)
				return false;
		} else if (!vlt_id.equals(other.vlt_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VltLocation [vlt_id=" + vlt_id + ", code_id=" + code_id
				+ ", location_id=" + location_id + "]";
	}
	
	

}
