package it.todatasharing.dao;

import it.todatasharing.primitive.MeterGames;
import it.todatasharing.primitive.MeterVlt;
import it.todatasharing.primitive.Sessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class MeterVltDAO {

	protected static Logger logger = Logger.getLogger(MeterVltDAO.class);
	
	public static void insert(MeterVlt m,HashMap<String,MeterGames> hashNotConsiderableGames,HashMap<String,MeterVlt> hashNotConsiderableVlts, Connection connToDatasharing) throws SQLException{
		
		
		if(m.isConsiderable()){
			
		if(hashNotConsiderableGames.containsKey(m.getKey())){
			MeterGames mg = hashNotConsiderableGames.get(m.getKey());
			MeterGamesDAO.insertForced(mg, connToDatasharing);
			hashNotConsiderableGames.remove(m.getKey());			
		}	
			
		logger.debug("Start - Insert MeterVlt");
		
		String sql = "INSERT INTO meters_vlt (SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID," +
				     "TOTAL_IN,TOTAL_OUT,TICKET_IN,TICKET_OUT,COIN_IN,BILL_IN,CARD_IN,TOTAL_PREPAID_IN,TOTAL_DROP) VALUE " +
				     "(?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE TOTAL_IN=TOTAL_IN+?,TOTAL_OUT=TOTAL_OUT+?," +
				     "TICKET_IN=TICKET_IN+?,TICKET_OUT=TICKET_OUT+?,COIN_IN=COIN_IN+?,BILL_IN=BILL_IN+?,CARD_IN=CARD_IN+?," +
				     "TOTAL_PREPAID_IN=TOTAL_PREPAID_IN+?,TOTAL_DROP=TOTAL_DROP+?";
	
	
		PreparedStatement ps = connToDatasharing.prepareStatement(sql);	
		
		ps.setLong(1,m.getSession_id());
		ps.setString(2,m.getAams_location_id());
		ps.setString(3,m.getAams_code_id());
		ps.setString(4,m.getMachine_gs_id());
		ps.setLong(5,m.getTotal_in());
		ps.setLong(6,m.getTotal_out());
		ps.setLong(7,m.getTicket_in());
		ps.setLong(8,m.getTicket_out());
		ps.setLong(9,m.getCoin_in());
		ps.setLong(10,m.getBill_in());
		ps.setLong(11,m.getCard_in());
		ps.setLong(12,m.getTotal_prepaid_in());
		ps.setLong(13,m.getTotal_drop());
		
		ps.setLong(14,m.getTotal_in());
		ps.setLong(15,m.getTotal_out());
		ps.setLong(16,m.getTicket_in());
		ps.setLong(17,m.getTicket_out());
		ps.setLong(18,m.getCoin_in());
		ps.setLong(19,m.getBill_in());
		ps.setLong(20,m.getCard_in());
		ps.setLong(21,m.getTotal_prepaid_in());
		ps.setLong(22,m.getTotal_drop());
		
		
		
		ps.executeUpdate();
		
		ps.close();
		
		
		logger.debug("End - Insert MeterVlt");
		}
		else{
			
			   if(!hashNotConsiderableVlts.containsKey(m.getAams_location_id()+"-"+m.getAams_code_id()))
				hashNotConsiderableVlts.put(m.getAams_location_id()+"-"+m.getAams_code_id(), m);     	
				
		   
		}
		
		
	}
	
	
	public static void insertForced(MeterVlt m, Connection connToDatasharing) throws SQLException{
		
		logger.debug("Start - Insert MeterVlt");
		
		String sql = "INSERT INTO meters_vlt (SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID," +
				     "TOTAL_IN,TOTAL_OUT,TICKET_IN,TICKET_OUT,COIN_IN,BILL_IN,CARD_IN,TOTAL_PREPAID_IN,TOTAL_DROP) VALUE " +
				     "(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
		
		PreparedStatement ps = connToDatasharing.prepareStatement(sql);	
		
		ps.setLong(1,m.getSession_id());
		ps.setString(2,m.getAams_location_id());
		ps.setString(3,m.getAams_code_id());
		ps.setString(4,m.getMachine_gs_id());
		ps.setLong(5,m.getTotal_in());
		ps.setLong(6,m.getTotal_out());
		ps.setLong(7,m.getTicket_in());
		ps.setLong(8,m.getTicket_out());
		ps.setLong(9,m.getCoin_in());
		ps.setLong(10,m.getBill_in());
		ps.setLong(11,m.getCard_in());
		ps.setLong(12,m.getTotal_prepaid_in());
		ps.setLong(13,m.getTotal_drop());
		
		
		ps.executeUpdate();
		
		ps.close();
		
		
		logger.debug("End - Insert MeterVlt");
		
		
	}
	
	public static  HashMap<String ,MeterVlt> getAllMeterVlt(Sessione s, Connection connToDatasharing) throws SQLException{
    	HashMap<String ,MeterVlt> map = new HashMap<String, MeterVlt>();
    	
    	String sql = "SELECT v.SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID," +
    				 "SUM(TOTAL_IN) TOTAL_IN,SUM(TOTAL_OUT) TOTAL_OUT, SUM(TICKET_IN) TICKET_IN, SUM(TICKET_OUT) TICKET_OUT,SUM(COIN_IN) COIN_IN,SUM(BILL_IN) BILL_IN," +
    				 "SUM(CARD_IN) CARD_IN, SUM(TOTAL_PREPAID_IN) TOTAL_PREPAID_IN,SUM(TOTAL_DROP) TOTAL_DROP from meters_vlt v inner join `session_log` s " +
    				 "on v.`SESSION_ID` = s.`SESSION_ID` " +
    				 "where date(s.`START_DATE`) = ? AND (time(s.`START_DATE`)<>'00:00:00' " +
    				 "or time(s.`END_DATE`)<>'23:59:59' ) and s.`SESSION_SUCCESS`=1 " +
    				 "GROUP BY v.`AAMS_LOCATION_ID`,v.`AAMS_CODE_ID`";

	
	  PreparedStatement ps = connToDatasharing.prepareStatement(sql);
	  
	  ps.setDate(1,new java.sql.Date(s.getSTART_DATE().getTime()));
	  
	  //devo provare a stampare questa data di sopra
	  
	  ResultSet rs = ps.executeQuery();
	  
	  while(rs.next()){
		  MeterVlt mv = new MeterVlt();
		  mv.setSession_id(rs.getLong("SESSION_ID"));
		  mv.setAams_location_id(rs.getString("AAMS_LOCATION_ID"));
		  mv.setAams_code_id(rs.getString("AAMS_CODE_ID"));
		  mv.setMachine_gs_id(rs.getString("MACHINE_GS_ID"));
		  mv.setTotal_in(rs.getLong("TOTAL_IN"));
		  mv.setTotal_out(rs.getLong("TOTAL_OUT"));
		  mv.setTicket_in(rs.getLong("TICKET_IN"));
		  mv.setTicket_out(rs.getLong("TICKET_OUT"));
		  mv.setCoin_in(rs.getLong("COIN_IN"));
		  mv.setBill_in(rs.getLong("BILL_IN"));
		  mv.setCard_in(rs.getLong("CARD_IN"));
		  mv.setTotal_prepaid_in(rs.getLong("TOTAL_PREPAID_IN"));
		  mv.setTotal_drop(rs.getLong("TOTAL_DROP"));
		  
		  map.put(mv.getKey(),mv);
	  }
    	
	  ps.close();
    	rs.close();
    	
	  
    	return map;
    }
}
