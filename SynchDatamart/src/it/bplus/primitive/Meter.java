package it.bplus.primitive;

import java.util.Date;

public class Meter {

	java.util.Date data;
	int anno;
	int mese;
	int giorno;
	int ora;
	int minuto;
	long bet;
	long win;
	long jackpot_win;
	long jackpot_contribution;
	long tot_in;
	long tot_out;
	long games_played;
	long games_won;
	long total_handpay;
	long total_ticket_in;
	long total_ticket_out;
	long total_bill_in;
	long total_coin_in;
	long total_prepaid_in;
	long total_card_in;
	long aams_game_code;
	String aams_vlt_code;
	String aams_location_code;
	long aams_game_system_code;
	long unique_session_id;

	public Meter() {
	}

	public Meter(Date data, int anno, int mese, int giorno, int ora,
			int minuto, long bet, long win, long jackpot_win,
			long jackpot_contribution, long tot_in, long tot_out,
			long games_played, long games_won, long total_handpay,
			long total_ticket_in, long total_ticket_out, long total_bill_in,
			long total_coin_in, long total_prepaid_in, long total_card_in,
			long aams_game_code, String aams_vlt_code,
			String aams_location_code, long aams_game_system_code,
			long unique_session_id) {
		super();
		this.data = data;
		this.anno = anno;
		this.mese = mese;
		this.giorno = giorno;
		this.ora = ora;
		this.minuto = minuto;
		this.bet = bet;
		this.win = win;
		this.jackpot_win = jackpot_win;
		this.jackpot_contribution = jackpot_contribution;
		this.tot_in = tot_in;
		this.tot_out = tot_out;
		this.games_played = games_played;
		this.games_won = games_won;
		this.total_handpay = total_handpay;
		this.total_ticket_in = total_ticket_in;
		this.total_ticket_out = total_ticket_out;
		this.total_bill_in = total_bill_in;
		this.total_coin_in = total_coin_in;
		this.total_prepaid_in = total_prepaid_in;
		this.total_card_in = total_card_in;
		this.aams_game_code = aams_game_code;
		this.aams_vlt_code = aams_vlt_code;
		this.aams_location_code = aams_location_code;
		this.aams_game_system_code = aams_game_system_code;
		this.unique_session_id = unique_session_id;
	}

	public java.util.Date getData() {
		return data;
	}

	public void setData(java.util.Date data) {
		this.data = data;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public int getOra() {
		return ora;
	}

	public void setOra(int ora) {
		this.ora = ora;
	}

	public int getMinuto() {
		return minuto;
	}

	public void setMinuto(int minuto) {
		this.minuto = minuto;
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

	public long getJackpot_win() {
		return jackpot_win;
	}

	public void setJackpot_win(long jackpot_win) {
		this.jackpot_win = jackpot_win;
	}

	public long getJackpot_contribution() {
		return jackpot_contribution;
	}

	public void setJackpot_contribution(long jackpot_contribution) {
		this.jackpot_contribution = jackpot_contribution;
	}

	public long getTot_in() {
		return tot_in;
	}

	public void setTot_in(long tot_in) {
		this.tot_in = tot_in;
	}

	public long getTot_out() {
		return tot_out;
	}

	public void setTot_out(long tot_out) {
		this.tot_out = tot_out;
	}

	public long getGames_played() {
		return games_played;
	}

	public void setGames_played(long games_played) {
		this.games_played = games_played;
	}

	public long getGames_won() {
		return games_won;
	}

	public void setGames_won(long games_won) {
		this.games_won = games_won;
	}

	public long getTotal_handpay() {
		return total_handpay;
	}

	public void setTotal_handpay(long total_handpay) {
		this.total_handpay = total_handpay;
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

	public long getTotal_bill_in() {
		return total_bill_in;
	}

	public void setTotal_bill_in(long total_bill_in) {
		this.total_bill_in = total_bill_in;
	}

	public long getTotal_coin_in() {
		return total_coin_in;
	}

	public void setTotal_coin_in(long total_coin_in) {
		this.total_coin_in = total_coin_in;
	}

	public long getTotal_prepaid_in() {
		return total_prepaid_in;
	}

	public void setTotal_prepaid_in(long total_prepaid_in) {
		this.total_prepaid_in = total_prepaid_in;
	}

	public long getTotal_card_in() {
		return total_card_in;
	}

	public void setTotal_card_in(long total_card_in) {
		this.total_card_in = total_card_in;
	}

	public long getAams_game_code() {
		return aams_game_code;
	}

	public void setAams_game_code(long aams_game_code) {
		this.aams_game_code = aams_game_code;
	}

	public String getAams_vlt_code() {
		return aams_vlt_code;
	}

	public void setAams_vlt_code(String aams_vlt_code) {
		this.aams_vlt_code = aams_vlt_code;
	}

	public String getAams_location_code() {
		return aams_location_code;
	}

	public void setAams_location_code(String aams_location_code) {
		this.aams_location_code = aams_location_code;
	}

	public long getAams_game_system_code() {
		return aams_game_system_code;
	}

	public void setAams_game_system_code(long aams_game_system_code) {
		this.aams_game_system_code = aams_game_system_code;
	}

	public long getUnique_session_id() {
		return unique_session_id;
	}

	public void setUnique_session_id(long unique_session_id) {
		this.unique_session_id = unique_session_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (aams_game_code ^ (aams_game_code >>> 32));
		result = prime
				* result
				+ (int) (aams_game_system_code ^ (aams_game_system_code >>> 32));
		result = prime
				* result
				+ ((aams_location_code == null) ? 0 : aams_location_code
						.hashCode());
		result = prime * result
				+ ((aams_vlt_code == null) ? 0 : aams_vlt_code.hashCode());
		result = prime * result + anno;
		result = prime * result + (int) (bet ^ (bet >>> 32));
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + (int) (games_played ^ (games_played >>> 32));
		result = prime * result + (int) (games_won ^ (games_won >>> 32));
		result = prime * result + giorno;
		result = prime * result
				+ (int) (jackpot_contribution ^ (jackpot_contribution >>> 32));
		result = prime * result + (int) (jackpot_win ^ (jackpot_win >>> 32));
		result = prime * result + mese;
		result = prime * result + minuto;
		result = prime * result + ora;
		result = prime * result + (int) (tot_in ^ (tot_in >>> 32));
		result = prime * result + (int) (tot_out ^ (tot_out >>> 32));
		result = prime * result
				+ (int) (total_bill_in ^ (total_bill_in >>> 32));
		result = prime * result
				+ (int) (total_card_in ^ (total_card_in >>> 32));
		result = prime * result
				+ (int) (total_coin_in ^ (total_coin_in >>> 32));
		result = prime * result
				+ (int) (total_handpay ^ (total_handpay >>> 32));
		result = prime * result
				+ (int) (total_prepaid_in ^ (total_prepaid_in >>> 32));
		result = prime * result
				+ (int) (total_ticket_in ^ (total_ticket_in >>> 32));
		result = prime * result
				+ (int) (total_ticket_out ^ (total_ticket_out >>> 32));
		result = prime * result
				+ (int) (unique_session_id ^ (unique_session_id >>> 32));
		result = prime * result + (int) (win ^ (win >>> 32));
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
		Meter other = (Meter) obj;
		if (aams_game_code != other.aams_game_code)
			return false;
		if (aams_game_system_code != other.aams_game_system_code)
			return false;
		if (aams_location_code == null) {
			if (other.aams_location_code != null)
				return false;
		} else if (!aams_location_code.equals(other.aams_location_code))
			return false;
		if (aams_vlt_code == null) {
			if (other.aams_vlt_code != null)
				return false;
		} else if (!aams_vlt_code.equals(other.aams_vlt_code))
			return false;
		if (anno != other.anno)
			return false;
		if (bet != other.bet)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (games_played != other.games_played)
			return false;
		if (games_won != other.games_won)
			return false;
		if (giorno != other.giorno)
			return false;
		if (jackpot_contribution != other.jackpot_contribution)
			return false;
		if (jackpot_win != other.jackpot_win)
			return false;
		if (mese != other.mese)
			return false;
		if (minuto != other.minuto)
			return false;
		if (ora != other.ora)
			return false;
		if (tot_in != other.tot_in)
			return false;
		if (tot_out != other.tot_out)
			return false;
		if (total_bill_in != other.total_bill_in)
			return false;
		if (total_card_in != other.total_card_in)
			return false;
		if (total_coin_in != other.total_coin_in)
			return false;
		if (total_handpay != other.total_handpay)
			return false;
		if (total_prepaid_in != other.total_prepaid_in)
			return false;
		if (total_ticket_in != other.total_ticket_in)
			return false;
		if (total_ticket_out != other.total_ticket_out)
			return false;
		if (unique_session_id != other.unique_session_id)
			return false;
		if (win != other.win)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Meter [data=" + data.toLocaleString() + ", anno=" + anno
				+ ", mese=" + mese + ", giorno=" + giorno + ", ora=" + ora
				+ ", minuto=" + minuto + ", bet=" + bet + ", win=" + win
				+ ", jackpot_win=" + jackpot_win + ", jackpot_contribution="
				+ jackpot_contribution + ", tot_in=" + tot_in + ", tot_out="
				+ tot_out + ", games_played=" + games_played + ", games_won="
				+ games_won + ", total_handpay=" + total_handpay
				+ ", total_ticket_in=" + total_ticket_in
				+ ", total_ticket_out=" + total_ticket_out + ", total_bill_in="
				+ total_bill_in + ", total_coin_in=" + total_coin_in
				+ ", total_prepaid_in=" + total_prepaid_in + ", total_card_in="
				+ total_card_in + ", aams_game_code=" + aams_game_code
				+ ", aams_vlt_code=" + aams_vlt_code + ", aams_location_code="
				+ aams_location_code + ", aams_game_system_code="
				+ aams_game_system_code + ", unique_session_id="
				+ unique_session_id + "]";
	}

}
