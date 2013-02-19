package it.bplus.dao;

import it.bplus.primitive.Contratto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class ContrattoDAO {

protected static Logger logger = Logger.getLogger(ContrattoDAO.class);
	
	public static HashMap<String,Contratto> getAllContrattoStaging(Connection connectionExport) throws SQLException{
		
		
		logger.info("Start Get Contratto From Staging");
		HashMap<String ,Contratto> hashAllContratto = new HashMap<String, Contratto>();
		
		
		Statement statementImport = connectionExport.createStatement();
		String sqlImportContratto = "select AAMS_LOCATION_CODE,COD_GESTORE,COD_ESERCENTE,PCT_GESTORE,PCT_ESERCENTE,PCT_CONCESSIONARIO from BIRSCONTRATTO";		
		ResultSet resultSetImportContratto = statementImport.executeQuery(sqlImportContratto);
		
		
		while (resultSetImportContratto.next()) {	
			
			String AAMS_LOCATION_ID = resultSetImportContratto.getString("AAMS_LOCATION_CODE");
			String COD_GESTORE = resultSetImportContratto.getString("COD_GESTORE");
			int COD_ESERCENTE = resultSetImportContratto.getInt("COD_ESERCENTE");
			double PCT_ES = resultSetImportContratto.getDouble("PCT_ESERCENTE");
			double PCT_GE = resultSetImportContratto.getDouble("PCT_GESTORE");
			double PCT_CO = resultSetImportContratto.getDouble("PCT_CONCESSIONARIO");
			
			Contratto co = new Contratto(AAMS_LOCATION_ID, COD_GESTORE, COD_ESERCENTE, PCT_GE, PCT_ES, PCT_CO);
		    
		    hashAllContratto.put(AAMS_LOCATION_ID, co); //era AAMS_LOCATION_ID+"-"+COD_GESTORE+"-"+COD_ESERCENTE
			
		}
		
		resultSetImportContratto.close();
		
		statementImport.close();
		
		logger.info("End Get Contratto From Staging");
		
		return hashAllContratto;
	}
	
	
    public static ArrayList<Contratto> getAllContrattoEtna(Connection connectionImport)  throws SQLException{
		
		logger.info("Start - Get Contratto from Etna");
		ArrayList<Contratto> listContrattoEtna = new ArrayList<Contratto>();
		
		Statement statementImport = connectionImport.createStatement();
		String sqlImportContratto = "select id,manager_id,merchant_id,pct_esercente,pct_gestore,pct_concessionario from sc_location";		
		ResultSet resultSetImportContratto = statementImport.executeQuery(sqlImportContratto);
		
		while(resultSetImportContratto.next()){
			String AAMS_LOCATION_ID = resultSetImportContratto.getString("id");
			String COD_GESTORE = resultSetImportContratto.getString("manager_id");
			int COD_ESERCENTE = resultSetImportContratto.getInt("merchant_id");
			double PCT_ES = resultSetImportContratto.getDouble("pct_esercente");
			double PCT_GE = resultSetImportContratto.getDouble("pct_gestore");
			double PCT_CO = resultSetImportContratto.getDouble("pct_concessionario");
			
			Contratto co = new Contratto(AAMS_LOCATION_ID, COD_GESTORE, COD_ESERCENTE, PCT_GE, PCT_ES, PCT_CO);
			
			listContrattoEtna.add(co);
		}
		
		logger.info("End - Get Contratto from Etna");
		
		resultSetImportContratto.close();
		statementImport.close();		
		return listContrattoEtna;
		
	}

    
   
    public static void insertContratto(Connection connEtna,Connection connStaging) throws SQLException{
		 
		   ArrayList<Contratto> listContrattoEtna = getAllContrattoEtna(connEtna);
		   HashMap<String,Contratto> hashContrattoStaging = getAllContrattoStaging(connStaging);
		   
		   connStaging.setAutoCommit(true);
		   
		   
		   for(Contratto actContratto:listContrattoEtna){
			   String key = actContratto.getAAMS_LOCATION_ID();
			   if(!hashContrattoStaging.containsKey(key))
			   {
				   String sqlInsertContratto = "INSERT INTO BIRSCONTRATTO (AAMS_LOCATION_CODE,COD_GESTORE,COD_ESERCENTE,PCT_GESTORE,PCT_ESERCENTE,PCT_CONCESSIONARIO)"+
				   " VALUES (?,?,?,?,?,?)";
				   PreparedStatement ps = connStaging.prepareStatement(sqlInsertContratto);
				   
				   
				   
				   ps.setString(1,actContratto.getAAMS_LOCATION_ID());
				   ps.setString(2,actContratto.getCOD_GESTORE());
				   ps.setInt(3,actContratto.getCOD_ESERCENTE());
				   ps.setDouble(4, actContratto.getPCT_GESTORE());
				   ps.setDouble(5, actContratto.getPCT_ESERCENTE());
				   ps.setDouble(6, actContratto.getPCT_CONCESSIONARIO());
				   
				   if(actContratto.getCOD_GESTORE()!=null || actContratto.getCOD_ESERCENTE()!=0){		   
				   ps.executeUpdate();
				   logger.info("INSERT CONTRATTO su LOCATION: "+key);
				   }
				   
				   
				   ps.close();
			   }
			   else{
				   Contratto ContrattoChanged = hashContrattoStaging.get(key);
				   
				   if(!ContrattoChanged.equals(actContratto)){
					   
					   				   
					   String sqlUpdateContratto = "UPDATE BIRSCONTRATTO SET PCT_ESERCENTE=? , PCT_GESTORE=? , PCT_CONCESSIONARIO=? WHERE AAMS_LOCATION_CODE=?";
					   PreparedStatement ps = connStaging.prepareStatement(sqlUpdateContratto);
					   
					   logger.info("UPDATE CONTRATTO su LOCATION: "+key);
					   
					   
					   ps.setDouble(1,actContratto.getPCT_ESERCENTE());
					   ps.setDouble(2,actContratto.getPCT_GESTORE());
					   ps.setDouble(3,actContratto.getPCT_CONCESSIONARIO());
					   
					   ps.setString(4,actContratto.getAAMS_LOCATION_ID());
					   
					   
					   ps.executeUpdate();
					   
					   ps.close();
				   }		
			   }
		   }  
			   
		   connStaging.setAutoCommit(false);
		   
	   }
	
	
}
