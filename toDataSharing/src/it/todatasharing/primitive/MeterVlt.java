package it.todatasharing.primitive;

import it.todatasharing.dao.MeterVltDAO;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class MeterVlt implements Cloneable{

	private long session_id;
	private String aams_location_id;
	private String aams_code_id;
	private String machine_gs_id;
	private long total_in;
	private long total_out;
	private long ticket_in;
	private long ticket_out;
	private long coin_in;
	private long bill_in;
	private long card_in;
	private long total_prepaid_in;
	private long total_drop;
	
	protected static Logger logger = Logger.getLogger(MeterVlt.class);
	
	public MeterVlt(){}
	
	public MeterVlt(long session_id, String aams_location_id,
			String aams_code_id, String machine_gs_id, long total_in,
			long total_out, long ticket_in, long ticket_out, long coin_in,
			long bill_in, long card_in, long total_prepaid_in, long total_drop) {
		super();
		this.session_id = session_id;
		this.aams_location_id = aams_location_id;
		this.aams_code_id = aams_code_id;
		this.machine_gs_id = machine_gs_id;
		this.total_in = total_in;
		this.total_out = total_out;
		this.ticket_in = ticket_in;
		this.ticket_out = ticket_out;
		this.coin_in = coin_in;
		this.bill_in = bill_in;
		this.card_in = card_in;
		this.total_prepaid_in = total_prepaid_in;
		this.total_drop = total_drop;
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

	public long getTicket_in() {
		return ticket_in;
	}

	public void setTicket_in(long ticket_in) {
		this.ticket_in = ticket_in;
	}

	public long getTicket_out() {
		return ticket_out;
	}

	public void setTicket_out(long ticket_out) {
		this.ticket_out = ticket_out;
	}

	public long getCoin_in() {
		return coin_in;
	}

	public void setCoin_in(long coin_in) {
		this.coin_in = coin_in;
	}

	public long getBill_in() {
		return bill_in;
	}

	public void setBill_in(long bill_in) {
		this.bill_in = bill_in;
	}

	public long getCard_in() {
		return card_in;
	}

	public void setCard_in(long card_in) {
		this.card_in = card_in;
	}

	public long getTotal_prepaid_in() {
		return total_prepaid_in;
	}

	public void setTotal_prepaid_in(long total_prepaid_in) {
		this.total_prepaid_in = total_prepaid_in;
	}

	public long getTotal_drop() {
		return total_drop;
	}

	public void setTotal_drop(long total_drop) {
		this.total_drop = total_drop;
	}
	
	//proverranno entrambi dal sogei report pertanto non tutti i campi saranno valorizzati
	public void sum(MeterVlt mvAdd){
		
		this.total_in = mvAdd.total_in+this.total_in;
		this.total_out = mvAdd.total_out+this.total_out;
		this.ticket_in = mvAdd.ticket_in+this.ticket_in;
		this.ticket_out = mvAdd.ticket_out+this.ticket_out;
		this.coin_in = mvAdd.coin_in+this.coin_in;
		this.bill_in = mvAdd.bill_in+this.bill_in;
		this.card_in = mvAdd.card_in+this.card_in;
		this.total_prepaid_in = mvAdd.total_prepaid_in+this.total_prepaid_in;
		this.total_drop = (mvAdd.total_out-mvAdd.total_in)+(this.total_out-this.total_in);
		
	}
	
	//mvPrev proviene dal datasharing pertanto ha tutti i campi valorizzati 
	//mvAct proviene dal sogeireport3 pertanto dobbiamo calcolare il total_drop
	public static MeterVlt subtract(MeterVlt mvPrev, MeterVlt mvAct){
		MeterVlt dMeterVlt = new MeterVlt();
		
		dMeterVlt.session_id = mvAct.session_id;
		dMeterVlt.aams_location_id = mvAct.aams_location_id;
		dMeterVlt.aams_code_id = mvAct.aams_code_id;
		dMeterVlt.machine_gs_id = mvAct.machine_gs_id;
		dMeterVlt.total_in = mvAct.total_in-mvPrev.total_in;
		dMeterVlt.total_out = mvAct.total_out-mvPrev.total_out;
		dMeterVlt.ticket_in = mvAct.ticket_in-mvPrev.ticket_in;
		dMeterVlt.ticket_out = mvAct.ticket_out-mvPrev.ticket_out;
		dMeterVlt.coin_in = mvAct.coin_in-mvPrev.coin_in;
		dMeterVlt.bill_in = mvAct.bill_in-mvPrev.bill_in;
		dMeterVlt.card_in = mvAct.card_in-mvPrev.card_in;
		dMeterVlt.total_prepaid_in = mvAct.total_prepaid_in-mvPrev.total_prepaid_in;
		dMeterVlt.total_drop = (mvAct.total_out-mvAct.total_in)-mvPrev.total_drop;
		
		return dMeterVlt;
	}
	
	public static HashMap<String,MeterVlt> delta(HashMap<String, MeterVlt> hPrev, HashMap<String, MeterVlt> hAct){
		HashMap< String,MeterVlt> map = new HashMap<String, MeterVlt>();
		
		ArrayList<MeterVlt> list  = new ArrayList<MeterVlt>(hAct.values());
		
		for(MeterVlt mvAct: list){
			if(hPrev.containsKey(mvAct.getKey())){
				
				MeterVlt mvPrev = hPrev.get(mvAct.getKey());			
			
				MeterVlt cInsert = subtract(mvPrev, mvAct);
				
				map.put(cInsert.getKey(), cInsert);
				
			}
			else{
				MeterVlt cInsert = subtract(new MeterVlt(), mvAct);
				map.put(cInsert.getKey(), cInsert);
				
			}
		}
		
		return map;		
	}

	
	public String getKey(){
		return this.getAams_location_id()+"-"+this.getAams_code_id();
		
	}
	
	@Override
	public String toString() {
		return "MeterVlt [session_id=" + session_id + ", aams_location_id="
				+ aams_location_id + ", aams_code_id=" + aams_code_id
				+ ", machine_gs_id=" + machine_gs_id + ", total_in=" + total_in
				+ ", total_out=" + total_out + ", ticket_in=" + ticket_in
				+ ", ticket_out=" + ticket_out + ", coin_in=" + coin_in
				+ ", bill_in=" + bill_in + ", card_in=" + card_in
				+ ", total_prepaid_in=" + total_prepaid_in + ", total_drop="
				+ total_drop + "]";
	}
	
	
	public MeterVlt clone(){
		MeterVlt mvClone = new MeterVlt();
		
		mvClone.session_id = this.session_id;
		mvClone.aams_location_id = this. aams_location_id;
		mvClone.aams_code_id = this. aams_code_id;
		mvClone.machine_gs_id = this. machine_gs_id;
		mvClone.total_in = this. total_in;
		mvClone.total_out = this. total_out;
		mvClone.ticket_in = this. ticket_in;
		mvClone.ticket_out = this. ticket_out;
		mvClone.coin_in = this. coin_in;
		mvClone.bill_in = this. bill_in;
		mvClone.card_in = this. card_in;
		mvClone.total_prepaid_in = this. total_prepaid_in;
		mvClone.total_drop = this. total_drop;
	    
       return mvClone;
		
	}
	
	public HashMap<String , MeterVlt> updateHashMap(HashMap<String, MeterVlt> hash){
		
		
		if(!hash.containsKey(this.getKey())){
			
			MeterVlt mHash = new MeterVlt();

			mHash =  this.clone();

			hash.put(mHash.getKey(), mHash);	
			
		} 
		
		return hash;
		
	}
	
	
	public boolean isConsiderable(){
		return (this.total_in!=0 || this.total_out!=0 || this.ticket_in!=0 || this.ticket_out!=0 || this.coin_in!=0 || this.bill_in!=0 || this.card_in!=0 || this.total_prepaid_in!=0 || this.total_drop!=0);
	}
	
}
