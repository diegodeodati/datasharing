package it.bplus.primitive;

public class Game {

	String UGN;	
	long AAMS_GAME_ID=-1;	
	String NAME="";
	long GS_GAME_ID = -1;
	double rtp = -1;
	long GS_ID= -1;
	
	
	
	public Game(){}

	
	public String getUGN() {
		return UGN;
	}

	public void setUGN(String u) {
		UGN = u;
	}
	
	public long getAAMS_GAME_ID() {
		return AAMS_GAME_ID;
	}

	public void setAAMS_GAME_ID(long a) {
		AAMS_GAME_ID = a;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String n) {
		if(n!=null)
		NAME = n;
		else
		NAME = "";
	}

	public long getGS_GAME_ID() {
		return GS_GAME_ID;
	}


	public void setGS_GAME_ID(long gS_GAME_ID) {
		GS_GAME_ID = gS_GAME_ID;
	}
	

	public double getRtp() {
		return rtp;
	}


	public void setRtp(double rtp) {
		this.rtp = rtp;
	}
	
	public long getGS_ID() {
		return GS_ID;
	}


	public void setGS_ID(long gS_ID) {
		GS_ID = gS_ID;
	}

	
	@Override
	public String toString() {
		return "game [UGN=" + UGN + ", AAMS_GAME_ID=" + AAMS_GAME_ID
				+ ", NAME=" + NAME + ", GS_GAME_ID=" + GS_GAME_ID + ", rtp="
				+ rtp + "GS_ID="+GS_ID+"]";
	}


	
}
