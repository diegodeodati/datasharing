package it.todatasharing.dao;

import it.todatasharing.primitive.MeterGames;
import it.todatasharing.primitive.Sessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class MeterGamesDAO {

	protected static Logger logger = Logger.getLogger(MeterGamesDAO.class);

	public static void insert(MeterGames m,
			HashMap<String, MeterGames> hashNotConsiderableGames,
			Connection connToDatasharing) throws SQLException {

		if (m.isConsiderable()) {

			logger.debug("Start - Insert MeterGames");

			String sql = "INSERT INTO meters_games (SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID,"
					+ "AAMS_GAME_ID,BET,WIN,GAMES_PLAYED,GAMES_WON,JACKPOT_WIN,JACKPOT_CONTRIBUTION) VALUE "
					+ "(?,?,?,?,?,?,?,?,?,?,?)";

			PreparedStatement ps = connToDatasharing.prepareStatement(sql);

			ps.setLong(1, m.getSession_id());
			ps.setString(2, m.getAams_location_id());
			ps.setString(3, m.getAams_code_id());
			ps.setString(4, m.getMachine_gs_id());
			ps.setLong(5, m.getAams_game_id());
			ps.setLong(6, m.getBet());
			ps.setLong(7, m.getWin());
			ps.setLong(8, m.getGames_played());
			ps.setInt(9, m.getGames_won());
			ps.setLong(10, m.getJackpot_win());
			ps.setLong(11, m.getJackpot_contribution());

			ps.executeUpdate();

			ps.close();

			logger.debug("End - Insert MeterGames");
		} else {
			if (!hashNotConsiderableGames.containsKey(m.getAams_location_id()+"-"+m.getAams_code_id())) {

				hashNotConsiderableGames.put(m.getAams_location_id()+"-"+m.getAams_code_id(), m);
			}

		}

	}

	public static void insertForced(MeterGames m, Connection connToDatasharing)
			throws SQLException {

		logger.debug("Start - Insert MeterGames Forced");

		String sql = "INSERT INTO meters_games (SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID,"
				+ "AAMS_GAME_ID,BET,WIN,GAMES_PLAYED,GAMES_WON,JACKPOT_WIN,JACKPOT_CONTRIBUTION) VALUE "
				+ "(?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement ps = connToDatasharing.prepareStatement(sql);

		ps.setLong(1, m.getSession_id());
		ps.setString(2, m.getAams_location_id());
		ps.setString(3, m.getAams_code_id());
		ps.setString(4, m.getMachine_gs_id());
		ps.setLong(5, m.getAams_game_id());
		ps.setLong(6, m.getBet());
		ps.setLong(7, m.getWin());
		ps.setLong(8, m.getGames_played());
		ps.setInt(9, m.getGames_won());
		ps.setLong(10, m.getJackpot_win());
		ps.setLong(11, m.getJackpot_contribution());

		ps.executeUpdate();

		ps.close();

		logger.debug("End - Insert MeterGames Forced");

	}

	public static HashMap<String, MeterGames> getAllMeterGames(Sessione s,
			Connection connToDatasharing) throws SQLException {
		HashMap<String, MeterGames> map = new HashMap<String, MeterGames>();

		String sql = "select v.SESSION_ID,AAMS_LOCATION_ID,AAMS_CODE_ID,MACHINE_GS_ID,AAMS_GAME_ID, "
				+ "SUM(BET) BET,SUM(WIN) WIN,SUM(GAMES_PLAYED) GAMES_PLAYED,SUM(GAMES_WON) GAMES_WON, "
				+ "SUM(JACKPOT_WIN) JACKPOT_WIN, SUM(JACKPOT_CONTRIBUTION) JACKPOT_CONTRIBUTION "
				+ "from `meters_games` v inner join `session_log` s "
				+ "on v.`SESSION_ID` = s.`SESSION_ID` "
				+ "where date(s.`START_DATE`) = ? AND (time(s.`START_DATE`)<>'00:00:00'  or time(s.`END_DATE`)<>'23:59:59') and s.`SESSION_SUCCESS`=1 "
				+ "GROUP BY v.`AAMS_LOCATION_ID`,v.`AAMS_CODE_ID`,v.`AAMS_GAME_ID`";

		PreparedStatement ps = connToDatasharing.prepareStatement(sql);

		ps.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			MeterGames mg = new MeterGames();
			mg.setSession_id(rs.getLong("SESSION_ID"));
			mg.setAams_location_id(rs.getString("AAMS_LOCATION_ID"));
			mg.setAams_code_id(rs.getString("AAMS_CODE_ID"));
			mg.setMachine_gs_id(rs.getString("MACHINE_GS_ID"));
			mg.setAams_game_id(rs.getLong("AAMS_GAME_ID"));
			mg.setBet(rs.getLong("BET"));
			mg.setWin(rs.getLong("WIN"));
			mg.setGames_played(rs.getLong("GAMES_PLAYED"));
			mg.setGames_won(rs.getInt("GAMES_WON"));
			mg.setJackpot_win(rs.getLong("JACKPOT_WIN"));
			mg.setJackpot_contribution(rs.getLong("JACKPOT_CONTRIBUTION"));

			map.put(mg.getKey(), mg);

		}

		ps.close();
		rs.close();

		return map;
	}

}
