package it.bplus.primitive;


public class VltGame {

	
	String AAMS_VLT_ID;	
	long AAMS_GAME_ID;
	boolean ENABLE;
	
	public VltGame() {
		super();
	}
	
	public VltGame(String aAMS_VLT_ID, long aAMS_GAME_ID, boolean eNABLE) {
		super();
		AAMS_VLT_ID = aAMS_VLT_ID;
		AAMS_GAME_ID = aAMS_GAME_ID;
		ENABLE = eNABLE;
	}

	public String getAAMS_VLT_ID() {
		return AAMS_VLT_ID;
	}

	public void setAAMS_VLT_ID(String aAMS_VLT_ID) {
		AAMS_VLT_ID = aAMS_VLT_ID;
	}

	public long getAAMS_GAME_ID() {
		return AAMS_GAME_ID;
	}

	public void setAAMS_GAME_ID(long aAMS_GAME_ID) {
		AAMS_GAME_ID = aAMS_GAME_ID;
	}

	public boolean isENABLE() {
		return ENABLE;
	}

	public void setENABLE(boolean eNABLE) {
		ENABLE = eNABLE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (AAMS_GAME_ID ^ (AAMS_GAME_ID >>> 32));
		result = prime * result
				+ ((AAMS_VLT_ID == null) ? 0 : AAMS_VLT_ID.hashCode());
		result = prime * result + (ENABLE ? 1231 : 1237);
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
		VltGame other = (VltGame) obj;
		if (AAMS_GAME_ID != other.AAMS_GAME_ID)
			return false;
		if (AAMS_VLT_ID == null) {
			if (other.AAMS_VLT_ID != null)
				return false;
		} else if (!AAMS_VLT_ID.equals(other.AAMS_VLT_ID))
			return false;
		if (ENABLE != other.ENABLE)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VltGame [AAMS_VLT_ID=" + AAMS_VLT_ID + ", AAMS_GAME_ID="
				+ AAMS_GAME_ID + ", ENABLE=" + ENABLE + "]";
	}
	
	
	
}
