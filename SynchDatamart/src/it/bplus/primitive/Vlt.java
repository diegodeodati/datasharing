package it.bplus.primitive;

import java.sql.Timestamp;
import java.util.List;

public class Vlt {

	String AAMS_VLT_ID;
	String GS_VLT_CODE;
	String CIV;
	int ID_VLT_MODEL;
	String LOCATION_ID;
	long GS_ID;
	int CONNECTION_TYPE;
	String MAC_ADDRESS;
	Timestamp CREATION_DATE;
	Timestamp CESSATION_DATE;
	Timestamp VARIATION_DATE;
	
	
	
	public Vlt(){
		AAMS_VLT_ID = "";
		GS_VLT_CODE = "";
		CIV = "";
		ID_VLT_MODEL = -1;
		LOCATION_ID = "";
		GS_ID = -1;
		MAC_ADDRESS = "";	
		CONNECTION_TYPE = 0; //custom lan
	}
	
	public String getAAMS_VLT_ID() {
		return AAMS_VLT_ID.toUpperCase();
	}


	public void setAAMS_VLT_ID(String aAMS_VLT_ID) {
		AAMS_VLT_ID = aAMS_VLT_ID;
	}


	public String getGS_VLT_CODE() {
		return GS_VLT_CODE.toUpperCase();
	}


	public void setGS_VLT_CODE(String gS_VLT_CODE) {
		GS_VLT_CODE = gS_VLT_CODE;
	}


	public String getCIV() {
		return CIV;
	}


	public void setCIV(String cIV) {
		CIV = cIV;
	}


	public int getID_VLT_MODEL() {
		return ID_VLT_MODEL;
	}


	public void setID_VLT_MODEL(int iD_VLT_MODEL) {
		ID_VLT_MODEL = iD_VLT_MODEL;
	}

	
	

	public String getLOCATION_ID() {
		return LOCATION_ID.toUpperCase();
	}

	public void setLOCATION_ID(String lOCATION_ID) {
		LOCATION_ID = lOCATION_ID;
	}

	public long getGS_ID() {
		return GS_ID;
	}

	public void setGS_ID(long gS_ID) {
		GS_ID = gS_ID;
	}

	public int getCONNECTION_TYPE() {
		return CONNECTION_TYPE;
	}

	public void setCONNECTION_TYPE(int cONNECTION_TYPE) {
		CONNECTION_TYPE = cONNECTION_TYPE;
	}

	public String getMAC_ADDRESS() {
		return MAC_ADDRESS.toUpperCase();
	}

	public void setMAC_ADDRESS(String mAC_ADDRESS) {
		MAC_ADDRESS = mAC_ADDRESS;
	}

	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public Timestamp getCESSATION_DATE() {
		return CESSATION_DATE;
	}

	public void setCESSATION_DATE(Timestamp cESSATION_DATE) {
		CESSATION_DATE = cESSATION_DATE;
	}

	public Timestamp getVARIATION_DATE() {
		return VARIATION_DATE;
	}

	public void setVARIATION_DATE(Timestamp vARIATION_DATE) {
		VARIATION_DATE = vARIATION_DATE;
	}
	

	@Override
	public String toString() {
		return "vlt [AAMS_VLT_ID=" + AAMS_VLT_ID + ", GS_VLT_CODE="
				+ GS_VLT_CODE + ", CIV=" + CIV + ", ID_VLT_MODEL="
				+ ID_VLT_MODEL + ", LOCATION_ID=" + LOCATION_ID + ", GS_ID="
				+ GS_ID + ", CONNECTION_TYPE=" + CONNECTION_TYPE
				+ ", MAC_ADDRESS=" + MAC_ADDRESS + ", CREATION_DATE="
				+ CREATION_DATE + ", CESSATION_DATE=" + CESSATION_DATE
				+ ", VARIATION_DATE=" + VARIATION_DATE + "]";
	}

	
	public static String toParamenterVltID(List<Vlt> actualVltDismissed){
		String stringsVltId = "(";	
		int i = 0;
		for (Vlt v : actualVltDismissed){
			stringsVltId+= "'"+v.getAAMS_VLT_ID()+"'";
			
			if(i<actualVltDismissed.size()-1){
				stringsVltId+=",";	
			}
				
			i++;
		}
		
		return stringsVltId+")";
	}
	
}
