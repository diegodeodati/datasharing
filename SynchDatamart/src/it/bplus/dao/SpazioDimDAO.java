package it.bplus.dao;

import it.bplus.primitive.SpazioDim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class SpazioDimDAO {

	protected static Logger logger = Logger.getLogger(SpazioDimDAO.class);
	
	
	public static ArrayList<SpazioDim> getAllSpazioDimAggregate(Connection connectionImport) throws SQLException{
		ArrayList<SpazioDim> listSpazioDim = new ArrayList<SpazioDim>();
		
		logger.info("Start - Get Spazio Dim from Aggregate");
		
		String sqlImportSpazioDim = "select comuni.cadastral_code CADASTRAL_CODE,province.id_prov ID_PROV,regioni.id_reg ID_REG,aree.id_area ID_AREA,country.id_country ID_NAZ " +
				"from aggregate.birscomuni comuni " +
				"join aggregate.birsprovince province on comuni.id_prov = province.id_prov " +
				"join aggregate.birsregioni regioni on province.id_reg=regioni.id_reg " +
				"join aggregate.birsarea aree on regioni.id_area=aree.id_area " +
				"join aggregate.birscountry country on regioni.id_country=country.id_country";
	
		
		Statement statementImportSpazioDim = connectionImport.createStatement();		
		ResultSet resultSetImportSpazioDim = statementImportSpazioDim.executeQuery(sqlImportSpazioDim);
		
		while (resultSetImportSpazioDim.next()){
			SpazioDim sdim = new SpazioDim();
			sdim.setId_comune(resultSetImportSpazioDim.getString("CADASTRAL_CODE"));
			sdim.setId_provincia(resultSetImportSpazioDim.getInt("ID_PROV"));
			sdim.setId_regione(resultSetImportSpazioDim.getString("ID_REG"));
		    sdim.setId_area(resultSetImportSpazioDim.getInt("ID_AREA"));
		    sdim.setId_nazione(resultSetImportSpazioDim.getInt("ID_NAZ"));
			
			
			listSpazioDim.add(sdim);
			
		}
				
		logger.info("End - Get Cliente Dim from Aggregate");
		
		statementImportSpazioDim.close();
		resultSetImportSpazioDim.close();
		return listSpazioDim;
	}

   public static HashMap<String, SpazioDim> getAllSpazioDimDatamart(Connection connExport) throws SQLException{
	   HashMap<String, SpazioDim> hashSpazioDim  = new HashMap<String, SpazioDim>();
		
	   logger.info("Start - Get Spazio Dim from Datamart");
		
		String sqlImportSpazioDim = "select ID,ID_COMUNE,ID_PROVINCIA,ID_AREA,ID_REGIONE,ID_NAZIONE from spaziodim";
	
		
		Statement statementImportSpazioDim = connExport.createStatement();		
		ResultSet resultSetImportSpazioDim = statementImportSpazioDim.executeQuery(sqlImportSpazioDim);
		
				
		while (resultSetImportSpazioDim.next()){
			SpazioDim sdim = new SpazioDim();
			sdim.setId(resultSetImportSpazioDim.getLong("ID"));
			sdim.setId_comune(resultSetImportSpazioDim.getString("ID_COMUNE"));
			sdim.setId_provincia(resultSetImportSpazioDim.getInt("ID_PROVINCIA"));
			sdim.setId_regione(resultSetImportSpazioDim.getString("ID_REGIONE"));
		    sdim.setId_area(resultSetImportSpazioDim.getInt("ID_AREA"));
		    sdim.setId_nazione(resultSetImportSpazioDim.getInt("ID_NAZIONE"));
			
			hashSpazioDim.put(sdim.getId_comune(),sdim);			
		}
		
		 logger.info("End - Get Spazio Dim from Datamart");
		
		 statementImportSpazioDim.close();
		 resultSetImportSpazioDim.close();
		return hashSpazioDim;
	   
   }
   
   public static HashMap<String, SpazioDim> getAllLocationSpazioDimDatamart(Connection connExport) throws SQLException{
	   HashMap<String, SpazioDim> hashLocationSpazioDim  = new HashMap<String, SpazioDim>();
		
	   logger.info("Start - Get Spazio Dim from Datamart");
		
		String sqlImportLocationSpazioDim = "select AAMS_LOCATION_CODE,ID from spaziodim sdim " +
				"inner join aggregate.birslocation loc on sdim.`ID_COMUNE`=loc.CADASTRAL_CODE";
	
		
		Statement statementImportLocationSpazioDim = connExport.createStatement();		
		ResultSet resultSetImportLocationSpazioDim = statementImportLocationSpazioDim.executeQuery(sqlImportLocationSpazioDim);
		
				
		while (resultSetImportLocationSpazioDim.next()){
			SpazioDim sdim = new SpazioDim();
			sdim.setId(resultSetImportLocationSpazioDim.getLong("ID"));
			
			hashLocationSpazioDim.put(resultSetImportLocationSpazioDim.getString("AAMS_LOCATION_CODE"),sdim);			
		}
		
		 logger.info("End - Get Spazio Dim from Datamart");
		
		 statementImportLocationSpazioDim.close();
		 resultSetImportLocationSpazioDim.close();
		return hashLocationSpazioDim;
	   
   }
   
   
   
   public static void insertSpazioDim(HashMap<String, SpazioDim> hashSpazioDimDatamart,Connection connImport,Connection connExport) throws SQLException{
	 
	   ArrayList<SpazioDim> listSpazioDimAggregate = getAllSpazioDimAggregate(connImport);
	   
	   connExport.setAutoCommit(true);
	   
	   String sqlInsertSpazioDim = "INSERT INTO spaziodim (ID_COMUNE,ID_PROVINCIA,ID_REGIONE,ID_AREA,ID_NAZIONE) VALUES (?,?,?,?,?)";
	   PreparedStatement ps = connExport.prepareStatement(sqlInsertSpazioDim);
	   
      
	   
	   logger.info("Start - Insert Spazio Dim to Datamart");
	 
	   
	   for(SpazioDim actSpazioDim:listSpazioDimAggregate){		   
		   if(!hashSpazioDimDatamart.containsKey(actSpazioDim.getId_comune()))
		   {  
			   ps.setString(1,actSpazioDim.getId_comune());
			   ps.setInt(2,actSpazioDim.getId_provincia());
			   ps.setString(3, actSpazioDim.getId_regione());
			   ps.setInt(4, actSpazioDim.getId_area());
			   ps.setInt(5, actSpazioDim.getId_nazione());
			   
			   ps.executeUpdate();
			   
			   Statement statement = connExport.createStatement();

			   ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");

				if (rs.next()) {
					long lastInsert = rs.getLong(1);
					System.out.println("SPAZIODIM NUM " + lastInsert);
					actSpazioDim.setId(lastInsert);
					hashSpazioDimDatamart.put(actSpazioDim.getId_comune(), actSpazioDim);
				}

				statement.close();
				rs.close();
			   
			   
		   }
	   }
	   
	   logger.info("End - Insert Spazio Dim to Datamart");
	   
	   
	   
	   ps.close();
		   
	   connExport.setAutoCommit(false);
	   
   }
}
