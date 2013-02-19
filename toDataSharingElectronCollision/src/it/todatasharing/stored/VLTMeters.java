package it.todatasharing.stored;

import it.todatasharing.exception.DataLayerException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VLTMeters {
	static CallableStatement proc_vlt_data = null;
	static ResultSet rs = null;

	
	public static void execute(Connection conn,int siteid,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			 
			proc_vlt_data = conn.prepareCall("{ call Aams.VLT_meters (?) }");
			proc_vlt_data.setInt(1,siteid);
			
			
			rs = proc_vlt_data.executeQuery();
			
			while(rs.next()){
				
				
			}
			
			System.out.println("prova");
			proc_vlt_data.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call Aams.VLT_meters", e);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
