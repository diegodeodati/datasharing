package it.bplus.dao;

import it.bplus.primitive.SistemaGiocoDim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class SistemaGiocoDimDAO {

	protected static Logger logger = Logger.getLogger(SistemaGiocoDimDAO.class);
	
	
	
   public static HashMap<String, SistemaGiocoDim> getAllSistemaGiocoDimDatamart(Connection connExport) throws SQLException{
	   HashMap<String, SistemaGiocoDim> hashSistemaGiocoDim  = new HashMap<String, SistemaGiocoDim>();
		
	   logger.info("Start - Get SistemaGioco Dim from Datamart");
	   
	   		
		String sqlImportSistemaGiocoDim = "select ID,AAMS_GAMESYSTEM_CODE,AAMS_LOCATION_CODE,AAMS_VLT_CODE,GS_VLT_CODE,AAMS_GAME_CODE from sistemagiocodim";
	
		
		Statement statementImportSistemaGiocoDim = connExport.createStatement();		
		ResultSet resultSetImportSistemaGiocoDim = statementImportSistemaGiocoDim.executeQuery(sqlImportSistemaGiocoDim);
		
		while (resultSetImportSistemaGiocoDim.next()){
			SistemaGiocoDim sysdim = new SistemaGiocoDim();
			sysdim.setId(resultSetImportSistemaGiocoDim.getLong("ID"));
			sysdim.setAAMS_GAMESYSTEM_CODE(resultSetImportSistemaGiocoDim.getLong("AAMS_GAMESYSTEM_CODE"));
			sysdim.setAAMS_LOCATION_CODE(resultSetImportSistemaGiocoDim.getString("AAMS_LOCATION_CODE"));
			sysdim.setAAMS_VLT_CODE(resultSetImportSistemaGiocoDim.getString("AAMS_VLT_CODE"));
			sysdim.setGS_VLT_CODE(resultSetImportSistemaGiocoDim.getString("GS_VLT_CODE"));
			sysdim.setAAMS_GAME_CODE(resultSetImportSistemaGiocoDim.getLong("AAMS_GAME_CODE"));
			
			hashSistemaGiocoDim.put(sysdim.getAAMS_LOCATION_CODE()+"-"+sysdim.getAAMS_VLT_CODE()+"-"+sysdim.getAAMS_GAME_CODE(),sysdim);
		}
		
		 logger.info("End - Get SistemaGioco Dim from Datamart");
		
		 statementImportSistemaGiocoDim.close();
		 resultSetImportSistemaGiocoDim.close();
		return hashSistemaGiocoDim;
	   
   }
   
   
	public static void insertSistemaGiocoDim(
			HashMap<String, SistemaGiocoDim> hashSistemaDimDatamart,
			SistemaGiocoDim sysdim, Connection connExport) throws SQLException {

		//connExport.setAutoCommit(true);

		String sqlInsertClienteDim = "INSERT INTO sistemagiocodim (AAMS_GAMESYSTEM_CODE,AAMS_LOCATION_CODE,AAMS_VLT_CODE,GS_VLT_CODE,AAMS_GAME_CODE ) VALUES (?,?,?,?,?)";
		PreparedStatement ps = connExport.prepareStatement(sqlInsertClienteDim);

		logger.debug("Start - Insert SistemaGioco Dim to Datamart "+sysdim.getAAMS_GAMESYSTEM_CODE()+"-"+sysdim.getAAMS_LOCATION_CODE()+"-"+sysdim.getAAMS_VLT_CODE()+"-"+sysdim.getAAMS_GAME_CODE());

		if (!hashSistemaDimDatamart.containsKey(sysdim.getAAMS_LOCATION_CODE()
				+ "-" + sysdim.getAAMS_VLT_CODE() + "-"
				+ sysdim.getAAMS_GAME_CODE())) {
			ps.setLong(1, sysdim.getAAMS_GAMESYSTEM_CODE());
			ps.setString(2, sysdim.getAAMS_LOCATION_CODE());
			ps.setString(3, sysdim.getAAMS_VLT_CODE());
			ps.setString(4, sysdim.getGS_VLT_CODE());
			ps.setLong(5, sysdim.getAAMS_GAME_CODE());

			ps.executeUpdate();

			Statement statement = connExport.createStatement();

			ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");

			if (rs.next()) {
				long lastInsert = rs.getLong(1);
				logger.info("SYNC  SistemaGioco Dim");				
				logger.info("SYSDIM NUM " + lastInsert+ " dim "+sysdim);
				sysdim.setId(lastInsert);
				hashSistemaDimDatamart.put(
						sysdim.getAAMS_LOCATION_CODE() + "-"
								+ sysdim.getAAMS_VLT_CODE() + "-"
								+ sysdim.getAAMS_GAME_CODE(), sysdim);
			}

			statement.close();
			rs.close();
		}

		logger.debug("End - Insert SistemaGioco Dim to Datamart "+sysdim.getAAMS_GAMESYSTEM_CODE()+"-"+sysdim.getAAMS_LOCATION_CODE()+"-"+sysdim.getAAMS_VLT_CODE()+"-"+sysdim.getAAMS_GAME_CODE());

		ps.close();

		//connExport.setAutoCommit(false);

	}
   
   
   
}
