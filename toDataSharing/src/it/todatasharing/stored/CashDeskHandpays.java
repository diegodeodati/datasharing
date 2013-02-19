package it.todatasharing.stored;

import it.todatasharing.exception.DataLayerException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CashDeskHandpays {
	
	static CallableStatement proc_cash_desk_data = null;
	static ResultSet rs = null;
	
	public static void execute(Connection conn,int siteid,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			java.sql.Date dStart = new java.sql.Date(dataStart.getTime());
			java.sql.Date dEnd = new java.sql.Date(dataEnd.getTime());
			
			 
			proc_cash_desk_data = conn.prepareCall("{ call Nucleus.VoucherHandpays (?,'','',?,?) }");
			proc_cash_desk_data.setInt(1,siteid);
			proc_cash_desk_data.setDate(2, dStart);
			proc_cash_desk_data.setDate(3,dEnd);
			
			
			rs = proc_cash_desk_data.executeQuery();
			
			while(rs.next()){
				
				
			}
			
			System.out.println("prova");
			proc_cash_desk_data.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call AGS.CashiersData", e);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
