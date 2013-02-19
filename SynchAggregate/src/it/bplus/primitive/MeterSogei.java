package it.bplus.primitive;

import it.bplus.exception.MeterException;
import it.bplus.util.IErrorCodes;

public class MeterSogei {

	double bet;
	double win;
	double total_in;
	double total_out;
	double total_ticket_in;
	double total_ticket_out;
	double total_coin_in;
	double total_bill_in;
	double total_card_in;	
	double total_prepaid_in;
	double jackpot_win;
	double jackpot_contribution;
	int games_played;
	int games_win;
	double handpay;
	
	
	public double getTotal_in() {
		return total_in;
	}

	public void setTotal_in(double total_in) {
		if(total_in<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalIn < 0");
		this.total_in = total_in;
	}

	public double getTotal_out() {
		return total_out;
	}

	public void setTotal_out(double total_out) {
		if(total_out<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalOut < 0");
		this.total_out = total_out;
	}

	public double getTotal_ticket_in() {
		return total_ticket_in;
	}

	public void setTotal_ticket_in(double total_ticket_in) {
		if(total_ticket_in<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalTicketIn < 0");
		this.total_ticket_in = total_ticket_in;
	}

	public double getTotal_ticket_out() {
		return total_ticket_out;
	}

	public void setTotal_ticket_out(double total_ticket_out) {
		if(total_ticket_out<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalTicketOut < 0");
		this.total_ticket_out = total_ticket_out;
	}

	public double getTotal_coin_in() {
		return total_coin_in;
	}

	public void setTotal_coin_in(double total_coin_in) {
		if(total_coin_in<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalCoinIn < 0");
		this.total_coin_in = total_coin_in;
	}

	
	public double getTotal_bill_in() {
		return total_bill_in;
	}

	public void setTotal_bill_in(double total_bill_in) {
		if(total_bill_in<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalBillIn < 0");
		this.total_bill_in = total_bill_in;
	}

	public double getTotal_card_in() {
		return total_card_in;
	}

	public void setTotal_card_in(double total_card_in) {
		if(total_card_in<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalCardIn < 0");
		this.total_card_in = total_card_in;
	}

	public double getBet() {
		return bet;
	}

	public void setBet(double bet) {
		if(bet<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore Bet < 0");
		this.bet = bet;
	}

	public double getWin() {
		return win;
	}

	public void setWin(double win) {
		if(win<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore Win < 0");
		this.win = win;
	}

	public int getGames_played() {
		return games_played;
	}

	public void setGames_played(int games_played) {
		this.games_played = games_played;
	}

	public int getGames_win() {
		return games_win;
	}

	public void setGames_win(int games_win) {
		this.games_win = games_win;
	}

	public double getHandpay() {
		return handpay;
	}

	public void setHandpay(double total_out,double total_ticket_out) {		
		this.handpay = total_out-total_ticket_out;
		if(handpay<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalHandpay < 0");
	}
	
	public void setHandpay(double handpay) {
		if(handpay<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore TotalHandpay < 0");
		this.handpay = handpay;
	}

	
	
	public double getJackpot_win() {
		return jackpot_win;
	}

	public void setJackpot_win(double jackpot_win) {
		if(jackpot_win<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore Jackpot Win < 0");
		this.jackpot_win = jackpot_win;
	}

	public double getJackpot_contribution() {
		return jackpot_contribution;
	}

	public void setJackpot_contribution(double jackpot_contribution) {
		if(jackpot_contribution<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore Jackpot Contribution < 0");
		this.jackpot_contribution = jackpot_contribution;
	}

	public double getTotal_prepaid_in() {
		return total_prepaid_in;
	}

	public void setTotal_prepaid_in(double total_prepaid_in) {
		if(total_prepaid_in<0)
			new MeterException(IErrorCodes.MeterAccessErrorMsg,"Errore Prepaid In < 0");
		this.total_prepaid_in = total_prepaid_in;
	}

	@Override
	public String toString() {
		return "MeterSogei [bet=" + bet + ", win=" + win + ", total_in="
				+ total_in + ", total_out=" + total_out + ", total_ticket_in="
				+ total_ticket_in + ", total_ticket_out=" + total_ticket_out
				+ ", total_coin_in=" + total_coin_in + ", total_bill_in=" + total_bill_in
				+ ", total_card_in=" + total_card_in + ", total_prepaid_in="
				+ total_prepaid_in + ", jackpot_win=" + jackpot_win
				+ ", jackpot_contribution=" + jackpot_contribution
				+ ", games_played=" + games_played + ", games_win=" + games_win
				+ ", handpay=" + handpay + "]";
	}

	

	

}
