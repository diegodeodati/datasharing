package it.todatasharing.stored;


import it.todatasharing.exception.DataLayerException;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SeicentoMessages {
	static CallableStatement proc_games_data = null;
	static ResultSet rs = null;

	
	public static void executeTMin(Connection conn,int siteid,java.util.Date dataStart,java.util.Date dataEnd) throws DataLayerException{
		try {
			
			/*
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIMEFORMATTER_CQI);
            String dStartStr = sdf.format(arg0)*/
			
			java.sql.Date dStart = new java.sql.Date(dataStart.getTime());
			java.sql.Date dEnd = new java.sql.Date(dataEnd.getTime());
			 
			proc_games_data = conn.prepareCall("{ call Aams.parseMessage600 ('',?,'','',?,?) }");
			proc_games_data.setInt(1,siteid);
			proc_games_data.setDate(2,dStart);
			proc_games_data.setDate(3,dEnd);
			
			
			rs = proc_games_data.executeQuery();
			
			System.out.println("prova");
			proc_games_data.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("call Rng.GameHistoryAllPhases", e);
		}
	}
}
