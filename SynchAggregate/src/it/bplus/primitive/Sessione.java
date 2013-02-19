package it.bplus.primitive;

import java.util.Date;

public class Sessione {

	
	long UNIQUE_SESSION_ID;
	long AAMS_GAME_SYSTEM_CODE;
	String TYPE;
	int EXIT_CODE;
	java.util.Date START_DATE;
	java.util.Date END_DATE;
	java.util.Date OPERATION_DATE_START;
	java.util.Date OPERATION_DATE_END;
	
	public Sessione(){}
	
	public Sessione(long uNIQUE_SESSION_ID, long aAMS_GAME_SYSTEM_CODE,
			String tYPE, int eXIT_CODE, Date sTART_DATE, Date eND_DATE,
			Date oPERATION_DATE_START, Date oPERATION_DATE_END) {
		super();
		UNIQUE_SESSION_ID = uNIQUE_SESSION_ID;
		AAMS_GAME_SYSTEM_CODE = aAMS_GAME_SYSTEM_CODE;
		TYPE = tYPE;
		EXIT_CODE = eXIT_CODE;
		START_DATE = sTART_DATE;
		END_DATE = eND_DATE;
		OPERATION_DATE_START = oPERATION_DATE_START;
		OPERATION_DATE_END = oPERATION_DATE_END;
	}

	public long getUNIQUE_SESSION_ID() {
		return UNIQUE_SESSION_ID;
	}

	public void setUNIQUE_SESSION_ID(long uNIQUE_SESSION_ID) {
		UNIQUE_SESSION_ID = uNIQUE_SESSION_ID;
	}

	public long getAAMS_GAME_SYSTEM_CODE() {
		return AAMS_GAME_SYSTEM_CODE;
	}

	public void setAAMS_GAME_SYSTEM_CODE(long aAMS_GAME_SYSTEM_CODE) {
		AAMS_GAME_SYSTEM_CODE = aAMS_GAME_SYSTEM_CODE;
	}

	public int getEXIT_CODE() {
		return EXIT_CODE;
	}

	public void setEXIT_CODE(int eXIT_CODE) {
		EXIT_CODE = eXIT_CODE;
	}

	public java.util.Date getSTART_DATE() {
		return START_DATE;
	}

	public void setSTART_DATE(java.util.Date sTART_DATE) {
		START_DATE = sTART_DATE;
	}

	public java.util.Date getEND_DATE() {
		return END_DATE;
	}

	public void setEND_DATE(java.util.Date eND_DATE) {
		END_DATE = eND_DATE;
	}

	public java.util.Date getOPERATION_DATE_START() {
		return OPERATION_DATE_START;
	}

	public void setOPERATION_DATE_START(java.util.Date oPERATION_DATE_START) {
		OPERATION_DATE_START = oPERATION_DATE_START;
	}

	public java.util.Date getOPERATION_DATE_END() {
		return OPERATION_DATE_END;
	}

	public void setOPERATION_DATE_END(java.util.Date oPERATION_DATE_END) {
		OPERATION_DATE_END = oPERATION_DATE_END;
	}

	@Override
	public String toString() {
		return "Sessione [UNIQUE_SESSION_ID=" + UNIQUE_SESSION_ID
				+ ", AAMS_GAME_SYSTEM_CODE=" + AAMS_GAME_SYSTEM_CODE
				+ ", EXIT_CODE=" + EXIT_CODE
				+ ", START_DATE=" + START_DATE + ", END_DATE=" + END_DATE
				+ ", OPERATION_DATE_START=" + OPERATION_DATE_START
				+ ", OPERATION_DATE_END=" + OPERATION_DATE_END + "]";
	}
	
	
}
