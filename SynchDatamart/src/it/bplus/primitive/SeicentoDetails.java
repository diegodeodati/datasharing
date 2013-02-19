package it.bplus.primitive;

public class SeicentoDetails {
	
	
	long id;
	long game_id;
	String code_id;
	long gs_id;
	long bet;
	long win;
	long bet_num;
	long tot_bet;
	long tot_win;
	long tot_bet_num;
	java.sql.Date date;
	int year=0;
	int month=0;
	int day=0;
	long msg_id;
	
	public SeicentoDetails(){}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getGame_id() {
		return game_id;
	}
	public void setGame_id(long game_id) {
		this.game_id = game_id;
	}
	public String getCode_id() {
		return code_id.toUpperCase();
	}
	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}
	public long getGs_id() {
		return gs_id;
	}
	public void setGs_id(long gs_id) {
		this.gs_id = gs_id;
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
	public long getBet_num() {
		return bet_num;
	}
	public void setBet_num(long bet_num) {
		this.bet_num = bet_num;
	}
	public long getTot_bet() {
		return tot_bet;
	}
	public void setTot_bet(long tot_bet) {
		this.tot_bet = tot_bet;
	}
	public long getTot_win() {
		return tot_win;
	}
	public void setTot_win(long tot_win) {
		this.tot_win = tot_win;
	}
	public long getTot_bet_num() {
		return tot_bet_num;
	}
	public void setTot_bet_num(long tot_bet_num) {
		this.tot_bet_num = tot_bet_num;
	}
	public java.sql.Date getDate() {
		return date;
	}
	public void setDate(java.sql.Date date) {
		this.date = date;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(long msg_id) {
		this.msg_id = msg_id;
	}
	
	@Override
	public String toString() {
		return "seicentoDetails [id=" + id + ", game_id=" + game_id
				+ ", code_id=" + code_id + ", gs_id=" + gs_id + ", bet=" + bet
				+ ", win=" + win + ", bet_num=" + bet_num + ", tot_bet="
				+ tot_bet + ", tot_win=" + tot_win + ", tot_bet_num="
				+ tot_bet_num + ", date=" + date + ", year=" + year
				+ ", month=" + month + ", day=" + day + ", msg_id=" + msg_id
				+ "]";
	}

}
