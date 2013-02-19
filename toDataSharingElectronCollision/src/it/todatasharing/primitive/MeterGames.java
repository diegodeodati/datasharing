package it.todatasharing.primitive;

import java.util.ArrayList;
import java.util.HashMap;

public class MeterGames {
	
	private long session_id;
	private String aams_location_id;
	private String aams_code_id;
	private String machine_gs_id;
	private long aams_game_id;
	private long bet;
	private long win;
	private long games_played;
	private int games_won;
	private long jackpot_win;
	private long jackpot_contribution;
	private String club_id;
	
	public MeterGames(){
		session_id = 0 ;
		aams_location_id = "";
		aams_code_id = "";
		machine_gs_id = "";
		aams_game_id = 0;
		bet = 0;
		win = 0;
		games_played = 0;
		games_won = 0;
		jackpot_win = 0;
		jackpot_contribution = 0;
		club_id = "";
	}

	public MeterGames(long session_id, String aams_location_id,
			String aams_code_id, String machine_gs_id, long aams_game_id,
			long bet, long win, int games_played, int games_won,
			int jackpot_win, int jackpot_contribution,String club_id) {
		super();
		this.session_id = session_id;
		this.aams_location_id = aams_location_id;
		this.aams_code_id = aams_code_id;
		this.machine_gs_id = machine_gs_id;
		this.aams_game_id = aams_game_id;
		this.bet = bet;
		this.win = win;
		this.games_played = games_played;
		this.games_won = games_won;
		this.jackpot_win = jackpot_win;
		this.jackpot_contribution = jackpot_contribution;
		this.club_id=club_id;
	}

	public long getSession_id() {
		return session_id;
	}

	public void setSession_id(long session_id) {
		this.session_id = session_id;
	}

	public String getAams_location_id() {
		return aams_location_id;
	}

	public void setAams_location_id(String aams_location_id) {
		this.aams_location_id = aams_location_id;
	}

	public String getAams_code_id() {
		return aams_code_id;
	}

	public void setAams_code_id(String aams_code_id) {
		this.aams_code_id = aams_code_id;
	}

	public String getMachine_gs_id() {
		return machine_gs_id;
	}

	public void setMachine_gs_id(String machine_gs_id) {
		this.machine_gs_id = machine_gs_id;
	}

	public long getAams_game_id() {
		return aams_game_id;
	}

	public void setAams_game_id(long aams_game_id) {
		this.aams_game_id = aams_game_id;
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

	public long getGames_played() {
		return games_played;
	}

	public void setGames_played(long games_played) {
		this.games_played = games_played;
	}

	public int getGames_won() {
		return games_won;
	}

	public void setGames_won(int games_won) {
		this.games_won = games_won;
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
	
	
	public String getClub_id() {
		return club_id;
	}

	public void setClub_id(String club_id) {
		this.club_id = club_id;
	}

	public void sum(MeterGames mgAdd){
		
		this.bet = this.bet+mgAdd.bet;
		this.win = this.win+mgAdd.win;
		this.games_played = this.games_played+mgAdd.games_played;
		this.games_won = this.games_won+mgAdd.games_won;
		this.jackpot_win = this.jackpot_win+mgAdd.jackpot_win;
		this.jackpot_contribution = this.jackpot_contribution+mgAdd.jackpot_contribution;
		
	}
	
	public static MeterGames subtract(MeterGames mgPrev, MeterGames mgAct){
		MeterGames dMeterGames = new MeterGames();
		
		dMeterGames.session_id = mgAct.session_id;
		dMeterGames.aams_location_id = mgAct.aams_location_id;
		dMeterGames.aams_code_id = mgAct.aams_code_id;
		dMeterGames.machine_gs_id = mgAct.machine_gs_id;
		dMeterGames.aams_game_id = mgAct.aams_game_id;
		
		dMeterGames.bet = mgAct.bet-mgPrev.bet;
		dMeterGames.win = mgAct.win-mgPrev.win;
		dMeterGames.games_played = mgAct.games_played-mgPrev.games_played;
		dMeterGames.games_won = mgAct.games_won-mgPrev.games_won;
		dMeterGames.jackpot_win = mgAct.jackpot_win-mgPrev.jackpot_win;
		dMeterGames.jackpot_contribution = mgAct.jackpot_contribution-mgPrev.jackpot_contribution;
		dMeterGames.club_id = mgAct.club_id;
		
		return dMeterGames;
	}

	public static HashMap<String,MeterGames> delta(HashMap<String, MeterGames> hPrev, HashMap<String, MeterGames> hAct){
		HashMap< String,MeterGames> map = new HashMap<String, MeterGames>();
		
		ArrayList<MeterGames> list  = new ArrayList<MeterGames>(hAct.values());
		
		for(MeterGames mgAct: list){
			if(hPrev.containsKey(mgAct.getKey())){
				
				MeterGames mgPrev = hPrev.get(mgAct.getKey());				

				MeterGames cInsert = subtract(mgPrev, mgAct);
				
				map.put(cInsert.getKey(), cInsert);
				
			}
			else{
				MeterGames cInsert = subtract(new MeterGames(), mgAct);
				map.put(cInsert.getKey(), cInsert);
				
			}
		}
		
		return map;		
	}
	
	public String getKey(){
		return this.getAams_location_id()+"-"+this.getAams_code_id()+"-"+this.getAams_game_id();
		
	}
	
	@Override
	public String toString() {
		return "MeterGames [session_id=" + session_id + ", aams_location_id="
				+ aams_location_id + ", aams_code_id=" + aams_code_id
				+ ", machine_gs_id=" + machine_gs_id + ", aams_game_id="
				+ aams_game_id + ", bet=" + bet + ", win=" + win
				+ ", games_played=" + games_played + ", games_won=" + games_won
				+ ", jackpot_win=" + jackpot_win + ", jackpot_contribution="
				+ jackpot_contribution + "]";
	}

	
	public MeterGames clone(){
		MeterGames mgClone = new MeterGames();
		
		mgClone.session_id = this.session_id;
		mgClone.aams_location_id = this.aams_location_id;
		mgClone.aams_code_id = this.aams_code_id;
		mgClone.machine_gs_id = this.machine_gs_id;
		mgClone.aams_game_id = this.aams_game_id;
		mgClone.bet = this.bet;
		mgClone.win = this.win;
		mgClone.games_played = this.games_played;
		mgClone.games_won = this.games_won;
		mgClone.jackpot_win = this.jackpot_win;
		mgClone.jackpot_contribution = this.jackpot_contribution;
		mgClone.club_id=this.club_id;
	    
       return mgClone;
		
	}
	
	
	public HashMap<String , MeterGames> updateHashMap(HashMap<String, MeterGames> hash){
		
		
		if(hash.containsKey(this.getKey())){
			MeterGames mgIn = hash.get(this.getKey());			
			
			if(this.club_id!=mgIn.club_id)			
			mgIn.sum(this);
			
		}else{
			MeterGames mHash = new MeterGames();

			mHash =  this.clone();

			hash.put(mHash.getKey(), mHash);			
		}
		
		return hash;
		
	}
	
	
	
	public boolean isConsiderable(){
			return (this.bet!=0 || this.win!=0 || this.games_played!=0 || this.games_won!=0 || this.jackpot_contribution!=0 || this.jackpot_win!=0);
	}
}
