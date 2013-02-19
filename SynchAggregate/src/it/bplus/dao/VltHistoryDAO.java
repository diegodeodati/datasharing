package it.bplus.dao;

import it.bplus.primitive.VltHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;


public class VltHistoryDAO {

protected static Logger logger = Logger.getLogger(VltHistoryDAO.class);
	
	public static ArrayList<VltHistory> getAllVltHistoryEtna(Connection connectionImport,int maxId) throws SQLException{
		ArrayList<VltHistory> listVltHistoryEtna = new ArrayList<VltHistory>();
		
		logger.info("Start - Get Vlt History from Etna");
		
		String sqlImportEtnaVltHistory = "select id,code_id,location_id,ts from sc_vlt_location_history where id>"+maxId;
	
		
		Statement statementImportEtnaVltHistory = connectionImport.createStatement();		
		ResultSet resultSetImportEtnaVltHistory = statementImportEtnaVltHistory.executeQuery(sqlImportEtnaVltHistory);
		
		while (resultSetImportEtnaVltHistory.next()){
			VltHistory vltH = new VltHistory();
			vltH.setId(resultSetImportEtnaVltHistory.getLong("id"));
			vltH.setAAMS_VLT_ID(resultSetImportEtnaVltHistory.getString("code_id"));
			vltH.setLOCATION_ID(resultSetImportEtnaVltHistory.getString("location_id"));
			vltH.setMOVE_DATE(resultSetImportEtnaVltHistory.getTimestamp("ts"));
			
			
			listVltHistoryEtna.add(vltH);
			
		}
				
		logger.info("End - Get Vlt History from Etna");
		
		statementImportEtnaVltHistory.close();
		resultSetImportEtnaVltHistory.close();
		return listVltHistoryEtna;
	}

	public static int getMaxHistoryStaging(Connection connectionExport)throws SQLException{
		logger.info("Start - Get Max Id Vlt History from Staging");
			
			String sqlMaxVltHistoryStaging = "select max(id) from VLTHISTORY";
		
			
			Statement statementMaxVltHistoryStaging = connectionExport.createStatement();		
			ResultSet resultSetMaxVltHistoryStaging= statementMaxVltHistoryStaging.executeQuery(sqlMaxVltHistoryStaging);
			
			
			logger.info("End - Get Max Id Vlt History from Staging");
			
			
			
			
			if(resultSetMaxVltHistoryStaging.next()){
				int aux = resultSetMaxVltHistoryStaging.getInt(1);
				resultSetMaxVltHistoryStaging.close();
				statementMaxVltHistoryStaging.close();			
				return aux;
			}
			else{
				statementMaxVltHistoryStaging.close();
				resultSetMaxVltHistoryStaging.close();
			    return 0;
			}
		}

    public static void insertVltHistory(Connection connEtna,Connection connStaging) throws SQLException{
    	
    	int maxId = getMaxHistoryStaging(connStaging);
    	ArrayList<VltHistory> listVltHistoryEtna = getAllVltHistoryEtna(connEtna, maxId);
    	
    	connStaging.setAutoCommit(false);
    	
    	String sqlInsertVltHistory = "INSERT INTO VLTHISTORY (ID,AAMS_VLT_CODE,DATE_CHANGE,AAMS_LOCATION_CODE)"+
    	" VALUES (?,?,?,?)";
 	    PreparedStatement ps = connStaging.prepareStatement(sqlInsertVltHistory);
 	    
 	   logger.info("Start - Insert Vlt History to Staging");
    	
    	for(VltHistory vltH:listVltHistoryEtna){
    		ps.setLong(1,vltH.getId());
    		ps.setString(2, vltH.getAAMS_VLT_ID());
    		ps.setTimestamp(3,vltH.getMOVE_DATE());
    		ps.setString(4,vltH.getLOCATION_ID());
    		
    		
    		ps.executeUpdate();
    	}
    	
    	ps.close();
    	connStaging.commit();
    	
    	logger.info("End - Insert Vlt History  to Staging");
    }

	
	
}