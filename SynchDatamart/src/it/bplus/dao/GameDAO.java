package it.bplus.dao;

import it.bplus.primitive.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class GameDAO {

	protected static Logger logger = Logger.getLogger(GameDAO.class);
	

	
    public static ArrayList<Game> getAllGameEtna(Connection connectionImport)  throws SQLException{
		
		logger.info("Start - Get Game from Etna");
		Statement statementImport = connectionImport.createStatement();
		ArrayList<Game> listGameEtna = new ArrayList<Game>();
		
		String sqlImportEtnaVlt = "select id,description,rtp,id_game_gs,gs_id from sc_game";
		ResultSet resultSetImportGame = statementImport.executeQuery(sqlImportEtnaVlt);
		
		while(resultSetImportGame.next()){
			Game g = new Game();
			g.setAAMS_GAME_ID(resultSetImportGame.getLong("id"));
			g.setNAME(resultSetImportGame.getString("description"));
			g.setRtp(resultSetImportGame.getDouble("rtp"));
			g.setGS_GAME_ID(resultSetImportGame.getLong("id_game_gs"));		
			g.setGS_ID(resultSetImportGame.getLong("gs_id"));
			listGameEtna.add(g);
		}
		
		logger.info("End - Get Game from Etna");
		
		resultSetImportGame.close();
		statementImport.close();		
		return listGameEtna;
		
	}

    
    public static HashMap<Long, Game> getAllGameStaging(Connection connectionImport) throws SQLException  {
		   HashMap<Long, Game> hashGameStaging = new HashMap<Long, Game>();
			
		   logger.info("Start - Get Game from Staging");
			
			String sqlImportStagingGame = "select NAME,GS_GAMES_CODE,RTP_THEORICAL,AAMS_GAME_SYSTEM_CODE,AAMS_GAME_CODE from BIRSGAMES";
		
			
			Statement statementImportStagingGame = connectionImport.createStatement();		
			ResultSet resultSetImportStagingGame = statementImportStagingGame.executeQuery(sqlImportStagingGame);
		
			while(resultSetImportStagingGame.next()){
				Game g = new Game();
				g.setGS_GAME_ID(resultSetImportStagingGame.getLong("GS_GAMES_CODE"));
				g.setNAME(resultSetImportStagingGame.getString("NAME"));
				g.setGS_ID(resultSetImportStagingGame.getLong("AAMS_GAME_SYSTEM_CODE"));
				g.setAAMS_GAME_ID(resultSetImportStagingGame.getLong("AAMS_GAME_CODE"));
				g.setRtp(resultSetImportStagingGame.getDouble("RTP_THEORICAL"));
				hashGameStaging.put(g.getAAMS_GAME_ID(),g);		
			}
			
			logger.info("End - Get Game from Staging");
			
			statementImportStagingGame.close();
			resultSetImportStagingGame.close();			
			
		return hashGameStaging;
	}

    public static void insertGame(Connection connEtna,Connection connStaging) throws SQLException{
		 
		   ArrayList<Game> listGameEtna = getAllGameEtna(connEtna);
		   HashMap<Long, Game> hashGameStaging = getAllGameStaging(connStaging);
		   
		   connStaging.setAutoCommit(true);
		   
		   
		   
		   
		   for(Game actGame:listGameEtna){
			   if(!hashGameStaging.containsKey(actGame.getAAMS_GAME_ID()))
			   {
				   String sqlInsertGame = "INSERT INTO BIRSGAMES (NAME,GS_GAMES_CODE,RTP_THEORICAL,AAMS_GAME_SYSTEM_CODE,AAMS_GAME_CODE)"+
				   " VALUES (?,?,?,?,?)";
				   PreparedStatement ps = connStaging.prepareStatement(sqlInsertGame);
				   
				   logger.info("INSERT GAME: "+actGame.getAAMS_GAME_ID()+" NAME: "+actGame.getNAME());
				   
				   ps.setString(1,actGame.getNAME());
				   ps.setLong(2,actGame.getGS_GAME_ID());
				   ps.setDouble(3,actGame.getRtp());
				   ps.setLong(4, actGame.getGS_ID());
				   ps.setLong(5, actGame.getAAMS_GAME_ID());
				   
				   ps.executeUpdate();
				   
				   ps.close();
			   }
			   else{
				   Game gameChanged = hashGameStaging.get(actGame.getAAMS_GAME_ID());
				   
				   if(gameChanged.getRtp()!=actGame.getRtp()){
					   String sqlInsertVlt = "UPDATE BIRSGAMES SET RPT=? WHERE AAMS_GAME_CODE=?";
					   PreparedStatement ps = connStaging.prepareStatement(sqlInsertVlt);
					   
					   logger.info("UPDATE GAME "+actGame.getAAMS_GAME_ID());
					   
					   ps.setDouble(1,actGame.getRtp());
					   ps.setLong(2,actGame.getAAMS_GAME_ID());
					   
					   
					   ps.executeUpdate();
					   
					   ps.close();
				   }		
			   }
		   }  
			   
		   connStaging.setAutoCommit(false);
		   
	   }
}
