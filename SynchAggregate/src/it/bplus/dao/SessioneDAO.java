package it.bplus.dao;


import it.bplus.primitive.Sessione;

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
	
	
	public static void insertSessionSyncStart(Connection connAggregate)
			throws SQLException {

		connAggregate.setAutoCommit(false);

		logger.info("Start - Insert Sessione Start SYNC to Aggregate");

		String sqlInsertvLT = "INSERT INTO BIRSSYNCLOG "
				+ "(EXIT_CODE,OPERATION_DATE_START) VALUES (?,?)";

		PreparedStatement ps = connAggregate.prepareStatement(sqlInsertvLT);

		
		ps.setInt(1,-1);

		ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));

		ps.executeUpdate();
		
		

		ps.close();
		
		connAggregate.commit();

		logger.info("End - Insert Sessione Start SYNC to Aggregate");
	}
	
	
	public static void insertSessionSyncEnd(long session_id,int exit_code, Connection connAggregate)
			throws SQLException {

		connAggregate.setAutoCommit(false);

		logger.info("Start - Update Sessione End SYNC to Aggregate");

		String sqlInsertvLT = "UPDATE BIRSSYNCLOG SET EXIT_CODE = ? , OPERATION_DATE_END=? WHERE SYNC_SESSION_ID = ?";

		PreparedStatement ps = connAggregate.prepareStatement(sqlInsertvLT);

		
		ps.setInt(1, exit_code);
		ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
		ps.setLong(3, session_id);

		ps.executeUpdate();

		
		
		ps.close();
		
		connAggregate.commit();

		logger.info("End - Update Sessione End SYNC to Aggregate");
	}
	
	public static void insertSessionImportStart(Sessione s, Connection connAggregate)
			throws SQLException {

		connAggregate.setAutoCommit(false);

		logger.info("Start - Insert Sessione Start IMPORT to Aggregate");

		String sqlInsertvLT = "INSERT INTO BIRSIMPORTLOG "
				+ "(UNIQUE_SESSION_ID,AAMS_GAME_SYSTEM_CODE,EXIT_CODE,START_DATE,END_DATE,"
				+ "OPERATION_DATE_START) VALUES (?,?,?,?,?,?)";

		PreparedStatement ps = connAggregate.prepareStatement(sqlInsertvLT);

		ps.setLong(1, s.getUNIQUE_SESSION_ID());
		ps.setLong(2, s.getAAMS_GAME_SYSTEM_CODE());
		ps.setInt(3, -1);
		ps.setTimestamp(4, new java.sql.Timestamp(s.getSTART_DATE().getTime()));
		ps.setTimestamp(5, new java.sql.Timestamp(s.getEND_DATE().getTime()));

		ps.setTimestamp(6,  new Timestamp(new java.util.Date().getTime()));

		ps.executeUpdate();

		ps.close();
		
		connAggregate.commit();


		logger.info("End - Insert Sessione Start IMPORT to Aggregate");
	}
	
	
	public static void insertSessionImportEnd(Sessione s,int exit_code, Connection connAggregate)
			throws SQLException {

		connAggregate.setAutoCommit(false);

		logger.info("Start - Update Sessione End IMPORT to Aggregate");

		String sqlInsertvLT = "UPDATE BIRSIMPORTLOG SET OPERATION_DATE_END=?,EXIT_CODE=? WHERE UNIQUE_SESSION_ID = ? AND AAMS_GAME_SYSTEM_CODE = ?";


		PreparedStatement ps = connAggregate.prepareStatement(sqlInsertvLT);

		ps.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
		ps.setInt(2, exit_code);
		ps.setLong(3, s.getUNIQUE_SESSION_ID());
		ps.setLong(4,s.getAAMS_GAME_SYSTEM_CODE());

		ps.executeUpdate();

		ps.close();
		
		connAggregate.commit();

		logger.info("End - Update Sessione End IMPORT to Aggregate");
	}
	
	
	public static long getMaxImportSessionAggregate(Connection connExport, long aams_game_system_code)throws SQLException{
		logger.info("Start - Get Max UNIQUE_SESSION_ID from Aggregate");
			
			String sqlMaxSessionAggregate = "select max(UNIQUE_SESSION_ID) from BIRSIMPORTLOG WHERE EXIT_CODE=1 AND AAMS_GAME_SYSTEM_CODE="+aams_game_system_code;
		
			
			Statement statementMaxSessionAggregate = connExport.createStatement();		
			ResultSet resultSetMaxSessionAggregate= statementMaxSessionAggregate.executeQuery(sqlMaxSessionAggregate);
			
			
			logger.info("End - Get Max UNIQUE_SESSION_ID from Aggregate");
			
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
	
	
	public static long getMaxSyncSessionAggregate(Connection connAggregate)throws SQLException{
		logger.info("Start - Get Max SYNC_SESSION_ID  from Aggregate");
			
			String sqlMaxSyncSessionAggregate = "select max(SYNC_SESSION_ID) from BIRSSYNCLOG WHERE EXIT_CODE=-1";
		
			
			Statement statementMaxSyncSessionAggregate = connAggregate.createStatement();		
			ResultSet resultSetMaxSyncSessionAggregate= statementMaxSyncSessionAggregate.executeQuery(sqlMaxSyncSessionAggregate);
			
			
			logger.info("End - Get Max SYNC_SESSION_ID from Aggregate");
			
			if(resultSetMaxSyncSessionAggregate.next()){
				long aux = resultSetMaxSyncSessionAggregate.getLong(1);
				statementMaxSyncSessionAggregate.close();			
				resultSetMaxSyncSessionAggregate.close();
				return aux;
			}
			else{
				statementMaxSyncSessionAggregate.close();
				resultSetMaxSyncSessionAggregate.close();
			    return 0;
			    
			}    
		}
	
	
	public static ArrayList<Sessione> getSessionsInspired(Connection conncectionImport,long maxSessionAggregate)throws SQLException{
		ArrayList<Sessione> arrayListSession = new ArrayList<Sessione>();
		
	    	logger.info("Start - Get SESSIONs_ID from Inspired");
			
			String sqlMaxSessionAggregate = "select SESSION_ID,START_DATE,END_DATE,SESSION_INIT_TIME,SESSION_FINISH_TIME from session_log WHERE SESSION_SUCCESS=1 AND SESSION_ID>"+maxSessionAggregate+" ORDER BY SESSION_ID";
					
			Statement statementMaxSessionAggregate = conncectionImport.createStatement();		
			ResultSet resultSetMaxSessionAggregate= statementMaxSessionAggregate.executeQuery(sqlMaxSessionAggregate);
			
			while(resultSetMaxSessionAggregate.next()){
				Sessione s = new Sessione();
				s.setUNIQUE_SESSION_ID(resultSetMaxSessionAggregate.getLong("SESSION_ID"));
				s.setAAMS_GAME_SYSTEM_CODE(1711000065);
				s.setSTART_DATE(resultSetMaxSessionAggregate.getTimestamp("START_DATE"));
				s.setEND_DATE(resultSetMaxSessionAggregate.getTimestamp("END_DATE"));
				
				arrayListSession.add(s);
			}
			
			resultSetMaxSessionAggregate.close();
			statementMaxSessionAggregate.close();
			logger.info("End - Get SESSIONs_ID from Inspired");
						
			return arrayListSession;			   
		}
	
	public static ArrayList<Sessione> getSessionsNovomatic(Connection conncectionImport,long maxSessionAggregate)throws SQLException{
		ArrayList<Sessione> arrayListSession = new ArrayList<Sessione>();
		
	    	logger.info("Start - Get SESSIONs_ID from Novomatic");
			
			String sqlMaxSessionAggregate = "select SESSION_ID,START_DATE,END_DATE,SESSION_INIT_TIME,SESSION_FINISH_TIME from session_log WHERE SESSION_SUCCESS=1 AND SESSION_ID>"+maxSessionAggregate+" ORDER BY SESSION_ID";
		
			Statement statementMaxSessionAggregate = conncectionImport.createStatement();		
			ResultSet resultSetMaxSessionAggregate= statementMaxSessionAggregate.executeQuery(sqlMaxSessionAggregate);
			
			while(resultSetMaxSessionAggregate.next()){
				Sessione s = new Sessione();
				s.setUNIQUE_SESSION_ID(resultSetMaxSessionAggregate.getLong("SESSION_ID"));
				s.setAAMS_GAME_SYSTEM_CODE(1711000045);
				s.setSTART_DATE(resultSetMaxSessionAggregate.getTimestamp("START_DATE"));
				s.setEND_DATE(resultSetMaxSessionAggregate.getTimestamp("END_DATE"));
				
				arrayListSession.add(s);
			}
			
			resultSetMaxSessionAggregate.close();
			statementMaxSessionAggregate.close();
			logger.info("End - Get SESSIONs_ID from Novomatic");
						
			return arrayListSession;			   
		}
	
}
