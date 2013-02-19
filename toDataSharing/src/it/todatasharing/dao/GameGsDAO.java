package it.todatasharing.dao;

import it.todatasharing.primitive.GameGs;
import it.todatasharing.primitive.Sessione;
import it.todatasharing.primitive.VltLocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class GameGsDAO {
	
	protected static Logger logger = Logger.getLogger(GameGsDAO.class);
	
	public static HashMap<Long,GameGs> getGameMap(Connection connBackOffice) throws SQLException {

		logger.info("Start - Get Game Map from BackOffice");

		String sql = "select id,id_game_gs from sc_game b where b.gs_id like '1711000045'";

		PreparedStatement ps = connBackOffice.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		HashMap<Long,GameGs> map = new HashMap<Long, GameGs>();
		
		while (rs.next()) {
			GameGs gameGs = new GameGs();
			
			gameGs.setId(rs.getLong("id"));
			gameGs.setId_game_gs(rs.getLong("id_game_gs"));
			
			
			map.put(rs.getLong("id_game_gs"),gameGs);
		}

		logger.info("End - Get Game Map from BackOffice");

		ps.close();
		rs.close();
		
		return map;
	}

}
