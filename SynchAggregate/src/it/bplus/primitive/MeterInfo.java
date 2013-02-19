package it.bplus.primitive;

public abstract class MeterInfo {

	String aamsLocationCode;
	String aamsVltCode;
	long aamsGameCode;
	java.util.Date dataRif;
	long gameSystemCode;
	long session_id;

	public MeterInfo(String aamsLocationCode, String aamsVltCode,
			long aamsGameCode, java.util.Date dataRif, long gameSystemCode,
			long session_id) {
		super();
		this.aamsLocationCode = aamsLocationCode;
		this.aamsVltCode = aamsVltCode;
		this.aamsGameCode = aamsGameCode;
		this.gameSystemCode = gameSystemCode;
		this.session_id = session_id;
	}

	public MeterInfo() {
	}

	public String getAamsLocationCode() {
		return aamsLocationCode.toUpperCase();
	}

	public void setAamsLocationCode(String aamsLocationCode) {
		this.aamsLocationCode = aamsLocationCode;
	}

	public String getAamsVltCode() {
		return aamsVltCode.toUpperCase();
	}

	public void setAamsVltCode(String aamsVltCode) {
		this.aamsVltCode = aamsVltCode;
	}

	public long getAamsGameCode() {
		return aamsGameCode;
	}

	public void setAamsGameCode(long aamsGameCode) {
		this.aamsGameCode = aamsGameCode;
	}

	public long getGameSystemCode() {
		return gameSystemCode;
	}

	public void setGameSystemCode(long gameSystemCode) {
		this.gameSystemCode = gameSystemCode;
	}

	public java.util.Date getDataRif() {
		return dataRif;
	}

	public void setDataRif(java.util.Date dataRif) {
		this.dataRif = dataRif;
	}

	public long getSessionId() {
		return session_id;
	}

	public void setSessionId(long session_id) {
		this.session_id = session_id;
	}

	@Override
	public String toString() {
		return "MeterInfo [aamsLocationCode=" + aamsLocationCode
				+ ", aamsVltCode=" + aamsVltCode + ", aamsGameCode="
				+ aamsGameCode + ", dataRif=" + dataRif + ", gameSystemCode="
				+ gameSystemCode + ", session_id=" + session_id + "]";
	}

}
