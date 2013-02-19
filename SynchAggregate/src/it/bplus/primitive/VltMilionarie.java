package it.bplus.primitive;

import java.util.Date;

public class VltMilionarie {
	
	  long id;
	  String aams_vlt_id;
	  Date data;
	  String aams_location_id;
	  long bet;
	  long win;
	  long bet_reale;
	  long win_reale;
	  
	public VltMilionarie(long id, String aams_vlt_id, Date data,
			String aams_location_id, long bet, long win, long bet_reale,
			long win_reale) {
		super();
		this.id = id;
		this.aams_vlt_id = aams_vlt_id;
		this.data = data;
		this.aams_location_id = aams_location_id;
		this.bet = bet;
		this.win = win;
		this.bet_reale = bet_reale;
		this.win_reale = win_reale;
	}

	public VltMilionarie() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAams_vlt_id() {
		return aams_vlt_id;
	}

	public void setAams_vlt_id(String aams_vlt_id) {
		this.aams_vlt_id = aams_vlt_id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getAams_location_id() {
		return aams_location_id;
	}

	public void setAams_location_id(String aams_location_id) {
		this.aams_location_id = aams_location_id;
	}

	public long getBet() {
		return bet;
	}

	public void setBet(long bet) {
		this.bet = bet;
	}

	public long getWin() {
		return win;
	}

	public void setWin(long win) {
		this.win = win;
	}

	public long getBet_reale() {
		return bet_reale;
	}

	public void setBet_reale(long bet_reale) {
		this.bet_reale = bet_reale;
	}

	public long getWin_reale() {
		return win_reale;
	}

	public void setWin_reale(long win_reale) {
		this.win_reale = win_reale;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VltMilionarie other = (VltMilionarie) obj;
		if (aams_location_id == null) {
			if (other.aams_location_id != null)
				return false;
		} else if (!aams_location_id.equals(other.aams_location_id))
			return false;
		if (aams_vlt_id == null) {
			if (other.aams_vlt_id != null)
				return false;
		} else if (!aams_vlt_id.equals(other.aams_vlt_id))
			return false;
		if (bet != other.bet)
			return false;
		if (bet_reale != other.bet_reale)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (id != other.id)
			return false;
		if (win != other.win)
			return false;
		if (win_reale != other.win_reale)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VltMilionarie [id=" + id + ", aams_vlt_id=" + aams_vlt_id
				+ ", data=" + data + ", aams_location_id=" + aams_location_id
				+ ", bet=" + bet + ", win=" + win + ", bet_reale=" + bet_reale
				+ ", win_reale=" + win_reale + "]";
	}
	  
	  
}
