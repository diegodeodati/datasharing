package it.todatasharing.dao;

import it.todatasharing.primitive.Sessione;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class SessioneDAO {

	protected static Logger logger = Logger.getLogger(SessioneDAO.class);

	public static int insertSessionImportStart(Connection connDataSharing,
			Sessione s) throws SQLException {

		logger.info("Start - Insert Sessione Start IMPORT to DataSharing");

		CallableStatement cs = connDataSharing
				.prepareCall("{ call INIT_SESSION (?,?,?) }");
		int session_id = -1;

		cs.setTimestamp(1, new java.sql.Timestamp(s.getSTART_DATE().getTime()));
		cs.setTimestamp(2, new java.sql.Timestamp(s.getEND_DATE().getTime()));
		cs.setLong(3, session_id);

		cs.execute();
		session_id = cs.getInt(3);

		cs.close();

		logger.info("End - Insert Sessione Start IMPORT to DataSharing");

		return session_id;

	}

	public static void insertSessionImportEnd(Connection connDataSharing,
			Sessione s, int exit_code) throws SQLException {

		logger.info("Start - Update Sessione End IMPORT to DataSharing");

		CallableStatement cs = connDataSharing
				.prepareCall("{ call FINISH_SESSION (?,?,?) }");

		cs.setLong(1, s.getUNIQUE_SESSION_ID());
		cs.setInt(2, exit_code);

		cs.execute();

		cs.close();

		logger.info("End - Update Sessione End IMPORT to DataSharing");
	}

	public static Sessione getLastSessionInserted(Connection connDataSharing,
			Sessione actS) throws SQLException {

		logger.info("Start - Get Last_SESSION_ID from DataSharing");

		String sql = "select * from session_log a inner join (" +
				     "select MAX(SESSION_ID) SESSION_ID from session_log " +
				     "WHERE SESSION_SUCCESS=1 AND date(START_DATE) = ? and date(END_DATE) = ?) x " +
				     "on a.SESSION_ID = x.SESSION_ID";

		PreparedStatement ps = connDataSharing.prepareStatement(sql);
		
		ps.setDate(1, new java.sql.Date(actS.getSTART_DATE().getTime()));
		ps.setDate(2, new java.sql.Date(actS.getEND_DATE().getTime()));

		ResultSet resultSetMaxSession = ps.executeQuery();

		Sessione s = new Sessione();
		while (resultSetMaxSession.next()) {
			s.setUNIQUE_SESSION_ID(resultSetMaxSession
					.getLong("SESSION_ID"));
			s.setSTART_DATE(resultSetMaxSession.getTimestamp("START_DATE"));
			s.setEND_DATE(resultSetMaxSession.getTimestamp("END_DATE"));

		}
		
		
		ps.close();
		resultSetMaxSession.close();

		logger.info("End - Get Last_SESSION_ID from DataSharing");

		return s;
	}

}
