package it.bplus.primitive;

public class SistemaGiocoDim {

	
	long id;
	long AAMS_GAMESYSTEM_CODE;
	String AAMS_VLT_CODE;
	String GS_VLT_CODE;
	long AAMS_GAME_CODE;
	String AAMS_LOCATION_CODE;
	
	public SistemaGiocoDim(){}
	
	public SistemaGiocoDim(long id, long aAMS_GAMESYSTEM_CODE,
			String aAMS_VLT_CODE, String gS_VLT_CODE, long aAMS_GAME_CODE,
			String aAMS_LOCATION_CODE) {
		super();
		this.id = id;
		AAMS_GAMESYSTEM_CODE = aAMS_GAMESYSTEM_CODE;
		AAMS_VLT_CODE = aAMS_VLT_CODE;
		GS_VLT_CODE = gS_VLT_CODE;
		AAMS_GAME_CODE = aAMS_GAME_CODE;
		AAMS_LOCATION_CODE = aAMS_LOCATION_CODE;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAAMS_GAMESYSTEM_CODE() {
		return AAMS_GAMESYSTEM_CODE;
	}
	public void setAAMS_GAMESYSTEM_CODE(long aAMS_GAMESYSTEM_CODE) {
		AAMS_GAMESYSTEM_CODE = aAMS_GAMESYSTEM_CODE;
	}
	public String getAAMS_VLT_CODE() {
		return AAMS_VLT_CODE;
	}
	public void setAAMS_VLT_CODE(String aAMS_VLT_CODE) {
		AAMS_VLT_CODE = aAMS_VLT_CODE;
	}
	public String getGS_VLT_CODE() {
		return GS_VLT_CODE;
	}
	public void setGS_VLT_CODE(String gS_VLT_CODE) {
		GS_VLT_CODE = gS_VLT_CODE;
	}
	public long getAAMS_GAME_CODE() {
		return AAMS_GAME_CODE;
	}
	public void setAAMS_GAME_CODE(long aAMS_GAME_CODE) {
		AAMS_GAME_CODE = aAMS_GAME_CODE;
	}
	public String getAAMS_LOCATION_CODE() {
		return AAMS_LOCATION_CODE;
	}
	public void setAAMS_LOCATION_CODE(String aAMS_LOCATION_CODE) {
		AAMS_LOCATION_CODE = aAMS_LOCATION_CODE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (AAMS_GAMESYSTEM_CODE ^ (AAMS_GAMESYSTEM_CODE >>> 32));
		result = prime * result
				+ (int) (AAMS_GAME_CODE ^ (AAMS_GAME_CODE >>> 32));
		result = prime
				* result
				+ ((AAMS_LOCATION_CODE == null) ? 0 : AAMS_LOCATION_CODE
						.hashCode());
		result = prime * result
				+ ((AAMS_VLT_CODE == null) ? 0 : AAMS_VLT_CODE.hashCode());
		result = prime * result
				+ ((GS_VLT_CODE == null) ? 0 : GS_VLT_CODE.hashCode());
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
		SistemaGiocoDim other = (SistemaGiocoDim) obj;
		if (AAMS_GAMESYSTEM_CODE != other.AAMS_GAMESYSTEM_CODE)
			return false;
		if (AAMS_GAME_CODE != other.AAMS_GAME_CODE)
			return false;
		if (AAMS_LOCATION_CODE == null) {
			if (other.AAMS_LOCATION_CODE != null)
				return false;
		} else if (!AAMS_LOCATION_CODE.equals(other.AAMS_LOCATION_CODE))
			return false;
		if (AAMS_VLT_CODE == null) {
			if (other.AAMS_VLT_CODE != null)
				return false;
		} else if (!AAMS_VLT_CODE.equals(other.AAMS_VLT_CODE))
			return false;
		if (GS_VLT_CODE == null) {
			if (other.GS_VLT_CODE != null)
				return false;
		} else if (!GS_VLT_CODE.equals(other.GS_VLT_CODE))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SistemaGiocoDim [id=" + id + ", AAMS_GAMESYSTEM_CODE="
				+ AAMS_GAMESYSTEM_CODE + ", AAMS_VLT_CODE=" + AAMS_VLT_CODE
				+ ", GS_VLT_CODE=" + GS_VLT_CODE + ", AAMS_GAME_CODE="
				+ AAMS_GAME_CODE + ", AAMS_LOCATION_CODE=" + AAMS_LOCATION_CODE
				+ "]";
	}
	
	
	
}
