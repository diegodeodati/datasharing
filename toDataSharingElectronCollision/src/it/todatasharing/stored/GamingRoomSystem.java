package it.todatasharing.stored;


import it.todatasharing.exception.DataLayerException;
import it.todatasharing.util.DateUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;


public class GamingRoomSystem {
   
	static ResultSet rs= null;

	protected static Logger logger = Logger.getLogger(GamingRoomSystem.class);
	/**Seleziona i clubId presenti all'interno della tabella AAMS.GamingRoomSystem
	 * 
	 * @param conn Connessione verso la 10 dalla parte del concessionario
	 * @return Ritorna il ResultSet che contiene l'insieme di tutti i club presenti a livello di concessionario
	 * @throws DataLayerException
	 */
	public static ResultSet execute(Connection conn,Date dEnd) throws DataLayerException{
		try {
			String data = DateUtils.dateToString(dEnd,"dd/MM/yyyy HH:mm");
			Statement stmtMSSQLClubId = conn.createStatement();
					
			String sql = "select clubid from AAMS.GamingRoomSystem where " +
					"(cessationdate is null or cessationdate>= CONVERT(DATETIME,'"+data+"',103))and registrationdate <= CONVERT(DATETIME,'"+data+"',103) " +
					"UNION " +
					"select clubid from AAMS.GamingRoomSystem where " +
					"CONVERT(VARCHAR(10),cessationdate,103) = CONVERT(VARCHAR(10),'"+data+"',103) ";
					
					rs = stmtMSSQLClubId.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("select clubid from AAMS.GamingRoomSystem", e);
		}
	}
	
	public static int size(Connection conn,Date dEnd) throws DataLayerException{
		try {
			int out = -1;
			String data = DateUtils.dateToString(dEnd,"dd-MM-yyyy HH:mm");
			Statement stmtMSSQLClubId = conn.createStatement();
			String sql = "select count(*) from AAMS.GamingRoomSystem where cessationdate is null and registrationdate <= CONVERT(DATETIME,'"+data+"',103)" ;
			rs = stmtMSSQLClubId.executeQuery(sql);
			while(rs.next())
			  out = rs.getInt(1);
			
			rs.close();
			return out;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DataLayerException("select clubid from AAMS.GamingRoomSystem", e);
		}
	}
	

}
