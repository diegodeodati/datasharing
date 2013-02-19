package it.bplus.dao;

import it.bplus.primitive.Sessione;
import it.bplus.primitive.VltMilionarie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;


public class VltMilionarieDAO {

protected static Logger logger = Logger.getLogger(VltMilionarieDAO.class);

	public static long[] getAamsGameCodeFromVltMilionarie(Connection connectionExport,VltMilionarie v) throws SQLException{
		
		long[] arr = new long[3];
		
		String sql = "select m.BET,m.WIN,m.AAMS_GAME_CODE from meter_games m where m.bet=(select MAX(BET) from meter_games m"+
				     " WHERE date(m.DATA) = date(?) and m.AAMS_VLT_CODE like ? and m.AAMS_LOCATION_CODE like ?) limit 1";
		
		PreparedStatement ps = connectionExport.prepareStatement(sql);
		ps.setDate(1,new java.sql.Date (v.getData().getTime()));
		ps.setString(2,v.getAams_vlt_id());
		ps.setString(3, v.getAams_location_id());
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			arr[0] = rs.getLong("BET");
			arr[1] = rs.getLong("WIN");
			arr[2] = rs.getLong("AAMS_GAME_CODE");
		}
		
		ps.close();
		rs.close();
		
		return arr;
	}
	
	public static ArrayList<VltMilionarie> getAllVltMilionarie(Connection connectionExport) throws SQLException{
		ArrayList<VltMilionarie> listVltMilionarie= new ArrayList<VltMilionarie>();
		
		logger.info("Start - Get Vlt Milionarie");
		
		String sql = "select aams_vlt_code,data,aams_location_code,bet,win,bet_reale,win_reale from aggregate.birsvltmilionarie where processed = 0";
	
		
		Statement st = connectionExport.createStatement();		
		ResultSet rs = st.executeQuery(sql);
		
		while(rs.next()){
		   VltMilionarie vltM = new VltMilionarie();
		   vltM.setAams_vlt_id(rs.getString("aams_vlt_code"));
		   vltM.setData(new java.sql.Date(rs.getDate("data").getTime()));
		   vltM.setAams_location_id(rs.getString("aams_location_code"));
		   vltM.setBet(rs.getLong("bet"));
		   vltM.setWin(rs.getLong("win"));
		   vltM.setBet_reale(rs.getLong("bet_reale"));
		   vltM.setWin_reale(rs.getLong("win_reale"));
			
		listVltMilionarie.add(vltM);
			
		}
				
		logger.info("End - Get Vlt Milionarie");
		
		st.close();
		rs.close();
		return listVltMilionarie;
	}

	public static ArrayList<VltMilionarie> getAllVltMilionarieDay(Connection connectionExport,Sessione s) throws SQLException{
		ArrayList<VltMilionarie> listVltMilionarie= new ArrayList<VltMilionarie>();
		
		logger.info("Start - Get Vlt Milionarie By Day");
		
		String sql = "select aams_vlt_code,data,aams_location_code,bet,win,bet_reale,win_reale from aggregate.birsvltmilionarie where data = date(?)";
	
		
		PreparedStatement ps = connectionExport.prepareStatement(sql);
		ps.setDate(1,new java.sql.Date(s.getSTART_DATE().getTime()));
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
		   VltMilionarie vltM = new VltMilionarie();	
		   vltM.setAams_vlt_id(rs.getString("aams_vlt_code"));
		   vltM.setData(new java.sql.Date(rs.getDate("data").getTime()));
		   vltM.setAams_location_id(rs.getString("aams_location_code"));
		   vltM.setBet(rs.getLong("bet"));
		   vltM.setWin(rs.getLong("win"));
		   vltM.setBet_reale(rs.getLong("bet_reale"));
		   vltM.setWin_reale(rs.getLong("win_reale"));
		   
		listVltMilionarie.add(vltM);
			
		}
				
		logger.info("End - Get Vlt Milionarie By Day");
		
		ps.close();
		rs.close();
		return listVltMilionarie;
	}
	
	public static void processedVltMilionarie(Connection connectionExport,VltMilionarie vMil) throws SQLException{
		
		String sql = "UPDATE aggregate.birsvltmilionarie SET processed = 1 where aams_vlt_code like ? and aams_location_code like ? and data = date(?)";

		PreparedStatement ps = connectionExport.prepareStatement(sql);
		
		ps.setString(1,vMil.getAams_vlt_id());
		ps.setString(2,vMil.getAams_location_id());
		ps.setDate(3,new java.sql.Date(vMil.getData().getTime()));
			
		ps.executeUpdate();
	}
	
}
