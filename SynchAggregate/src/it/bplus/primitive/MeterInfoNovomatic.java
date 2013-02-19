package it.bplus.primitive;

import java.util.Date;

public class MeterInfoNovomatic extends MeterInfo{

	
	public MeterInfoNovomatic() {
		super();
		gameSystemCode=1711000045;
		// TODO Auto-generated constructor stub
	}

	public MeterInfoNovomatic(String aamsLocationCode, String aamsVltCode,
			long aamsGameCode, Date dataRif,  long session) {
		super(aamsLocationCode, aamsVltCode, aamsGameCode, dataRif, 1711000045,
				session);
		// TODO Auto-generated constructor stub
	}

}
