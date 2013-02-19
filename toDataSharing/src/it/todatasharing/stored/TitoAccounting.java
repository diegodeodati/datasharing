package it.todatasharing.stored;

import it.todatasharing.exception.DataLayerException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TitoAccounting {
	
	static CallableStatement proc_ticket_data = null;
	static ResultSet rs = null;
	
	public static void executePrintDeviceType(Connection conn,String printDevType,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			java.sql.Date dStart = new java.sql.Date(dataStart.getTime());
			java.sql.Date dEnd = new java.sql.Date(dataEnd.getTime());
			
			 
			proc_ticket_data = conn.prepareCall("{ call AGS.Ticket (?,'','','','',?,?,?,?,'','') }");
			proc_ticket_data.setString(1,printDevType);
			proc_ticket_data.setDate(2,dStart);
			proc_ticket_data.setDate(3,dEnd);

			proc_ticket_data.setDate(4,dStart);
			proc_ticket_data.setDate(5,dEnd);
			
			
			
			rs = proc_ticket_data.executeQuery();
			
			while(rs.next()){
				
				
			}
			
			System.out.println("prova");
			proc_ticket_data.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call AGS.Ticket PRINT DEVICE", e);
		}
	}

	public static void executePayingDeviceType(Connection conn,String payingDevType,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			java.sql.Date dStart = new java.sql.Date(dataStart.getTime());
			java.sql.Date dEnd = new java.sql.Date(dataEnd.getTime());
			
			 
			proc_ticket_data = conn.prepareCall("{ call AGS.Ticket ('',?,'','','',?,?,?,?,'','') }");
			proc_ticket_data.setString(1,payingDevType);
			proc_ticket_data.setDate(2,dStart);
			proc_ticket_data.setDate(3,dEnd);

			proc_ticket_data.setDate(4,dStart);
			proc_ticket_data.setDate(5,dEnd);
			
			
			
			rs = proc_ticket_data.executeQuery();
			
			while(rs.next()){
				
				
			}
			
			System.out.println("prova");
			proc_ticket_data.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call AGS.Ticket PAYING DEVICE", e);
		}
	}

	public static void executeAllDeviceType(Connection conn,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			java.sql.Date dStart = new java.sql.Date(dataStart.getTime());
			java.sql.Date dEnd = new java.sql.Date(dataEnd.getTime());
			
			 
			proc_ticket_data = conn.prepareCall("{ call AGS.Ticket ('','','','','',?,?,?,?,'','') }");
			proc_ticket_data.setDate(1,dStart);
			proc_ticket_data.setDate(2,dEnd);

			proc_ticket_data.setDate(3,dStart);
			proc_ticket_data.setDate(4,dEnd);
			
			
			
			rs = proc_ticket_data.executeQuery();
			
			while(rs.next()){
				
				
			}
			
			System.out.println("prova");
			proc_ticket_data.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call AGS.Ticket ALL DEVICES", e);
		}
	}

	public static void executeAllDeviceTypeSite(Connection conn,int siteid,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			java.sql.Date dStart = new java.sql.Date(dataStart.getTime());
			java.sql.Date dEnd = new java.sql.Date(dataEnd.getTime());
			
			 
			proc_ticket_data = conn.prepareCall("{ call AGS.Ticket ('','',?,'','',?,?,?,?,'','') }");
			proc_ticket_data.setInt(1,siteid);
			proc_ticket_data.setDate(2,dStart);
			proc_ticket_data.setDate(3,dEnd);

			proc_ticket_data.setDate(4,dStart);
			proc_ticket_data.setDate(5,dEnd);
			
			
			
			rs = proc_ticket_data.executeQuery();
			
			while(rs.next()){
				
				
			}
			
			System.out.println("prova");
			proc_ticket_data.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call AGS.Ticket SITE", e);
		}
	}

}
