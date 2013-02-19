package it.bplus.dao;

import it.bplus.primitive.VltGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class VltGameDAO {

	protected static Logger logger = Logger.getLogger(VltGameDAO.class);
	
	public static ArrayList<VltGame> getAllVltGameEtna(Connection connectionImport)  throws SQLException{
		
		logger.info("Start - Get Vlt Game from Etna");
		Statement statementImport = connectionImport.createStatement();
		ArrayList<VltGame> listVltGameEtna = new ArrayList<VltGame>();
		
		String sqlImportEtnaVltGame = "select code_id, game_id,enabled from sc_vlt_game group by code_id, game_id";
		ResultSet resultSetImportVltGame = statementImport.executeQuery(sqlImportEtnaVltGame);
		
		while(resultSetImportVltGame.next()){
			VltGame vltGame = new VltGame();
			vltGame.setAAMS_VLT_ID(resultSetImportVltGame.getString("code_id"));
			vltGame.setAAMS_GAME_ID(resultSetImportVltGame.getLong("game_id"));
			vltGame.setENABLE(resultSetImportVltGame.getBoolean("enabled"));
			
			listVltGameEtna.add(vltGame);
		}
		
		logger.info("End - Get Vlt Game from Etna");
		
		resultSetImportVltGame.close();
		statementImport.close();		
		return listVltGameEtna;
		
	}

	public static HashMap<String, VltGame> getAllVltGameAggregate(Connection connectionImport) throws SQLException  {
		   HashMap<String, VltGame> hashVltGameStaging = new HashMap<String, VltGame>();
			
		   logger.info("Start - Get Vlt Game from Aggregate");
		   
			
			String sqlImportStagingVltGame = "select aams_vlt_id,aams_game_id,enabled from vltgame";
		
			
			Statement statementImportStagingVltGame = connectionImport.createStatement();		
			ResultSet resultSetImportStagingVltGame = statementImportStagingVltGame.executeQuery(sqlImportStagingVltGame);
		
			while(resultSetImportStagingVltGame.next()){
				VltGame vltGame = new VltGame();
				vltGame.setAAMS_VLT_ID(resultSetImportStagingVltGame.getString("aams_vlt_id").toUpperCase());
				vltGame.setAAMS_GAME_ID(resultSetImportStagingVltGame.getLong("aams_game_id"));
				vltGame.setENABLE(resultSetImportStagingVltGame.getBoolean("enabled"));
				
				hashVltGameStaging.put(vltGame.getAAMS_VLT_ID()+"-"+vltGame.getAAMS_GAME_ID(), vltGame);		
			}
			
			logger.info("End - Get Vlt Game from Aggregate");
			
			statementImportStagingVltGame.close();
			resultSetImportStagingVltGame.close();			
			
		return hashVltGameStaging;
	}

	
	public static void insertVltGame(Connection connEtna,Connection connStaging) throws SQLException{
		 
		   ArrayList<VltGame> listVltGameEtna = getAllVltGameEtna(connEtna);
		   HashMap<String, VltGame> hashVltGameStaging = getAllVltGameAggregate(connStaging);
		   
		   connStaging.setAutoCommit(true);
	   
		   logger.info("Start - Insert Vlt Game to Aggregate");
		   
		   for(VltGame actVLtGAME:listVltGameEtna){
			   
			   if(!hashVltGameStaging.containsKey(actVLtGAME.getAAMS_VLT_ID().toUpperCase()+"-"+actVLtGAME.getAAMS_GAME_ID()))
			   {
				   
				   
				   String sqlInsertvLT = "INSERT INTO vltgame (aams_vlt_id,aams_game_id,enabled) VALUES (?,?,?)";
				   PreparedStatement ps = connStaging.prepareStatement(sqlInsertvLT);
				   
				   ps.setString(1,actVLtGAME.getAAMS_VLT_ID().toUpperCase());
				   ps.setLong(2,actVLtGAME.getAAMS_GAME_ID());
				   ps.setBoolean(3,actVLtGAME.isENABLE());
				   
				   ps.executeUpdate();
				   
				   ps.close();
			   }
		   }  
			   
		   connStaging.setAutoCommit(false);
		   
		   logger.info("End - Insert Vlt to Aggregate");
		   
	   }
}
