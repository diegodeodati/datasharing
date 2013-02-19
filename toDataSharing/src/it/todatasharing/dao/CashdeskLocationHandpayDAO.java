package it.todatasharing.dao;

import it.todatasharing.primitive.CashdeskLocationHandpay;
import it.todatasharing.primitive.MeterGames;
import it.todatasharing.primitive.MeterVlt;
import it.todatasharing.primitive.Sessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class CashdeskLocationHandpayDAO {

	protected static Logger logger = Logger.getLogger(CashdeskLocationHandpayDAO.class);
	
	public static void insert(CashdeskLocationHandpay c,HashMap<String,MeterGames> hashNotConsiderableGames,HashMap<String,MeterVlt> hashNotConsiderableVlts, Connection connToDatasharing) throws SQLException{
		
		
		if(c.isConsiderable()){
			
		if(hashNotConsiderableGames.containsKey(c.getKey())){
				MeterGames mg = hashNotConsiderableGames.get(c.getKey());
				MeterGamesDAO.insertForced(mg, connToDatasharing);
				hashNotConsiderableGames.remove(c.getKey());
		}
		
		if(hashNotConsiderableVlts.containsKey(c.getKey())){
			   MeterVlt mv = hashNotConsiderableVlts.get(c.getKey());
			   MeterVltDAO.insertForced(mv, connToDatasharing);
			   hashNotConsiderableVlts.remove(c.getKey());
		}
		
		logger.debug("Start - Insert MeterCashDeskLocationHandpay");
		
		String sql = "INSERT INTO cashdesk_location_handpay (SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID," +
				     "HANDPAY_VALUE,HANDPAY_DATE) VALUE " +
				     "(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE HANDPAY_VALUE = ?";
	
		
		PreparedStatement ps = connToDatasharing.prepareStatement(sql);	
		
		ps.setLong(1,c.getSession_id());
		ps.setString(2,c.getAams_location_id());
		ps.setString(3,c.getAams_code_id());
		ps.setString(4,c.getMachine_gs_id());
		ps.setLong(5,c.getHandpay_value());
		ps.setTimestamp(6,c.getHandpay_date());
		
		ps.setLong(7,c.getHandpay_value());
		
		
		ps.executeUpdate();
		
		ps.close();		
		
		logger.debug("End - Insert MeterCashDeskLocationHandpay");
		}
		
		
	}

    public static HashMap<String ,CashdeskLocationHandpay> getAllCashdeskLocationHandpay(Sessione s, Connection connToDatasharing) throws SQLException{
    	HashMap<String ,CashdeskLocationHandpay> map = new HashMap<String, CashdeskLocationHandpay>();
    	
    	String sql = "SELECT v.SESSION_ID,v.AAMS_LOCATION_ID,v.AAMS_CODE_ID,v.MACHINE_GS_ID, " +
    			     "sum(v.HANDPAY_VALUE) HANDPAY_VALUE ,v.HANDPAY_DATE from cashdesk_location_handpay v inner join `session_log` s " +
    			     "on v.`SESSION_ID` = s.`SESSION_ID` " +
    			     "where date(s.`START_DATE`) = ? AND (time(s.`START_DATE`)<>'00:00:00' " +
    			     "or time(s.`END_DATE`)<>'23:59:59') and s.`SESSION_SUCCESS`=1 " +
    			     "GROUP BY v.`AAMS_LOCATION_ID`,v.`AAMS_CODE_ID`";

	
	  PreparedStatement ps = connToDatasharing.prepareStatement(sql);
	  
	  ps.setDate(1,new java.sql.Date(s.getSTART_DATE().getTime()));
	  
	  ResultSet rs = ps.executeQuery();
	  
	  while(rs.next()){
		  CashdeskLocationHandpay c = new CashdeskLocationHandpay();
		  c.setSession_id(rs.getLong("SESSION_ID"));
		  c.setAams_location_id(rs.getString("AAMS_LOCATION_ID"));
		  c.setAams_code_id(rs.getString("AAMS_CODE_ID"));
		  c.setMachine_gs_id(rs.getString("MACHINE_GS_ID"));
		  c.setHandpay_value(rs.getLong("HANDPAY_VALUE"));
		  c.setHandpay_date(rs.getTimestamp("HANDPAY_DATE"));
		  
		  map.put(c.getKey(), c);
	  }
    	
	  ps.close();
	  rs.close();
    	
    	return map;
    }


}
