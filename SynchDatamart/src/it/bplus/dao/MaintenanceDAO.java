package it.bplus.dao;

import it.bplus.primitive.Maintenance;
import it.bplus.util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class MaintenanceDAO {

	protected static Logger logger = Logger.getLogger(MaintenanceDAO.class);
	
    public static ArrayList<Maintenance> getAllMaintenanceEtna(Connection connectionImport)  throws SQLException{
		
		logger.info("Start - Get Maintenance from Etna");
		Statement statementImport = connectionImport.createStatement();
		ArrayList<Maintenance> listMaintenanceEtna = new ArrayList<Maintenance>();
		
		String sqlImportEtnaMaintenance = "select id,code_id,begin_date,end_date from sc_maintenance";
		ResultSet resultSetImportMaintenance = statementImport.executeQuery(sqlImportEtnaMaintenance);
		
		while(resultSetImportMaintenance.next()){
			Maintenance vlt = new Maintenance();
			vlt.setID_MAINTENANCE(resultSetImportMaintenance.getLong("id"));
			vlt.setAAMS_VLT_ID(resultSetImportMaintenance.getString("code_id"));
			vlt.setMAINTENANCE_DATE_IN(resultSetImportMaintenance.getDate("begin_date"));
			vlt.setMAINTENANCE_DATE_OUT(resultSetImportMaintenance.getDate("end_date"));
			
			listMaintenanceEtna.add(vlt);
		}
		
		logger.info("End - Get Maintenance from Etna");
		
		resultSetImportMaintenance.close();
		statementImport.close();		
		return listMaintenanceEtna;
		
	}

    public static HashMap<Long, Maintenance> getAllVltStaging(Connection connectionImport) throws SQLException  {
		   HashMap<Long, Maintenance> hashMaintenanceStaging = new HashMap<Long, Maintenance>();
			
		   logger.info("Start - Get Maintenance from Staging");
			
			String sqlImportStagingMaintenance = "select ID_MAINTENANCE, MAINTENANCE_DATE_IN,MAINTENANCE_DATE_OUT,AAMS_VLT_CODE from birsvltstatus";
		
			
			Statement statementImportStagingMaintenance = connectionImport.createStatement();		
			ResultSet resultSetImportStagingMaintenance = statementImportStagingMaintenance.executeQuery(sqlImportStagingMaintenance);
		
			while(resultSetImportStagingMaintenance.next()){
				Maintenance vlt = new Maintenance();
				vlt.setID_MAINTENANCE(resultSetImportStagingMaintenance.getLong("ID_MAINTENANCE"));
				vlt.setAAMS_VLT_ID(resultSetImportStagingMaintenance.getString("AAMS_VLT_ID"));	
				vlt.setMAINTENANCE_DATE_IN(resultSetImportStagingMaintenance.getDate("MAINTENANCE_DATE_IN"));
				vlt.setMAINTENANCE_DATE_OUT(resultSetImportStagingMaintenance.getDate("MAINTENANCE_DATE_OUT"));
				
				hashMaintenanceStaging.put(vlt.getID_MAINTENANCE(),vlt);
			}
			
			logger.info("End - Get Vlt from Staging");
			
			statementImportStagingMaintenance.close();
			resultSetImportStagingMaintenance.close();			
			
		return hashMaintenanceStaging;
	}


    public static void insertMaintenance(Connection connEtna,Connection connStaging) throws SQLException{
		 
		   ArrayList<Maintenance> listMaintenanceEtna = getAllMaintenanceEtna(connEtna);
		   HashMap<Long, Maintenance> hashMaintenanceStaging = getAllVltStaging(connStaging);
			   
		   connStaging.setAutoCommit(true);
		   
		   
		   logger.info("Start - Insert Maintenance");
		   
		   for(Maintenance actMaintenance:listMaintenanceEtna){
			   if(!hashMaintenanceStaging.containsKey(actMaintenance.getID_MAINTENANCE()))
			   {
				   String sqlInsertMaintenance = "INSERT INTO BIRSVLTSTATUS (ID_MAINTENANCE,AAMS_VLT_CODE,MAINTENANCE_DATE_IN,MAINTENANCE_DATE_OUT)"
				   +" VALUES (?,?,?,?)";
				   PreparedStatement ps = connStaging.prepareStatement(sqlInsertMaintenance);
				   
				   ps.setLong(1,actMaintenance.getID_MAINTENANCE());
				   ps.setString(2,actMaintenance.getAAMS_VLT_ID());
				   ps.setDate(3,actMaintenance.getMAINTENANCE_DATE_IN());
				   
				   if(actMaintenance.getMAINTENANCE_DATE_OUT()!=null)
				   ps.setDate(4,actMaintenance.getMAINTENANCE_DATE_OUT());
				   else
				   ps.setNull(4,Types.DATE);
				   
				   ps.executeUpdate();
				   
				   ps.close();
			   }
			   else{
				   Maintenance maintenanceChanged = hashMaintenanceStaging.get(actMaintenance.getID_MAINTENANCE());
				   if(!DateUtils.isDateEquals(maintenanceChanged.getMAINTENANCE_DATE_OUT(), actMaintenance.getMAINTENANCE_DATE_OUT())){
					   String sqlInsertMaintenance = "UPDATE BIRSVLTSTATUS SET MAINTENANCE_DATE_OUT=? WHERE ID_MAINTENANCE=?";
					   PreparedStatement ps = connStaging.prepareStatement(sqlInsertMaintenance);
					   
					   if(actMaintenance.getMAINTENANCE_DATE_OUT()!=null)
						   ps.setDate(1,actMaintenance.getMAINTENANCE_DATE_OUT());
					   else
						   ps.setNull(1,Types.DATE);
					   
					   ps.setLong(2,actMaintenance.getID_MAINTENANCE());
					   
					   ps.executeUpdate();
					   
					   ps.close();
				   }		
			   }
		   }  
			   
		   logger.info("Start - Insert Maintenance");
		   
		   connStaging.setAutoCommit(false);
		   
	   }
}
