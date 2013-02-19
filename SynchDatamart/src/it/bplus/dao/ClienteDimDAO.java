package it.bplus.dao;

import it.bplus.primitive.ClienteDim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class ClienteDimDAO {

	protected static Logger logger = Logger.getLogger(ClienteDimDAO.class);
	
	
	public static ArrayList<ClienteDim> getAllClienteDimAggregate(Connection connectionImport) throws SQLException{
		ArrayList<ClienteDim> listClienteDim = new ArrayList<ClienteDim>();
		
		logger.info("Start - Get Cliente Dim from Aggregate");
		
		String sqlImportClienteDim = "select distinct COD_ESERCENTE,COD_GESTORE from birscontratto";
	
		
		Statement statementImportClienteDim = connectionImport.createStatement();		
		ResultSet resultSetImportClienteDim = statementImportClienteDim.executeQuery(sqlImportClienteDim);
		
		while (resultSetImportClienteDim.next()){
			ClienteDim cdim = new ClienteDim();
			cdim.setCOD_ESERCENTE(resultSetImportClienteDim.getLong("COD_ESERCENTE"));
			cdim.setCOD_GESTORE(resultSetImportClienteDim.getString("COD_GESTORE"));
			
			listClienteDim.add(cdim);
			
		}
				
		logger.info("End - Get Cliente Dim from Aggregate");
		
		statementImportClienteDim.close();
		resultSetImportClienteDim.close();
		return listClienteDim;
	}

   public static HashMap<String, ClienteDim> getAllClienteDimDatamart(Connection connExport) throws SQLException{
	   HashMap<String, ClienteDim> hashClienteDim  = new HashMap<String, ClienteDim>();
		
	   logger.info("Start - Get Cliente Dim from Datamart");
		
		String sqlImportClienteDim = "select ID,COD_ESERCENTE,COD_GESTORE from clientedim";
	
		
		Statement statementImportClienteDim = connExport.createStatement();		
		ResultSet resultSetImportClienteDim = statementImportClienteDim.executeQuery(sqlImportClienteDim);
		
		while (resultSetImportClienteDim.next()){
			ClienteDim cdim = new ClienteDim();
			cdim.setId(resultSetImportClienteDim.getLong("ID"));
			cdim.setCOD_ESERCENTE(resultSetImportClienteDim.getLong("COD_ESERCENTE"));
			cdim.setCOD_GESTORE(resultSetImportClienteDim.getString("COD_GESTORE"));
			
			hashClienteDim.put(cdim.getCOD_ESERCENTE()+"-"+cdim.getCOD_GESTORE(),cdim);
			
		}
		
		 logger.info("End - Get Cliente Dim from Datamart");
		
		 statementImportClienteDim.close();
		 resultSetImportClienteDim.close();
		return hashClienteDim;
	   
   }
   
   /**Ritorna tutte le associazioni hash LOCATION - CLIENTEDIM_ID*/
   
   
   public static HashMap<String, ClienteDim> getAllLocationClienteDimDatamart(Connection connExport) throws SQLException{
	   HashMap<String, ClienteDim> hashLocationClienteDim  = new HashMap<String, ClienteDim>();
		
	   logger.info("Start - Get Location Cliente Dim from Datamart");
		
		String sqlImportLocationClienteDim = "select ID,cdim.COD_GESTORE COD_GESTORE,cdim.COD_ESERCENTE COD_ESERCENTE,cont.AAMS_LOCATION_CODE AAMS_LOCATION_CODE from clientedim cdim " +
				"inner join aggregate.birscontratto cont on cdim.COD_GESTORE= cont.COD_GESTORE " +
				"and cdim.COD_ESERCENTE= cont.COD_ESERCENTE";
	
		
		Statement statementImportLocationClienteDim = connExport.createStatement();		
		ResultSet resultSetImportLocationClienteDim = statementImportLocationClienteDim.executeQuery(sqlImportLocationClienteDim);
		
		while (resultSetImportLocationClienteDim.next()){
			ClienteDim cdim = new ClienteDim();
			cdim.setId(resultSetImportLocationClienteDim.getLong("ID"));
			cdim.setCOD_GESTORE(resultSetImportLocationClienteDim.getString("COD_GESTORE"));
			cdim.setCOD_ESERCENTE(resultSetImportLocationClienteDim.getLong("COD_ESERCENTE"));
			
			hashLocationClienteDim.put(resultSetImportLocationClienteDim.getString("AAMS_LOCATION_CODE"),cdim);
			
		}
		
		 logger.info("End - Get Location Cliente Dim from Datamart");
		
		 statementImportLocationClienteDim.close();
		 resultSetImportLocationClienteDim.close();
		return hashLocationClienteDim;
	   
   }
   
   
   public static void insertClienteDim(HashMap<String, ClienteDim> hashClienteDimDatamart,Connection connImport,Connection connExport) throws SQLException{
	 
	   ArrayList<ClienteDim> listClienteDimAggregate = getAllClienteDimAggregate(connImport);
	 	   
	   connExport.setAutoCommit(true);
	   
	   String sqlInsertClienteDim = "INSERT INTO clientedim (COD_ESERCENTE,COD_GESTORE) VALUES (?,?)";
	   PreparedStatement ps = connExport.prepareStatement(sqlInsertClienteDim);
	   
      
	   
	   logger.info("Start - Insert Cliente Dim to Datamart");
	 
	   
	   for(ClienteDim actClienteDim:listClienteDimAggregate){		   
		   if(!hashClienteDimDatamart.containsKey(actClienteDim.getCOD_ESERCENTE()+"-"+actClienteDim.getCOD_GESTORE()))
		   {  
			   ps.setLong(1,actClienteDim.getCOD_ESERCENTE());
			   ps.setString(2,actClienteDim.getCOD_GESTORE());
			   
			   ps.executeUpdate();
			   
			   Statement statement = connExport.createStatement();

			   ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");

				if (rs.next()) {
					long lastInsert = rs.getLong(1);			
					System.out.println("CLIENTEDIM NUM " + lastInsert);
					actClienteDim.setId(lastInsert);
					hashClienteDimDatamart.put(actClienteDim.getCOD_ESERCENTE()+"-"+actClienteDim.getCOD_GESTORE(),actClienteDim);			
				}

				statement.close();
				rs.close();
			   
		   }
	   }
	   
	   logger.info("End - Insert Cliente Dim to Datamart");
	   
	   
	   
	   ps.close();
		   
	   connExport.setAutoCommit(false);
	   
   }
}
