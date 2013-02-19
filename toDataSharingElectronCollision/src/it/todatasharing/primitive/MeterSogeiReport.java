package it.todatasharing.primitive;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MeterSogeiReport implements Serializable{


	private static final long serialVersionUID = -8893418691342317188L;
	
	long club_id;
	String machine_cn;
	long game_id;
	
	long total_in;
	long total_out;
	long total_ticket_in;
	long total_ticket_out;
	long total_coin_in;
	long total_bill_in;
	long total_card_in;
	long total_bet;
	long total_win;
	long games_played;
	long total_handpay;
	long payouts;
	
	public MeterSogeiReport(){
		club_id = 0;
		machine_cn = "";
		game_id = 0;
		total_in = 0;
		total_out = 0;
		total_ticket_in = 0;
		total_ticket_out = 0;
		total_coin_in = 0;
		total_bill_in = 0;
		total_card_in = 0;
		total_bet = 0;
		total_win = 0;
		games_played = 0;
		total_handpay= 0;
		payouts=0;
		
	}
	
	public MeterSogeiReport(long club_id, String machine_cn, long game_id,
			long total_in, long total_out, long total_ticket_in,
			long total_ticket_out, long total_coin_in, long total_bill_in,
			long total_card_in, long total_bet, long total_win,
			long games_played,long total_handpay,long payouts) {
		super();
		this.club_id = club_id;
		this.machine_cn = machine_cn;
		this.game_id = game_id;
		this.total_in = total_in;
		this.total_out = total_out;
		this.total_ticket_in = total_ticket_in;
		this.total_ticket_out = total_ticket_out;
		this.total_coin_in = total_coin_in;
		this.total_bill_in = total_bill_in;
		this.total_card_in = total_card_in;
		this.total_bet = total_bet;
		this.total_win = total_win;
		this.games_played = games_played;
		this.total_handpay = total_handpay;
		this.payouts = payouts;
	}

	public long getClub_id() {
		return club_id;
	}

	public void setClub_id(long club_id) {
		this.club_id = club_id;
	}

	public String getMachine_cn() {
		return machine_cn;
	}

	public void setMachine_cn(String machine_cn) {
		this.machine_cn = machine_cn;
	}

	public long getGame_id() {
		return game_id;
	}

	public void setGame_id(long game_id) {
		this.game_id = game_id;
	}

	public long getTotal_in() {
		return total_in;
	}

	public void setTotal_in(long total_in) {
		this.total_in = total_in;
	}

	public long getTotal_out() {
		return total_out;
	}

	public void setTotal_out(long total_out) {
		this.total_out = total_out;
	}

	public long getTotal_ticket_in() {
		return total_ticket_in;
	}

	public void setTotal_ticket_in(long total_ticket_in) {
		this.total_ticket_in = total_ticket_in;
	}

	public long getTotal_ticket_out() {
		return total_ticket_out;
	}

	public void setTotal_ticket_out(long total_ticket_out) {
		this.total_ticket_out = total_ticket_out;
	}

	public long getTotal_coin_in() {
		return total_coin_in;
	}

	public void setTotal_coin_in(long total_coin_in) {
		this.total_coin_in = total_coin_in;
	}

	public long getTotal_bill_in() {
		return total_bill_in;
	}

	public void setTotal_bill_in(long total_bill_in) {
		this.total_bill_in = total_bill_in;
	}

	public long getTotal_card_in() {
		return total_card_in;
	}

	public void setTotal_card_in(long total_card_in) {
		this.total_card_in = total_card_in;
	}

	public long getTotal_bet() {
		return total_bet;
	}

	public void setTotal_bet(long total_bet) {
		this.total_bet = total_bet;
	}

	public long getTotal_win() {
		return total_win;
	}

	public void setTotal_win(long total_win) {
		this.total_win = total_win;
	}

	public long getGames_played() {
		return games_played;
	}

	public void setGames_played(long games_played) {
		this.games_played = games_played;
	}

	public long getTotal_handpay() {
		return total_handpay;
	}

	public void setTotal_handpay(long total_handpay) {
		this.total_handpay = total_handpay;
	}

	public long getPayouts() {
		return payouts;
	}

	public void setPayouts(long payouts) {
		this.payouts = payouts;
	}

	/*FORSE POTREBBE NON SERVIRE*/
	public void sum(MeterSogeiReport msrAdd){
		
		this.total_in = this.total_in+msrAdd.total_in;
		this.total_out = this.total_out+msrAdd.total_out;
		this.total_ticket_in = this.total_ticket_in+msrAdd.total_ticket_in;
		this.total_ticket_out = this.total_ticket_out+msrAdd.total_ticket_out;
		this.total_coin_in = this.total_coin_in+msrAdd.total_coin_in;
		this.total_bill_in = this.total_bill_in+msrAdd.total_bill_in;
		this.total_card_in = this.total_card_in+msrAdd.total_card_in;
		this.total_bet = this.total_bet+msrAdd.total_bet;
		this.total_win = this.total_win+msrAdd.total_win;
		this.games_played = this.games_played+msrAdd.games_played;
		this.total_handpay = this.total_handpay + msrAdd.total_handpay;
		this.payouts = this.payouts + msrAdd.payouts;
		
	}



	@Override
	public String toString() {
		return "MeterSogeiReport [club_id=" + club_id + ", machine_cn="
				+ machine_cn + ", game_id=" + game_id + ", total_in="
				+ total_in + ", total_out=" + total_out + ", total_ticket_in="
				+ total_ticket_in + ", total_ticket_out=" + total_ticket_out
				+ ", total_coin_in=" + total_coin_in + ", total_bill_in="
				+ total_bill_in + ", total_card_in=" + total_card_in
				+ ", total_bet=" + total_bet + ", total_win=" + total_win
				+ ", games_played=" + games_played + ", total_handpay="
				+ total_handpay + ", payouts=" + payouts + "]";
	}

	/*FORSE POTREBBE NON SERVIRE*/
	public static MeterSogeiReport delta(MeterSogeiReport msPrev, MeterSogeiReport msAct){
		MeterSogeiReport dMeterSogeiReport = new MeterSogeiReport();
		
		dMeterSogeiReport.club_id = msAct.club_id;
		dMeterSogeiReport.machine_cn = msAct.machine_cn;
		dMeterSogeiReport.game_id = msAct.game_id;
		
		dMeterSogeiReport.total_in = msAct.total_in-msPrev.total_in;
		dMeterSogeiReport.total_out = msAct.total_out-msPrev.total_out;
		dMeterSogeiReport.total_ticket_in = msAct.total_ticket_in-msPrev.total_ticket_in;
		dMeterSogeiReport.total_ticket_out = msAct.total_ticket_out-msPrev.total_ticket_out;
		dMeterSogeiReport.total_coin_in = msAct.total_coin_in-msPrev.total_coin_in;
		dMeterSogeiReport.total_bill_in = msAct.total_bill_in-msPrev.total_bill_in;
		dMeterSogeiReport.total_card_in = msAct.total_card_in-msPrev.total_card_in;
		dMeterSogeiReport.total_bet = msAct.total_bet-msPrev.total_bet;
		dMeterSogeiReport.total_win = msAct.total_win-msPrev.total_win;
		dMeterSogeiReport.games_played = msAct.games_played-msPrev.games_played;
		dMeterSogeiReport.total_handpay = msAct.total_handpay - msPrev.total_handpay;
		dMeterSogeiReport.payouts = msAct.payouts - msPrev.payouts;
		
		return dMeterSogeiReport;
	}
	
	
	
	
	public MeterSogeiReport clone(){
		MeterSogeiReport msrClone = new MeterSogeiReport();
		
		Class<?> c = this.getClass();
	    Field[] fields = c.getDeclaredFields();
	    
	    Class<?> msrCloneClass = msrClone.getClass();
	       
	    for(int i = 0; i < fields.length; i++) {
	    
	    	Field currentField = fields[i];
	    	currentField.setAccessible(true);

	    	if (!Modifier.isStatic(currentField.getModifiers())) {
	    		
	    		Field actCurrentField;
	    		
	    		try {
	    			actCurrentField = msrCloneClass.getDeclaredField(currentField.getName());
	    		} catch(NoSuchFieldException nsfe){
	    			actCurrentField = null;
	    		}
	    		
	    		if(actCurrentField != null) {
	    			actCurrentField.setAccessible(true);
		    		try {
						currentField.set(this, actCurrentField.get(msrCloneClass));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}		 
	    		}
	    		
	    	}
	    	
	    }
	    
       return msrClone;
		
	}
	
	
	
	
}
