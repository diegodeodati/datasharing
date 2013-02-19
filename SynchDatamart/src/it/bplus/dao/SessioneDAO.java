package it.bplus.dao;


import it.bplus.primitive.Sessione;
import it.bplus.util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SessioneDAO {

	protected static Logger logger = Logger.getLogger(SessioneDAO.class);
	
	
	
	public static void insertSessionImportStart(Sessione s, Connection connDatamart)
			throws SQLException {

		//se trova una sessione reimportata che non sia un pregresso pertanto appartenente ad una mezzora gia importata
		//allora imposto a last imported = 0 che fara scattare i trigger per la scorporazione della vecchia sessione
		if(DateUtils.isRealTime(s.getSTART_DATE(),s.getEND_DATE()))
		setNotLastImportedIfExist(s,connDatamart);
		
		//connDatamart.setAutoCommit(false);

		logger.info("Start - Insert Sessione Start IMPORT to Datamart");

		String sqlInsertvLT = "INSERT INTO IMPORTLOG "
				+ "(UNIQUE_SESSION_ID,AAMS_GAME_SYSTEM_CODE,EXIT_CODE,START_DATE,END_DATE,"
				+ "OPERATION_DATE_START) VALUES (?,?,?,?,?,?)";

		PreparedStatement ps = connDatamart.prepareStatement(sqlInsertvLT);

		ps.setLong(1, s.getUNIQUE_SESSION_ID());
		ps.setLong(2, s.getAAMS_GAME_SYSTEM_CODE());
		ps.setInt(3, 0);
		ps.setTimestamp(4, new java.sql.Timestamp(s.getSTART_DATE().getTime()));
		ps.setTimestamp(5, new java.sql.Timestamp(s.getEND_DATE().getTime()));

		ps.setTimestamp(6,  new Timestamp(new java.util.Date().getTime()));

		ps.executeUpdate();

		ps.close();
		
		//connDatamart.commit();


		logger.info("End - Insert Sessione Start IMPORT to Datamart");
	}
	
	
	public static void insertSessionImportEnd(Sessione s,int exit_code, Connection connDatamart)
			throws SQLException {
		
		//connDatamart.setAutoCommit(false);

		logger.info("Start - Update Sessione End IMPORT to Datamart");

		String sqlUpdate = "UPDATE IMPORTLOG SET OPERATION_DATE_END=?,EXIT_CODE=? WHERE UNIQUE_SESSION_ID = ? AND AAMS_GAME_SYSTEM_CODE = ?";


		PreparedStatement ps = connDatamart.prepareStatement(sqlUpdate);

		ps.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
		ps.setInt(2, exit_code);
		ps.setLong(3, s.getUNIQUE_SESSION_ID());
		ps.setLong(4,s.getAAMS_GAME_SYSTEM_CODE());

		ps.executeUpdate();

		ps.close();
		
		//connDatamart.commit();

		logger.info("End - Update Sessione End IMPORT to Datamart");
	}
	
	
	public static void setNotLastImportedIfExist(Sessione s, Connection connDatamart)
			throws SQLException {

		//connDatamart.setAutoCommit(false);

		logger.info("Start - Set Not Last Imported to Datamart");
		
		String sqlCheck = "SELECT UNIQUE_SESSION_ID FROM IMPORTLOG WHERE START_DATE = ? and END_DATE = ?  AND AAMS_GAME_SYSTEM_CODE = ? AND LAST_IMPORTED=1";
		
		PreparedStatement psCheck = connDatamart.prepareStatement(sqlCheck);
		int session_id=0;		
		
		
		psCheck.setTimestamp(1, new Timestamp(s.getSTART_DATE().getTime()));
		psCheck.setTimestamp(2, new Timestamp(s.getEND_DATE().getTime()));
		psCheck.setLong(3,s.getAAMS_GAME_SYSTEM_CODE());
		
		ResultSet rs = psCheck.executeQuery();
		
		String sqlUpdate = "UPDATE IMPORTLOG SET LAST_IMPORTED=0 WHERE UNIQUE_SESSION_ID = ? AND AAMS_GAME_SYSTEM_CODE = ?";
		PreparedStatement psUpdate = connDatamart.prepareStatement(sqlUpdate);
		
		while(rs.next()){
		session_id=rs.getInt("UNIQUE_SESSION_ID");


		logger.info("SESSIONE PORTATA A ZERO:"+session_id);

		if(session_id!=0){		

		psUpdate.setLong(1, session_id);
		psUpdate.setLong(2,s.getAAMS_GAME_SYSTEM_CODE());

		psUpdate.executeUpdate();

		MeterDAO.updateMeterSetLastImported(session_id, s.getAAMS_GAME_SYSTEM_CODE(), connDatamart);
		}
		
		}
		rs.close();
		psCheck.close();
		psUpdate.close();
		
		//connDatamart.commit();

		logger.info("End - Set Not Last Imported to Datamart");
	}
	
	
	public static long getMaxImportSessionDatamart(Connection connExport, long aams_game_system_code)throws SQLException{
		logger.info("Start - Get Max UNIQUE_SESSION_ID from Datamart");
			
			String sqlMaxSessionAggregate = "select max(UNIQUE_SESSION_ID) from IMPORTLOG WHERE EXIT_CODE=1 AND AAMS_GAME_SYSTEM_CODE="+aams_game_system_code;
		
			
			Statement statementMaxSessionAggregate = connExport.createStatement();		
			ResultSet resultSetMaxSessionAggregate= statementMaxSessionAggregate.executeQuery(sqlMaxSessionAggregate);
			
			
			logger.info("End - Get Max UNIQUE_SESSION_ID from Datamart");
			
			if(resultSetMaxSessionAggregate.next()){
				long aux = resultSetMaxSessionAggregate.getLong(1);
				statementMaxSessionAggregate.close();			
				resultSetMaxSessionAggregate.close();
				return aux;
			}
			else{
				statementMaxSessionAggregate.close();
				resultSetMaxSessionAggregate.close();
			    return 0;
			    
			}    
		}
	
	
	
	
	public static ArrayList<Sessione> getSessionsInspired(Connection conncectionImport,long maxSessionDatamart)throws SQLException{
		ArrayList<Sessione> arrayListSession = new ArrayList<Sessione>();
		
	    	logger.info("Start - Get SESSIONs_ID from Aggregate Inspired");
			
			String sqlMaxSessionAggregate = "select UNIQUE_SESSION_ID,START_DATE,END_DATE,OPERATION_DATE_START,OPERATION_DATE_END from birsimportlog WHERE AAMS_GAME_SYSTEM_CODE=1711000065 AND EXIT_CODE=1 AND UNIQUE_SESSION_ID>"+maxSessionDatamart+" ORDER BY UNIQUE_SESSION_ID LIMIT 8";
					
			Statement statementMaxSessionAggregate = conncectionImport.createStatement();		
			ResultSet resultSetMaxSessionAggregate= statementMaxSessionAggregate.executeQuery(sqlMaxSessionAggregate);
			
			while(resultSetMaxSessionAggregate.next()){
				Sessione s = new Sessione();
				s.setUNIQUE_SESSION_ID(resultSetMaxSessionAggregate.getLong("UNIQUE_SESSION_ID"));
				s.setAAMS_GAME_SYSTEM_CODE(1711000065);
				s.setSTART_DATE(resultSetMaxSessionAggregate.getTimestamp("START_DATE"));
				s.setEND_DATE(resultSetMaxSessionAggregate.getTimestamp("END_DATE"));
				
				arrayListSession.add(s);
			}
						
			logger.info("End - Get SESSIONs_ID from Aggregate Inspired");
						
			return arrayListSession;			   
		}
	
	public static ArrayList<Sessione> getSessionsNovomatic(Connection conncectionImport,long maxSessionDatamart)throws SQLException{
		ArrayList<Sessione> arrayListSession = new ArrayList<Sessione>();
		
	    	logger.info("Start - Get SESSIONs_ID from Aggregate Novomatic");
			
	    	String sqlMaxSessionAggregate = "select UNIQUE_SESSION_ID,START_DATE,END_DATE,OPERATION_DATE_START,OPERATION_DATE_END from birsimportlog WHERE AAMS_GAME_SYSTEM_CODE=1711000045 AND EXIT_CODE=1 AND UNIQUE_SESSION_ID>"+maxSessionDatamart+" ORDER BY UNIQUE_SESSION_ID LIMIT 8";
			
			Statement statementMaxSessionAggregate = conncectionImport.createStatement();		
			ResultSet resultSetMaxSessionAggregate= statementMaxSessionAggregate.executeQuery(sqlMaxSessionAggregate);
			
			while(resultSetMaxSessionAggregate.next()){
				Sessione s = new Sessione();
				s.setUNIQUE_SESSION_ID(resultSetMaxSessionAggregate.getLong("UNIQUE_SESSION_ID"));
				s.setAAMS_GAME_SYSTEM_CODE(1711000045);
				s.setSTART_DATE(resultSetMaxSessionAggregate.getTimestamp("START_DATE"));
				s.setEND_DATE(resultSetMaxSessionAggregate.getTimestamp("END_DATE"));
				
				arrayListSession.add(s);
			}
			
			logger.info("End - Get SESSIONs_ID from Aggregate Novomatic");
						
			return arrayListSession;			   
		}
	
}
