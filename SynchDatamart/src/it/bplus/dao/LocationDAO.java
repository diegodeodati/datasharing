package it.bplus.dao;

import it.bplus.primitive.Location;
import it.bplus.primitive.LocationExtra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class LocationDAO {

	protected static Logger logger = Logger.getLogger(LocationDAO.class);
	


	
	public static ArrayList<Location> getAllLocationEtna(Connection connectionImport) throws SQLException{
		ArrayList<Location> listLocEtna = new ArrayList<Location>();
		
		logger.info("Start - Get Location from Etna");
		
		String sqlImportEtnaLocation = "select id,name,address,address_number,cadastral_code,cap,creation_ts,deletion_ts,"
			+"location_type_id,toponimo_id,surface,surface_vlt,telephone,opening_date from sc_location";
	
		
		Statement statementImportEtnaLocation = connectionImport.createStatement();		
		ResultSet resultSetImportEtnaLocation = statementImportEtnaLocation.executeQuery(sqlImportEtnaLocation);
		
		while (resultSetImportEtnaLocation.next()){
			Location loc = new Location();
			loc.setAAMS_LOCATION_ID(resultSetImportEtnaLocation.getString("id"));
			loc.setCOMMERCIAL_NAME(resultSetImportEtnaLocation.getString("name"));
			loc.setADDRESS(resultSetImportEtnaLocation.getString("address"));
			loc.setSTREET_NUMBER(resultSetImportEtnaLocation.getString("address_number"));
			loc.setCADASTRAL_CODE(resultSetImportEtnaLocation.getString("cadastral_code"));
			loc.setCAP(resultSetImportEtnaLocation.getString("cap"));
			
			
			String creationTimeStr =  resultSetImportEtnaLocation.getString("creation_ts");
			if(creationTimeStr!=null && !creationTimeStr.equals("0000-00-00 00:00:00"))
			loc.setCREATION_DATE(java.sql.Timestamp.valueOf(creationTimeStr));
			
			String cessationTimeStr =  resultSetImportEtnaLocation.getString("deletion_ts");
			if(cessationTimeStr!=null && !cessationTimeStr.equals("0000-00-00 00:00:00"))
			loc.setCESSATION_DATE(java.sql.Timestamp.valueOf(cessationTimeStr));
			
			
			loc.setID_LOCATION_TYPE(resultSetImportEtnaLocation.getInt("location_type_id"));
			loc.setID_TOPONIMO(resultSetImportEtnaLocation.getInt("toponimo_id"));
			loc.setFLOOR_SURFACE(resultSetImportEtnaLocation.getDouble("surface"));			
			loc.setFLOOR_VLT_SURFACE(resultSetImportEtnaLocation.getDouble("surface_vlt"));
			loc.setTELEPHONE(resultSetImportEtnaLocation.getString("telephone"));
			loc.setOPENING_DATE(resultSetImportEtnaLocation.getDate("opening_date"));
			
			listLocEtna.add(loc);
			
		}
				
		logger.info("End - Get Location from Etna");
		
		statementImportEtnaLocation.close();
		resultSetImportEtnaLocation.close();
		return listLocEtna;
	}

   public static HashMap<String, Location> getAllLocationStaging(Connection connectionImport) throws SQLException{
	   HashMap<String, Location> hashLocStaging = new HashMap<String, Location>();
		
	   logger.info("Start - Get Location from Staging");
		
		String sqlImportStagingLocation = "select AAMS_LOCATION_CODE,COMMERCIAL_NAME,ADDRESS,STREET_NUMBER,CADASTRAL_CODE,CAP,CREATION_DATE,CESSATION_DATE,"
			+"ID_LOCATION_TYPE,ID_TOPONIMO,FLOOR_SURFACE,FLOOR_VLT_SURFACE,TELEPHONE,OPENING_DATE from BIRSLOCATION";
	
		
		Statement statementImportStagingLocation = connectionImport.createStatement();		
		ResultSet resultSetImportStagingLocation = statementImportStagingLocation.executeQuery(sqlImportStagingLocation);
		
		while (resultSetImportStagingLocation.next()){
			Location loc = new Location();
			loc.setAAMS_LOCATION_ID(resultSetImportStagingLocation.getString("AAMS_LOCATION_CODE"));
			loc.setCOMMERCIAL_NAME(resultSetImportStagingLocation.getString("COMMERCIAL_NAME"));
			loc.setADDRESS(resultSetImportStagingLocation.getString("ADDRESS"));
			loc.setSTREET_NUMBER(resultSetImportStagingLocation.getString("STREET_NUMBER"));
			loc.setCADASTRAL_CODE(resultSetImportStagingLocation.getString("CADASTRAL_CODE"));
			loc.setCAP(resultSetImportStagingLocation.getString("CAP"));
			loc.setCREATION_DATE(resultSetImportStagingLocation.getTimestamp("CREATION_DATE"));
			loc.setCESSATION_DATE(resultSetImportStagingLocation.getTimestamp("CESSATION_DATE"));
			loc.setID_LOCATION_TYPE(resultSetImportStagingLocation.getInt("ID_LOCATION_TYPE"));
			loc.setID_TOPONIMO(resultSetImportStagingLocation.getInt("ID_TOPONIMO"));
			loc.setFLOOR_SURFACE(resultSetImportStagingLocation.getDouble("FLOOR_SURFACE"));			
			loc.setFLOOR_VLT_SURFACE(resultSetImportStagingLocation.getDouble("FLOOR_VLT_SURFACE"));
			loc.setTELEPHONE(resultSetImportStagingLocation.getString("TELEPHONE"));
			loc.setOPENING_DATE(resultSetImportStagingLocation.getDate("OPENING_DATE"));
			
			hashLocStaging.put(loc.getAAMS_LOCATION_ID(),loc);
			
		}
		
		 logger.info("End - Get Location from Staging");
		
		statementImportStagingLocation.close();
		resultSetImportStagingLocation.close();
		return hashLocStaging;
	   
   }
   
   public static HashMap<String, LocationExtra> getAllLocationExtraAggregate(Connection connectionImport) throws SQLException{
	   HashMap<String, LocationExtra> hashLocExtraAggregate = new HashMap<String, LocationExtra>();
		
	   logger.info("Start - Get Location Extra from Aggregate");
		
		String sqlImportLocationExtraAggregate = "select contr.AAMS_LOCATION_CODE AAMS_LOCATION_CODE,PCT_GESTORE,PCT_ESERCENTE,PCT_CONCESSIONARIO,loc.CADASTRAL_CODE CADASTRAL_CODE from aggregate.birslocation loc " +
				"inner join aggregate.birscontratto contr on loc.AAMS_LOCATION_CODE=contr.AAMS_LOCATION_CODE";
	
		
		Statement statementImportLocationExtraAggregate = connectionImport.createStatement();		
		ResultSet resultSetImportLocationExtraAggregate = statementImportLocationExtraAggregate.executeQuery(sqlImportLocationExtraAggregate);
		
		while (resultSetImportLocationExtraAggregate.next()){
			Location loc = new Location();
			loc.setAAMS_LOCATION_ID(resultSetImportLocationExtraAggregate.getString("AAMS_LOCATION_CODE"));
			loc.setCADASTRAL_CODE(resultSetImportLocationExtraAggregate.getString("CADASTRAL_CODE"));
			
			LocationExtra locExtra = new LocationExtra();
			
			locExtra.setL(loc);
			locExtra.setPct_gestore(resultSetImportLocationExtraAggregate.getDouble("PCT_GESTORE"));
			locExtra.setPct_esercente(resultSetImportLocationExtraAggregate.getDouble("PCT_ESERCENTE"));
			locExtra.setPct_concessionario(resultSetImportLocationExtraAggregate.getDouble("PCT_CONCESSIONARIO"));
			
			hashLocExtraAggregate.put(loc.getAAMS_LOCATION_ID(),locExtra);
			
		}
		
		 logger.info("End - Get Location Extra from Aggregate");
		
		statementImportLocationExtraAggregate.close();
		resultSetImportLocationExtraAggregate.close();
		return hashLocExtraAggregate;
	   
   }
   
   
   public static void insertLocation(Connection connEtna,Connection connStaging) throws SQLException{
	 
	   ArrayList<Location> listLocEtna = getAllLocationEtna(connEtna);
	   HashMap<String, Location> hashLocStaging = getAllLocationStaging(connStaging);
	   
	   connStaging.setAutoCommit(true);
	   
	   String sqlInsertLocation = "INSERT INTO BIRSLOCATION (AAMS_LOCATION_CODE,CADASTRAL_CODE,COMMERCIAL_NAME,ADDRESS,STREET_NUMBER,CESSATION_DATE,"+
	   "CREATION_DATE,FLOOR_SURFACE,ID_LOCATION_TYPE,ID_TOPONIMO,CAP,FLOOR_VLT_SURFACE,TELEPHONE,OPENING_DATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	   PreparedStatement ps = connStaging.prepareStatement(sqlInsertLocation);
	   
       String sqlUpdateLocation = "UPDATE BIRSLOCATION SET OPENING_DATE = ? where AAMS_LOCATION_CODE = ?";
       PreparedStatement psUpdate = connStaging.prepareStatement(sqlUpdateLocation);
	   
	   logger.info("Start - Insert Location to Staging");
	 
	   
	   for(Location actLoc:listLocEtna){		   
		   if(!hashLocStaging.containsKey(actLoc.getAAMS_LOCATION_ID()))
		   {  
			   ps.setString(1,actLoc.getAAMS_LOCATION_ID());
			   ps.setString(2,actLoc.getCADASTRAL_CODE());
			   ps.setString(3,actLoc.getCOMMERCIAL_NAME());
			   ps.setString(4, actLoc.getADDRESS());
			   ps.setString(5, actLoc.getSTREET_NUMBER());
			   
			   			   
			   ps.setTimestamp(6, actLoc.getCESSATION_DATE());
			   ps.setTimestamp(7,actLoc.getCREATION_DATE());
			   			   
			   
			   ps.setDouble(8,actLoc.getFLOOR_SURFACE());
			   ps.setInt(9,actLoc.getID_LOCATION_TYPE());
			   ps.setInt(10,actLoc.getID_TOPONIMO());
			   ps.setString(11,actLoc.getCAP());
			   ps.setDouble(12,actLoc.getFLOOR_VLT_SURFACE());
			   ps.setString(13, actLoc.getTELEPHONE());
			   ps.setDate(14, actLoc.getOPENING_DATE());
			   
			   ps.executeUpdate();
		   }
		   else
		   {
			   Location actLocStaging = hashLocStaging.get(actLoc.getAAMS_LOCATION_ID());
			   if(actLocStaging.getOPENING_DATE()==null){
				   psUpdate.setDate(1,actLoc.getOPENING_DATE());
				   psUpdate.setString(2,actLoc.getAAMS_LOCATION_ID());
				   
				   psUpdate.executeUpdate();
			   }
			   
			   
		   }
	   }
	   
	   logger.info("End - Insert Location to Staging");
	   
	   
	   
	   ps.close();
		   
	   connStaging.setAutoCommit(false);
	   
   }
}
