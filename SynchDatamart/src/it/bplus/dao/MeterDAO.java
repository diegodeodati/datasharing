package it.bplus.dao;

import it.bplus.primitive.ClienteDim;
import it.bplus.primitive.LocationExtra;
import it.bplus.primitive.Meter;
import it.bplus.primitive.Sessione;
import it.bplus.primitive.SistemaGiocoDim;
import it.bplus.primitive.SpazioDim;
import it.bplus.primitive.TempoDim;
import it.bplus.primitive.Vlt;
import it.bplus.primitive.VltExtra;
import it.bplus.primitive.VltMilionarie;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class MeterDAO {

	protected static Logger logger = Logger.getLogger(MeterDAO.class);

	public static void updateMeterForVltMilionarie(Connection connDatamart,
			ArrayList<VltMilionarie> listVltMilionarie,
			HashMap<String, LocationExtra> hashLocationExtra,
			HashMap<String, VltExtra> hashVltExtra) throws SQLException {

		for (VltMilionarie vMil : listVltMilionarie) {

			updateMeterGamesFromVltMilionarie(connDatamart, vMil,
					hashLocationExtra, hashVltExtra);

			updateMeterVltFromVltMilionarie(connDatamart, vMil,
					hashLocationExtra, hashVltExtra);

			//updateMeterLocationFromVltMilionarie(connDatamart, vMil,
			//		hashLocationExtra, hashVltExtra);

			VltMilionarieDAO.processedVltMilionarie(connDatamart, vMil);

		}

	}

	public static void updateMeterGamesFromVltMilionarie(
			Connection connDatamart, VltMilionarie vMil,
			HashMap<String, LocationExtra> hashLocationExtra,
			HashMap<String, VltExtra> hashVltExtra) throws SQLException {
		String sqlMeterGames = "UPDATE datamart.meter_games SET BET = ?, WIN = ?, PREU = ?, AAMS = ?,"
				+ " NET_WIN = ?, HOUSE_WIN = ?, SUPPLIER_PROFIT = ?, OPERATORS_PROFIT = ?, BPLUS_NET_PROFIT = ?"
				+ " WHERE AAMS_VLT_CODE like ? AND AAMS_LOCATION_CODE like ? AND DATA = ? AND AAMS_GAME_CODE = ?";

		PreparedStatement ps = connDatamart.prepareStatement(sqlMeterGames);

		long[] arr = VltMilionarieDAO.getAamsGameCodeFromVltMilionarie(
				connDatamart, vMil);

		if (arr != null) {

			long calculateBet = vMil.getBet_reale() - (vMil.getBet() - arr[0]);
			long calculateWin = vMil.getWin_reale() - (vMil.getWin() - arr[1]);
			long aams_game_code = arr[2];

			long preu = calculateBet * 4 / 100;
			long aams = calculateBet * 8 / 1000;
			long net_win = calculateBet - calculateWin;
			long house_win = net_win - preu;
			long supplier_profit = 0;
			long operator_profit = 0;

			if (hashVltExtra.containsKey(vMil.getAams_vlt_id()))
				supplier_profit = house_win
						* hashVltExtra.get(vMil.getAams_vlt_id())
								.getPct_supplier().longValue() / 100;

			if (hashLocationExtra.containsKey(vMil.getAams_location_id()))
				operator_profit = house_win
						* (100 - hashLocationExtra
								.get(vMil.getAams_location_id())
								.getPct_concessionario().longValue()) / 100;

			long bplus_net_profit = house_win - supplier_profit
					- operator_profit;

			ps.setLong(1, calculateBet);
			ps.setLong(2, calculateWin);
			ps.setLong(3, preu);
			ps.setLong(4, aams);
			ps.setLong(5, net_win);
			ps.setLong(6, house_win);
			ps.setLong(7, supplier_profit);
			ps.setLong(8, operator_profit);
			ps.setLong(9, bplus_net_profit);

			ps.setString(10, vMil.getAams_vlt_id());
			ps.setString(11, vMil.getAams_location_id());
			ps.setDate(12, new java.sql.Date(vMil.getData().getTime()));
			ps.setLong(13, aams_game_code);

			ps.executeUpdate();

		}

		ps.close();

	}

	public static void updateMeterVltFromVltMilionarie(Connection connDatamart,
			VltMilionarie vMil,
			HashMap<String, LocationExtra> hashLocationExtra,
			HashMap<String, VltExtra> hashVltExtra) throws SQLException {
		String sqlMeterGames = "UPDATE datamart.meter_vlt SET BET = ?, WIN = ?, PREU = ?, AAMS = ?,"
				+ " NET_WIN = ?, HOUSE_WIN = ?, SUPPLIER_PROFIT = ?, OPERATORS_PROFIT = ?, BPLUS_NET_PROFIT = ?"
				+ " WHERE AAMS_VLT_CODE like ? AND AAMS_LOCATION_CODE like ? AND DATA = ?";

		PreparedStatement ps = connDatamart.prepareStatement(sqlMeterGames);

		long calculateBet = vMil.getBet_reale();
		long calculateWin = vMil.getWin_reale();

		long preu = calculateBet * 4 / 100;
		long aams = calculateBet * 8 / 1000;
		long net_win = calculateBet - calculateWin;
		long house_win = net_win - preu;
		long supplier_profit = 0;
		long operator_profit = 0;

		if (hashVltExtra.containsKey(vMil.getAams_vlt_id()))
			supplier_profit = house_win
					* hashVltExtra.get(vMil.getAams_vlt_id()).getPct_supplier()
							.longValue() / 100;

		if (hashLocationExtra.containsKey(vMil.getAams_location_id()))
			operator_profit = house_win
					* (100 - hashLocationExtra.get(vMil.getAams_location_id())
							.getPct_concessionario().longValue()) / 100;

		long bplus_net_profit = house_win - supplier_profit - operator_profit;

		ps.setLong(1, calculateBet);
		ps.setLong(2, calculateWin);
		ps.setLong(3, preu);
		ps.setLong(4, aams);
		ps.setLong(5, net_win);
		ps.setLong(6, house_win);
		ps.setLong(7, supplier_profit);
		ps.setLong(8, operator_profit);
		ps.setLong(9, bplus_net_profit);

		ps.setString(10, vMil.getAams_vlt_id());
		ps.setString(11, vMil.getAams_location_id());
		ps.setDate(12, new java.sql.Date(vMil.getData().getTime()));

		ps.executeUpdate();

		ps.close();

	}

	public static void updateMeterLocationFromVltMilionarie(
			Connection connDatamart, VltMilionarie vMil,
			HashMap<String, LocationExtra> hashLocationExtra,
			HashMap<String, VltExtra> hashVltExtra) throws SQLException {
		String sqlMeterGames = "UPDATE datamart.meter_location SET BET = BET - ?, WIN = WIN - ?, PREU = PREU - ?, AAMS = AAMS - ?,"
				+ " NET_WIN = NET_WIN - ?, HOUSE_WIN = HOUSE_WIN - ?, SUPPLIER_PROFIT = SUPPLIER_PROFIT - ?, OPERATORS_PROFIT = OPERATORS_PROFIT - ?, BPLUS_NET_PROFIT = BPLUS_NET_PROFIT - ?"
				+ " WHERE AAMS_LOCATION_CODE like ? AND DATA = ?";

		PreparedStatement ps = connDatamart.prepareStatement(sqlMeterGames);

		long calculateBet = vMil.getBet_reale();
		long calculateWin = vMil.getWin_reale();

		long preu = calculateBet * 4 / 100;
		long aams = calculateBet * 8 / 1000;
		long net_win = calculateBet - calculateWin;
		long house_win = net_win - preu;
		long supplier_profit = 0;
		long operator_profit = 0;

		if (hashVltExtra.containsKey(vMil.getAams_vlt_id()))
			supplier_profit = house_win
					* hashVltExtra.get(vMil.getAams_vlt_id()).getPct_supplier()
							.longValue() / 100;

		if (hashLocationExtra.containsKey(vMil.getAams_location_id()))
			operator_profit = house_win
					* (100 - hashLocationExtra.get(vMil.getAams_location_id())
							.getPct_concessionario().longValue()) / 100;

		long bplus_net_profit = house_win - supplier_profit - operator_profit;

		ps.setLong(1, calculateBet);
		ps.setLong(2, calculateWin);
		ps.setLong(3, preu);
		ps.setLong(4, aams);
		ps.setLong(5, net_win);
		ps.setLong(6, house_win);
		ps.setLong(7, supplier_profit);
		ps.setLong(8, operator_profit);
		ps.setLong(9, bplus_net_profit);

		ps.setString(10, vMil.getAams_location_id());
		ps.setDate(11, new java.sql.Date(vMil.getData().getTime()));

		ps.executeUpdate();

		ps.close();

	}

	public static void deleteMetersForPregress(Sessione s,
			Connection connDatamart) throws SQLException {

		logger.info("Start - Delete Meters for Pregress");

		ArrayList<Vlt> listDismissedVlt = VltDAO.getAllVltDismissed(
				connDatamart, s);

		String sqlDeleteMeterVlt = "delete m.* from meter_vlt m WHERE date(m.DATA) = date(?) AND AAMS_GAMESYSTEM_CODE = ?";

		if (!listDismissedVlt.isEmpty())
			sqlDeleteMeterVlt = sqlDeleteMeterVlt
					+ " and m.AAMS_VLT_CODE NOT IN  "
					+ Vlt.toParamenterVltID(listDismissedVlt);

		PreparedStatement ps = connDatamart.prepareStatement(sqlDeleteMeterVlt);

		ps.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));
		ps.setLong(2, s.getAAMS_GAME_SYSTEM_CODE());

		ps.executeUpdate();

		ps.close();

		String sqlDeleteMeterGame = "delete m.* from meter_games m WHERE date(m.DATA) = date(?) AND AAMS_GAMESYSTEM_CODE = ?";

		if (!listDismissedVlt.isEmpty())
			sqlDeleteMeterGame = sqlDeleteMeterGame
					+ " and m.AAMS_VLT_CODE NOT IN  "
					+ Vlt.toParamenterVltID(listDismissedVlt);

		ps = connDatamart.prepareStatement(sqlDeleteMeterGame);

		ps.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));
		ps.setLong(2, s.getAAMS_GAME_SYSTEM_CODE());

		ps.executeUpdate();

		ps.close();

		// connDatamart.commit();

		logger.info("End - Delete Meters for Pregress");

	}

	public static void updateMeterSetLastImported(int session_id,
			long aams_game_system_code, Connection connDatamart)
			throws SQLException {

		// connDatamart.setAutoCommit(false);

		logger.info("Start - Update Meter LastImported to Datamart");

		String sqlUpdate = "UPDATE METERFACT SET LAST_IMPORTED=0 WHERE UNIQUE_SESSION_ID = ? AND AAMS_GAMESYSTEM_CODE = ? AND LAST_IMPORTED=1";

		PreparedStatement ps = connDatamart.prepareStatement(sqlUpdate);

		ps.setLong(1, session_id);
		ps.setLong(2, aams_game_system_code);

		ps.executeUpdate();

		ps.close();

		// connDatamart.commit();

		logger.info("End - Update Meter LastImported to Datamart");
	}

	public static ArrayList<Meter> getMeterInspired(
			Connection connectionImport, long maxId) throws SQLException {
		ArrayList<Meter> listMeterInspired = new ArrayList<Meter>();

		logger.info("Start - Get Meter Inspired");

		String sqlImportMeterInspired = "select DATA,ANNO,MESE,GIORNO,ORA,MINUTO,BET,WIN,JACKPOT_WIN,JACKPOT_CONTRIBUTION,"
				+ "TOT_IN,TOT_OUT,GAMES_PLAYED,GAMES_WON,TOTAL_HANDPAY,TOTAL_TICKET_IN,TOTAL_TICKET_OUT,TOTAL_BILL_IN,TOTAL_COIN_IN,TOTAL_PREPAID_IN,TOTAL_CARD_IN,"
				+ "AAMS_GAME_CODE,AAMS_VLT_CODE,AAMS_LOCATION_CODE,AAMS_GAME_SYSTEM_CODE,UNIQUE_SESSION_ID from birsgsmeters where AAMS_GAME_SYSTEM_CODE=1711000065 AND UNIQUE_SESSION_ID="
				+ maxId;

		Statement statementImportMeterInspired = connectionImport
				.createStatement();
		ResultSet resultSetImportMeterInspired = statementImportMeterInspired
				.executeQuery(sqlImportMeterInspired);

		while (resultSetImportMeterInspired.next()) {
			Meter m = new Meter();

			m.setData(new java.util.Date(resultSetImportMeterInspired
					.getTimestamp("DATA").getTime()));
			m.setAnno(resultSetImportMeterInspired.getInt("ANNO"));
			m.setMese(resultSetImportMeterInspired.getInt("MESE"));
			m.setGiorno(resultSetImportMeterInspired.getInt("GIORNO"));
			m.setOra(resultSetImportMeterInspired.getInt("ORA"));
			m.setMinuto(resultSetImportMeterInspired.getInt("MINUTO"));
			m.setBet(resultSetImportMeterInspired.getLong("BET"));
			m.setWin(resultSetImportMeterInspired.getLong("WIN"));
			m.setJackpot_win(resultSetImportMeterInspired
					.getLong("JACKPOT_WIN"));
			m.setJackpot_contribution(resultSetImportMeterInspired
					.getLong("JACKPOT_CONTRIBUTION"));
			m.setTot_in(resultSetImportMeterInspired.getLong("TOT_IN"));
			m.setTot_out(resultSetImportMeterInspired.getLong("TOT_OUT"));
			m.setGames_played(resultSetImportMeterInspired
					.getLong("GAMES_PLAYED"));
			m.setGames_won(resultSetImportMeterInspired.getLong("GAMES_WON"));
			m.setTotal_handpay(resultSetImportMeterInspired
					.getLong("TOTAL_HANDPAY"));
			m.setTotal_ticket_in(resultSetImportMeterInspired
					.getLong("TOTAL_TICKET_IN"));
			m.setTotal_ticket_out(resultSetImportMeterInspired
					.getLong("TOTAL_TICKET_OUT"));
			m.setTotal_bill_in(resultSetImportMeterInspired
					.getLong("TOTAL_BILL_IN"));
			m.setTotal_coin_in(resultSetImportMeterInspired
					.getLong("TOTAL_COIN_IN"));
			m.setTotal_prepaid_in(resultSetImportMeterInspired
					.getLong("TOTAL_PREPAID_IN"));
			m.setTotal_card_in(resultSetImportMeterInspired
					.getLong("TOTAL_CARD_IN"));
			m.setAams_game_code(resultSetImportMeterInspired
					.getLong("AAMS_GAME_CODE"));
			m.setAams_vlt_code(resultSetImportMeterInspired
					.getString("AAMS_VLT_CODE"));
			m.setAams_location_code(resultSetImportMeterInspired
					.getString("AAMS_LOCATION_CODE"));
			m.setAams_game_system_code(resultSetImportMeterInspired
					.getLong("AAMS_GAME_SYSTEM_CODE"));
			m.setUnique_session_id(resultSetImportMeterInspired
					.getLong("UNIQUE_SESSION_ID"));

			listMeterInspired.add(m);
		}

		logger.info("End - Get Meter Inspired");

		statementImportMeterInspired.close();
		resultSetImportMeterInspired.close();
		return listMeterInspired;
	}

	public static ArrayList<Meter> getMeterNovomatic(
			Connection connectionImport, long maxId) throws SQLException {
		ArrayList<Meter> listMeterInspired = new ArrayList<Meter>();

		logger.info("Start - Get Meter Novomatic");

		String sqlImportMeterInspired = "select DATA,ANNO,MESE,GIORNO,ORA,MINUTO,BET,WIN,JACKPOT_WIN,JACKPOT_CONTRIBUTION,"
				+ "TOT_IN,TOT_OUT,GAMES_PLAYED,GAMES_WON,TOTAL_HANDPAY,TOTAL_TICKET_IN,TOTAL_TICKET_OUT,TOTAL_BILL_IN,TOTAL_COIN_IN,TOTAL_PREPAID_IN,TOTAL_CARD_IN,"
				+ "AAMS_GAME_CODE,AAMS_VLT_CODE,AAMS_LOCATION_CODE,AAMS_GAME_SYSTEM_CODE,UNIQUE_SESSION_ID from birsgsmeters where AAMS_GAME_SYSTEM_CODE=1711000045 AND UNIQUE_SESSION_ID="
				+ maxId;

		Statement statementImportMeterInspired = connectionImport
				.createStatement();
		ResultSet resultSetImportMeterInspired = statementImportMeterInspired
				.executeQuery(sqlImportMeterInspired);

		while (resultSetImportMeterInspired.next()) {
			Meter m = new Meter();

			m.setData(new java.util.Date(resultSetImportMeterInspired
					.getTimestamp("DATA").getTime()));
			m.setAnno(resultSetImportMeterInspired.getInt("ANNO"));
			m.setMese(resultSetImportMeterInspired.getInt("MESE"));
			m.setGiorno(resultSetImportMeterInspired.getInt("GIORNO"));
			m.setOra(resultSetImportMeterInspired.getInt("ORA"));
			m.setMinuto(resultSetImportMeterInspired.getInt("MINUTO"));
			m.setBet(resultSetImportMeterInspired.getLong("BET"));
			m.setWin(resultSetImportMeterInspired.getLong("WIN"));
			m.setJackpot_win(resultSetImportMeterInspired
					.getLong("JACKPOT_WIN"));
			m.setJackpot_contribution(resultSetImportMeterInspired
					.getLong("JACKPOT_CONTRIBUTION"));
			m.setTot_in(resultSetImportMeterInspired.getLong("TOT_IN"));
			m.setTot_out(resultSetImportMeterInspired.getLong("TOT_OUT"));
			m.setGames_played(resultSetImportMeterInspired
					.getLong("GAMES_PLAYED"));
			m.setGames_won(resultSetImportMeterInspired.getLong("GAMES_WON"));
			m.setTotal_handpay(resultSetImportMeterInspired
					.getLong("TOTAL_HANDPAY"));
			m.setTotal_ticket_in(resultSetImportMeterInspired
					.getLong("TOTAL_TICKET_IN"));
			m.setTotal_ticket_out(resultSetImportMeterInspired
					.getLong("TOTAL_TICKET_OUT"));
			m.setTotal_bill_in(resultSetImportMeterInspired
					.getLong("TOTAL_BILL_IN"));
			m.setTotal_coin_in(resultSetImportMeterInspired
					.getLong("TOTAL_COIN_IN"));
			m.setTotal_prepaid_in(resultSetImportMeterInspired
					.getLong("TOTAL_PREPAID_IN"));
			m.setTotal_card_in(resultSetImportMeterInspired
					.getLong("TOTAL_CARD_IN"));
			m.setAams_game_code(resultSetImportMeterInspired
					.getLong("AAMS_GAME_CODE"));
			m.setAams_vlt_code(resultSetImportMeterInspired
					.getString("AAMS_VLT_CODE"));
			m.setAams_location_code(resultSetImportMeterInspired
					.getString("AAMS_LOCATION_CODE"));
			m.setAams_game_system_code(resultSetImportMeterInspired
					.getLong("AAMS_GAME_SYSTEM_CODE"));
			m.setUnique_session_id(resultSetImportMeterInspired
					.getLong("UNIQUE_SESSION_ID"));

			listMeterInspired.add(m);
		}

		logger.info("End - Get Meter Novomatic");

		statementImportMeterInspired.close();
		resultSetImportMeterInspired.close();
		return listMeterInspired;
	}

	public static void insertMeterInspired(Meter m,
			HashMap<String, SpazioDim> hashSpazioDimDatamart,
			HashMap<String, ClienteDim> hashClienteDimDatamart,
			HashMap<String, SistemaGiocoDim> hashSysDimDatamart,
			HashMap<java.util.Date, TempoDim> hashTempoDimDatamart,
			HashMap<String, SpazioDim> hashLocationSpazioDim,
			HashMap<String, ClienteDim> hashLocationClienteDim,
			HashMap<String, LocationExtra> hashLocationExtra,
			HashMap<String, VltExtra> hashVltExtra, Connection connExport)
			throws SQLException {

		// connExport.setAutoCommit(false);

		String sqlInsertMeterInspired = "INSERT INTO METERFACT (BET,WIN,GAMES_PLAYED,GAMES_WON,TOTAL_IN,TOTAL_OUT,"
				+ "TICKET_IN,TICKET_OUT,COIN_IN,BILL_IN,PREPAID_IN,CARD_IN,HANDPAY,"
				+ "JACKPOT_WIN,JACKPOT_CONTRIBUTION,"
				+ "PREU,AAMS,NET_WIN,HOUSE_WIN,SUPPLIER_PROFIT,OPERATORS_PROFIT,BPLUS_NET_PROFIT,"
				+ "DMCLIENTE_ID,DMSPAZIO_ID,DMTEMPO_ID,DMSISTEMAGIOCO_ID,UNIQUE_SESSION_ID,DATA,AAMS_GAMESYSTEM_CODE,AAMS_LOCATION_CODE,AAMS_VLT_CODE,GS_VLT_CODE,AAMS_GAME_CODE)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = connExport
				.prepareStatement(sqlInsertMeterInspired);

		logger.debug("Start - Insert Meter Inspired to Datamart");

		ps.setLong(1, m.getBet());
		ps.setLong(2, m.getWin());
		ps.setLong(3, m.getGames_played());
		ps.setLong(4, m.getGames_won());
		ps.setLong(5, m.getTot_in());
		ps.setLong(6, m.getTot_out());
		ps.setLong(7, m.getTotal_ticket_in());
		ps.setLong(8, m.getTotal_ticket_out());
		ps.setLong(9, m.getTotal_coin_in());
		ps.setLong(10, m.getTotal_bill_in());
		ps.setLong(11, m.getTotal_prepaid_in());
		ps.setLong(12, m.getTotal_card_in());
		ps.setLong(13, m.getTotal_handpay());
		ps.setLong(14, m.getJackpot_win());
		ps.setLong(15, m.getJackpot_contribution());

		long preu = m.getBet() * 4 / 100;
		long aams = m.getBet() * 8 / 1000;
		long net_win = m.getBet() - m.getWin();
		long house_win = net_win - preu - aams - m.getJackpot_contribution();
		long supplier_profit = 0;
		long operator_profit = 0;

		if (hashVltExtra.containsKey(m.getAams_vlt_code()))
			supplier_profit = house_win
					* hashVltExtra.get(m.getAams_vlt_code()).getPct_supplier()
							.longValue() / 100;

		if (hashLocationExtra.containsKey(m.getAams_location_code()))
			operator_profit = house_win
					* (100 - hashLocationExtra.get(m.getAams_location_code())
							.getPct_concessionario().longValue()) / 100;

		long bplus_net_profit = house_win - supplier_profit - operator_profit;

		ps.setLong(16, preu); // PREU
		ps.setLong(17, aams); // AAMS
		ps.setLong(18, net_win); // NET_WIN
		ps.setLong(19, house_win); // HOUSE_WIN
		ps.setLong(20, supplier_profit); // SUPPLIER_PROFIT
		ps.setLong(21, operator_profit); // OPERATOR_PROFIT
		ps.setLong(22, bplus_net_profit); // BPLUS_NET_PROFIT

		// SE LA SALA E' IN VALIDAZIONE VERIFICA o ALTRO che non sia IN
		// ESERCIZIO
		if (hashLocationClienteDim.containsKey(m.getAams_location_code())) {

			String cod_gestore = hashLocationClienteDim.get(
					m.getAams_location_code()).getCOD_GESTORE();
			long cod_esercente = hashLocationClienteDim.get(
					m.getAams_location_code()).getCOD_ESERCENTE();

			String cadastral_code = hashLocationExtra
					.get(m.getAams_location_code()).getL().getCADASTRAL_CODE();

			long DMCLIENTE_ID = hashClienteDimDatamart.get(
					cod_esercente + "-" + cod_gestore).getId();
			long DMSPAZIO_ID = hashSpazioDimDatamart.get(cadastral_code)
					.getId();
			long DMTEMPO_ID = hashTempoDimDatamart.get(m.getData()).getId();

			long DMSISTEMAGIOCO_ID = hashSysDimDatamart.get(
					m.getAams_location_code() + "-" + m.getAams_vlt_code()
							+ "-" + m.getAams_game_code()).getId();
			long UNIQUE_SESSION_ID = m.getUnique_session_id();

			long aams_game_system_code = m.getAams_game_system_code();
			String aams_location_code = m.getAams_location_code();
			String aams_vlt_code = m.getAams_vlt_code();
			String gs_vlt_code = hashVltExtra.get(m.getAams_vlt_code()).getV()
					.getGS_VLT_CODE();
			long aams_game_code = m.getAams_game_code();

			ps.setLong(23, DMCLIENTE_ID);
			ps.setLong(24, DMSPAZIO_ID);
			ps.setLong(25, DMTEMPO_ID);
			ps.setLong(26, DMSISTEMAGIOCO_ID);
			ps.setLong(27, UNIQUE_SESSION_ID);
			ps.setDate(28, new java.sql.Date(m.getData().getTime()));

			ps.setLong(29, aams_game_system_code);
			ps.setString(30, aams_location_code);
			ps.setString(31, aams_vlt_code);
			ps.setString(32, gs_vlt_code);
			ps.setLong(33, aams_game_code);

			ps.executeUpdate();

		}

		ps.close();

		logger.debug("End - Insert Meter Inspired to Datamart");
	}

	public static void insertMeterNovomatic(Meter m,
			HashMap<String, SpazioDim> hashSpazioDimDatamart,
			HashMap<String, ClienteDim> hashClienteDimDatamart,
			HashMap<String, SistemaGiocoDim> hashSysDimDatamart,
			HashMap<java.util.Date, TempoDim> hashTempoDimDatamart,
			HashMap<String, SpazioDim> hashLocationSpazioDim,
			HashMap<String, ClienteDim> hashLocationClienteDim,
			HashMap<String, LocationExtra> hashLocationExtra,
			HashMap<String, VltExtra> hashVltExtra, Connection connExport)
			throws SQLException, ParseException {

		// connExport.setAutoCommit(false);

		String sqlInsertMeterInspired = "INSERT IGNORE INTO METERFACT (BET,WIN,GAMES_PLAYED,GAMES_WON,TOTAL_IN,TOTAL_OUT,"
				+ "TICKET_IN,TICKET_OUT,COIN_IN,BILL_IN,PREPAID_IN,CARD_IN,HANDPAY,"
				+ "JACKPOT_WIN,JACKPOT_CONTRIBUTION,"
				+ "PREU,AAMS,NET_WIN,HOUSE_WIN,SUPPLIER_PROFIT,OPERATORS_PROFIT,BPLUS_NET_PROFIT,"
				+ "DMCLIENTE_ID,DMSPAZIO_ID,DMTEMPO_ID,DMSISTEMAGIOCO_ID,UNIQUE_SESSION_ID,DATA,AAMS_GAMESYSTEM_CODE,AAMS_LOCATION_CODE,AAMS_VLT_CODE,GS_VLT_CODE,AAMS_GAME_CODE)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = connExport
				.prepareStatement(sqlInsertMeterInspired);

		logger.debug("Start - Insert Meter Novomatic to Datamart");

		ps.setLong(1, m.getBet());
		ps.setLong(2, m.getWin());
		ps.setLong(3, m.getGames_played());
		ps.setLong(4, m.getGames_won());
		ps.setLong(5, m.getTot_in());
		ps.setLong(6, m.getTot_out());
		ps.setLong(7, m.getTotal_ticket_in());
		ps.setLong(8, m.getTotal_ticket_out());
		ps.setLong(9, m.getTotal_coin_in());
		ps.setLong(10, m.getTotal_bill_in());
		ps.setLong(11, m.getTotal_prepaid_in());
		ps.setLong(12, m.getTotal_card_in());
		ps.setLong(13, m.getTotal_handpay());
		ps.setLong(14, m.getJackpot_win());
		ps.setLong(15, m.getJackpot_contribution());

		long preu = m.getBet() * 4 / 100;
		long aams = m.getBet() * 8 / 1000;
		long net_win = m.getBet() - m.getWin();
		long house_win = net_win - preu - aams - m.getJackpot_contribution();
		long supplier_profit = 0;
		long operator_profit = 0;

		if (hashVltExtra.containsKey(m.getAams_vlt_code()))
			supplier_profit = house_win
					* hashVltExtra.get(m.getAams_vlt_code()).getPct_supplier()
							.longValue() / 100;

		if (hashLocationExtra.containsKey(m.getAams_location_code()))
			operator_profit = house_win
					* (100 - hashLocationExtra.get(m.getAams_location_code())
							.getPct_concessionario().longValue()) / 100;

		long bplus_net_profit = house_win - supplier_profit - operator_profit;

		ps.setLong(16, preu); // PREU
		ps.setLong(17, aams); // AAMS
		ps.setLong(18, net_win); // NET_WIN
		ps.setLong(19, house_win); // HOUSE_WIN
		ps.setLong(20, supplier_profit); // SUPPLIER_PROFIT
		ps.setLong(21, operator_profit); // OPERATOR_PROFIT
		ps.setLong(22, bplus_net_profit); // BPLUS_NET_PROFIT

		// SE LA SALA E' IN VALIDAZIONE VERIFICA o ALTRO che non sia IN
		// ESERCIZIO
		if (hashLocationClienteDim.containsKey(m.getAams_location_code())) {

			String cod_gestore = hashLocationClienteDim.get(
					m.getAams_location_code()).getCOD_GESTORE();
			long cod_esercente = hashLocationClienteDim.get(
					m.getAams_location_code()).getCOD_ESERCENTE();

			String cadastral_code = hashLocationExtra
					.get(m.getAams_location_code()).getL().getCADASTRAL_CODE();

			long DMCLIENTE_ID = hashClienteDimDatamart.get(
					cod_esercente + "-" + cod_gestore).getId();
			long DMSPAZIO_ID = hashSpazioDimDatamart.get(cadastral_code)
					.getId();

			long DMTEMPO_ID = hashTempoDimDatamart.get(m.getData()).getId();

			long DMSISTEMAGIOCO_ID = hashSysDimDatamart.get(
					m.getAams_location_code() + "-" + m.getAams_vlt_code()
							+ "-" + m.getAams_game_code()).getId();
			long UNIQUE_SESSION_ID = m.getUnique_session_id();

			long aams_game_system_code = m.getAams_game_system_code();
			String aams_location_code = m.getAams_location_code();
			String aams_vlt_code = m.getAams_vlt_code();
			String gs_vlt_code = hashVltExtra.get(m.getAams_vlt_code()).getV()
					.getGS_VLT_CODE();
			long aams_game_code = m.getAams_game_code();

			ps.setLong(23, DMCLIENTE_ID);
			ps.setLong(24, DMSPAZIO_ID);
			ps.setLong(25, DMTEMPO_ID);
			ps.setLong(26, DMSISTEMAGIOCO_ID);
			ps.setLong(27, UNIQUE_SESSION_ID);
			ps.setDate(28, new java.sql.Date(m.getData().getTime()));

			ps.setLong(29, aams_game_system_code);
			ps.setString(30, aams_location_code);
			ps.setString(31, aams_vlt_code);
			ps.setString(32, gs_vlt_code);
			ps.setLong(33, aams_game_code);

			ps.executeUpdate();

		}

		ps.close();

		logger.debug("End - Insert Meter Novomatic to Datamart");
	}

	public static void generateMeterLocation(Connection connToDatamart,
			Sessione s) throws SQLException {

		logger.info("Start - Generate Meter Location");

		CallableStatement cs = connToDatamart
				.prepareCall("{ CALL DATAMART.GENERATE_METER_LOCATION( ?, ?, ? ) }");

		cs.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));
		cs.setDate(2, new java.sql.Date(s.getEND_DATE().getTime()));
		cs.setLong(3, s.getAAMS_GAME_SYSTEM_CODE());

		cs.execute();

		cs.close();

		logger.info("End - Generate Meter Location");

	}

	public static void generateMeterLocationHour(Connection connToDatamart,
			Sessione s) throws SQLException {

		logger.info("Start - Generate Meter Location Hour");

		CallableStatement cs = connToDatamart
				.prepareCall("{ CALL DATAMART.GENERATE_METER_LOCATION_HOUR ( ?, ?, ?) }");

		cs.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));
		cs.setDate(2, new java.sql.Date(s.getEND_DATE().getTime()));
		cs.setLong(3, s.getAAMS_GAME_SYSTEM_CODE());

		cs.execute();

		cs.close();

		logger.info("End - Generate Meter Location Hour");

	}

}
