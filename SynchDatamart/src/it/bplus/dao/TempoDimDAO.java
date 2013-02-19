package it.bplus.dao;

import it.bplus.primitive.TempoDim;
import it.bplus.util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class TempoDimDAO {
	protected static Logger logger = Logger.getLogger(TempoDimDAO.class);
	
	
	
	   public static HashMap<java.util.Date, TempoDim> getAllTempoDimDatamart(Connection connExport) throws SQLException{
		   HashMap<java.util.Date, TempoDim> hashTempoDim = new HashMap<java.util.Date, TempoDim>();
			
		   logger.info("Start - Get Tempo Dim from Datamart");
			
			String sqlImportTempoDim = "select ID,DATA from tempodim";
		
			
			Statement statementImportTempoDim = connExport.createStatement();		
			ResultSet resultSetImportTempoDim = statementImportTempoDim.executeQuery(sqlImportTempoDim);
			
			while (resultSetImportTempoDim.next()){
				TempoDim tdim = new TempoDim(resultSetImportTempoDim.getLong("ID"),new java.util.Date(resultSetImportTempoDim.getTimestamp("DATA").getTime()));
				hashTempoDim.put(new java.util.Date(resultSetImportTempoDim.getTimestamp("DATA").getTime()),tdim);				
			}
			
			 logger.info("End - Get Tempo Dim from Datamart");
			
			 statementImportTempoDim.close();
			 resultSetImportTempoDim.close();
			return hashTempoDim;
		   
	   }
	   
	   
	   public static void insertTempoDim(HashMap<java.util.Date, TempoDim> hashTempoDimDatamart,java.util.Date data,Connection connExport) throws SQLException, ParseException{
		 
		   
		   
		   //connExport.setAutoCommit(false);
		   
		   String sqlInsertTempoDim = "INSERT INTO tempodim (DATA,ORA,MINUTO,GIORNO,SETTIMANA,DIECIGIORNI,QUINDICIGIORNI,MESE,SEIMESI,ANNO) VALUES (?,?,?,?,?,?,?,?,?,?)";
		   PreparedStatement ps = connExport.prepareStatement(sqlInsertTempoDim);
		   		   
		   logger.debug("Start - Insert Tempo Dim to Datamart");
		   
			   if(!hashTempoDimDatamart.containsKey(data))
			   {  
				   TempoDim tdim = new TempoDim(data);
				   ps.setTimestamp(1,new java.sql.Timestamp(data.getTime()));
				   
				   ps.setInt(2,tdim.getHour());
				   ps.setInt(3, tdim.getMinute());
				   ps.setInt(4, tdim.getDay());
				   ps.setInt(5, tdim.getWeek());
				   ps.setInt(6, tdim.getTendays());
				   ps.setInt(7, tdim.getFifteendays());
				   ps.setInt(8, tdim.getMonth());
				   ps.setInt(9, tdim.getSixmonth());
				   ps.setInt(10, tdim.getYear());
				   
				   ps.executeUpdate();
				   
				   
				   Statement statement = connExport.createStatement();

					ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");

					if (rs.next()) {
						long lastInsert = rs.getLong(1);			
						logger.debug("AGGIUNTO TEMPODIM NUM " + lastInsert);
						tdim.setId(lastInsert);
						
						
						hashTempoDimDatamart.put(DateUtils.timestampNoMillisecond(data),tdim);
					}

					statement.close();
					rs.close();
				   
			   }
		   
		   
		   logger.debug("End - Insert Tempo Dim to Datamart");
		   
		   
		   
		   ps.close();
			   
		  // connExport.commit();
		   
	   }
	   

}
