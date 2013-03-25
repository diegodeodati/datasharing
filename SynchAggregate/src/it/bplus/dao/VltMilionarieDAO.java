package it.bplus.dao;

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
	
	public static ArrayList<VltMilionarie> getAllVltMilionarieEtna(Connection connectionImport,int maxId) throws SQLException{
		ArrayList<VltMilionarie> listVltMilionarieEtna = new ArrayList<VltMilionarie>();
		
		logger.info("Start - Get Vlt Milionarie from Etna");
		
		String sql = "select id,code_id,data,location_id,bet,win,bet_reale,win_reale from sc_vlt_milionarie where id > "+maxId+" limit 100";
	
		
		Statement st = connectionImport.createStatement();		
		ResultSet rs = st.executeQuery(sql);
		
		while(rs.next()){
		   VltMilionarie vltM = new VltMilionarie();	
		   vltM.setId(rs.getLong("id"));
		   vltM.setAams_vlt_id(rs.getString("code_id"));
		   vltM.setData(new java.sql.Date(rs.getDate("data").getTime()));
		   vltM.setAams_location_id(rs.getString("location_id"));
		   vltM.setBet(rs.getLong("bet"));
		   vltM.setWin(rs.getLong("win"));
		   vltM.setBet_reale(rs.getLong("bet_reale"));
		   vltM.setWin_reale(rs.getLong("win_reale"));
			
		listVltMilionarieEtna.add(vltM);
			
		}
				
		logger.info("End - Get Vlt Milionarie from Etna");
		
		st.close();
		rs.close();
		return listVltMilionarieEtna;
	}

	public static int getMaxVltMilionarieStaging(Connection conncectionExport)throws SQLException{
	logger.info("Start - Get Max Id Vlt Milionarie from Staging");
		
		String sql= "select max(id) from birsvltmilionarie";
	
		
		Statement st = conncectionExport.createStatement();		
		ResultSet rs= st.executeQuery(sql);
		
		
		logger.info("End - Get Max Id Seicento from Staging");
		
		if(rs.next()){
			int aux = rs.getInt(1);
			st.close();			
			rs.close();
			return aux;
		}
		else{
			st.close();
			rs.close();
		    return 0;
		    
		}    
	}

    public static void insertVltMilionarie(Connection connEtna,Connection connStaging) throws SQLException{
    	
    	int maxId = getMaxVltMilionarieStaging(connStaging);
    	ArrayList<VltMilionarie> listVltMilionarieEtna = getAllVltMilionarieEtna(connEtna, maxId);
    	
    	connStaging.setAutoCommit(false);
    	
    	String sqlInsertSeicento = "INSERT IGNORE INTO BIRSVLTMILIONARIE (ID,AAMS_VLT_CODE,DATA,AAMS_LOCATION_CODE,BET,WIN,BET_REALE,WIN_REALE)"+
    	" VALUES (?,?,?,?,?,?,?,?)";
 	    PreparedStatement ps = connStaging.prepareStatement(sqlInsertSeicento);
 	    
 	   logger.info("Start - Insert Vlt Milionarie to Staging");
    	
    	for(VltMilionarie vltM:listVltMilionarieEtna){
    		ps.setLong(1,vltM.getId());
    		ps.setString(2,vltM.getAams_vlt_id());
    		ps.setDate(3,new java.sql.Date(vltM.getData().getTime()));
    		ps.setString(4,vltM.getAams_location_id());
    		ps.setLong(5,vltM.getBet());
    		ps.setLong(6,vltM.getWin());
    		ps.setLong(7,vltM.getBet_reale());
    		ps.setLong(8,vltM.getWin_reale());
    		
    		
    		ps.executeUpdate();
    	}
    	
    	ps.close();
    	connStaging.commit();
    	
    	logger.info("End - Insert Vlt Milionarie to Staging");
    }
	
}
