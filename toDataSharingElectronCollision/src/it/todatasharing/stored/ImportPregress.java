package it.todatasharing.stored;

import it.todatasharing.beans.ImporterBean;
import it.todatasharing.business.Semaphore;
import it.todatasharing.connector.DBConnectionManager;
import it.todatasharing.dao.CashdeskLocationHandpayDAO;
import it.todatasharing.dao.GameGsDAO;
import it.todatasharing.dao.MeterGamesDAO;
import it.todatasharing.dao.MeterVltDAO;
import it.todatasharing.dao.SessioneDAO;
import it.todatasharing.dao.VltLocationDAO;
import it.todatasharing.exception.DataLayerException;
import it.todatasharing.primitive.CashdeskLocationHandpay;
import it.todatasharing.primitive.GameGs;
import it.todatasharing.primitive.MeterGames;
import it.todatasharing.primitive.MeterSogeiReport;
import it.todatasharing.primitive.MeterVlt;
import it.todatasharing.primitive.Sessione;
import it.todatasharing.primitive.VltLocation;
import it.todatasharing.util.DateUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ImportPregress implements Runnable {

	protected static Logger logger = Logger.getLogger(ImportPregress.class);

	Date datestartIni;
	Date datestartFin;
	Date dateEnd;
	Connection connToBackOffice;
	Connection connToDatasharing;
	Connection connToConc;
	Connection connToNucleus;
	ImporterBean importBean;

	/*
	 * private static ImportPregress instance ;
	 * 
	 * 
	 * public static synchronized ImportPregress getInstance() { if (instance ==
	 * null) { synchronized (ImportPregress.class) { instance = new
	 * ImportPregress(); } } return instance; }
	 * 
	 * private ImportPregress(){
	 * 
	 * }
	 */

	public ImportPregress(Date datestartIni, Date datestartFin, Date dateEnd,
			ImporterBean importBean) {
		super();
		this.datestartIni = datestartIni;
		this.datestartFin = datestartFin;
		this.dateEnd = dateEnd;
		this.importBean = importBean;
	}

	public static synchronized void execute(Date datestartIni,
			Date datestartFin, Date dateEnd, Connection connToBackOffice,
			Connection connToDatasharing, Connection connToConc,
			Connection connToNucleus, ImporterBean importBean)
			throws SQLException, DataLayerException {

		while (DateUtils.isDateAfter(dateEnd, datestartIni)) {

			Sessione actS = new Sessione();
			actS.setSTART_DATE(datestartIni);
			actS.setEND_DATE(datestartFin);

			Integer internalPercentage = 0;

			actS.setUNIQUE_SESSION_ID(SessioneDAO.insertSessionImportStart(
					connToDatasharing, actS));
			logger.info("SESSIONE ATTUALE: " + actS);

			logger.debug("COSTRUISCO MAPPE INFO EXTRA");
			HashMap<String, VltLocation> mapVltLocation = VltLocationDAO
					.getVltLocationMap(connToBackOffice, actS.getEND_DATE());
			HashMap<Long, GameGs> mapGame = GameGsDAO
					.getGameMap(connToBackOffice);

			/*
			 * logger.debug("COSTRUISCO MAPPE PREV"); HashMap<String,MeterGames>
			 * mapMeterGamesPrev = MeterGamesDAO.getAllMeterGames(actS,
			 * connToDatasharing); HashMap<String,MeterVlt> mapMeterVltPrev =
			 * MeterVltDAO.getAllMeterVlt(actS, connToDatasharing);
			 * HashMap<String,CashdeskLocationHandpay>
			 * mapCashdeskLocationHandpayPrev =
			 * CashdeskLocationHandpayDAO.getAllCashdeskLocationHandpay(actS,
			 * connToDatasharing);
			 */

			ResultSet rsGamingRoomSystem = GamingRoomSystem.execute(connToConc,
					actS.getEND_DATE());

			int size = GamingRoomSystem.size(connToConc, actS.getEND_DATE());
			int i = 0;

			HashMap<String, MeterGames> mapMeterGamesAct = new HashMap<String, MeterGames>();
			HashMap<String, MeterVlt> mapMeterVltAct = new HashMap<String, MeterVlt>();
			HashMap<String, CashdeskLocationHandpay> mapCashdeskLocationHandpayAct = new HashMap<String, CashdeskLocationHandpay>();

			while (rsGamingRoomSystem.next()) {
				logger.info("FETCHED PREGRESS: " + (i++) + " of " + size);
				internalPercentage = new Integer(Math.round(i * 100 / size) - 1);
				importBean.setPercentage(internalPercentage);

				String clubId = rsGamingRoomSystem.getString(1);
				List<MeterSogeiReport> listMeterSogei = GamesPlayedSelect
						.execute(connToNucleus, actS.getSTART_DATE(),
								actS.getEND_DATE(), clubId);

				for (MeterSogeiReport meterSogei : listMeterSogei) {

					GameGs gameGs = new GameGs();
					if (mapGame.containsKey(meterSogei.getGame_id()))
						gameGs = mapGame.get(meterSogei.getGame_id());

					// PRENDO SOLO TUTTE LE VLT EFFETTIVAMENTE IN ESERCIZIO
					if (mapVltLocation.containsKey(meterSogei.getMachine_cn())) {

						VltLocation vltLoc = new VltLocation();
						vltLoc = mapVltLocation.get(meterSogei.getMachine_cn());

						MeterGames mg = new MeterGames();

						mg.setAams_code_id(vltLoc.getCode_id());
						mg.setAams_game_id(gameGs.getId());
						mg.setAams_location_id(vltLoc.getLocation_id());
						mg.setBet(meterSogei.getTotal_bet());
						mg.setGames_played(meterSogei.getGames_played());
						mg.setGames_won(0);
						mg.setJackpot_contribution(0);
						mg.setJackpot_win(0);
						mg.setMachine_gs_id(vltLoc.getVlt_id());
						mg.setSession_id(actS.getUNIQUE_SESSION_ID());
						mg.setWin(meterSogei.getTotal_win());
						mg.setClub_id(clubId);

						MeterVlt mv = new MeterVlt();

						mv.setAams_code_id(vltLoc.getCode_id());
						mv.setAams_location_id(vltLoc.getLocation_id());
						mv.setBill_in(meterSogei.getTotal_bill_in());
						mv.setCard_in(meterSogei.getTotal_card_in());
						mv.setCoin_in(meterSogei.getTotal_coin_in());
						mv.setMachine_gs_id(vltLoc.getVlt_id());
						mv.setSession_id(actS.getUNIQUE_SESSION_ID());
						mv.setTicket_in(meterSogei.getTotal_ticket_in());
						mv.setTicket_out(meterSogei.getTotal_ticket_out()
								+ meterSogei.getPayouts());
						mv.setTotal_drop(meterSogei.getTotal_ticket_out()
								- meterSogei.getTotal_ticket_in());
						mv.setTotal_in(meterSogei.getTotal_in());
						mv.setTotal_out(meterSogei.getTotal_out());
						mv.setTotal_prepaid_in(0);
						mv.setClub_id(clubId);

						CashdeskLocationHandpay cslh = new CashdeskLocationHandpay();

						cslh.setAams_code_id(vltLoc.getCode_id());
						cslh.setAams_location_id(vltLoc.getLocation_id());
						cslh.setHandpay_date(new Timestamp(actS.getSTART_DATE()
								.getTime()));
						cslh.setHandpay_value(meterSogei.getTotal_handpay());
						cslh.setMachine_gs_id(vltLoc.getVlt_id());
						cslh.setSession_id(actS.getUNIQUE_SESSION_ID());
						cslh.setClub_id(clubId);

						mg.updateHashMap(mapMeterGamesAct);

						mv.updateHashMap(mapMeterVltAct);

						cslh.updateHashMap(mapCashdeskLocationHandpayAct);

					}

				}

			}

			// hash not considerable meter
			HashMap<String, MeterGames> hashNotConsiderableGames = new HashMap<String, MeterGames>();
			HashMap<String, MeterVlt> hashNotConsiderableVlts = new HashMap<String, MeterVlt>();

			ArrayList<MeterGames> listMeterGames = new ArrayList<MeterGames>(
					mapMeterGamesAct.values());

			// new ArrayList<MeterGames>(MeterGames.delta(mapMeterGamesPrev,
			// mapMeterGamesAct).values());

			for (MeterGames mg : listMeterGames) {
				MeterGamesDAO.insert(mg, hashNotConsiderableGames,
						connToDatasharing);
			}

			ArrayList<MeterVlt> listMeterVlt = new ArrayList<MeterVlt>(
					mapMeterVltAct.values());

			// new ArrayList<MeterVlt>(MeterVlt.delta(mapMeterVltPrev,
			// mapMeterVltAct).values());

			for (MeterVlt mv : listMeterVlt) {
				MeterVltDAO.insert(mv, hashNotConsiderableGames,
						hashNotConsiderableVlts, connToDatasharing);
			}

			ArrayList<CashdeskLocationHandpay> listCashdeskLocationHandpay = new ArrayList<CashdeskLocationHandpay>(
					mapCashdeskLocationHandpayAct.values());

			// new
			// ArrayList<CashdeskLocationHandpay>(CashdeskLocationHandpay.delta(mapCashdeskLocationHandpayPrev,
			// mapCashdeskLocationHandpayAct).values());

			for (CashdeskLocationHandpay cslh : listCashdeskLocationHandpay) {
				CashdeskLocationHandpayDAO.insert(cslh,
						hashNotConsiderableGames, hashNotConsiderableVlts,
						connToDatasharing);
			}

			importBean.setPercentage(internalPercentage + 1);
			rsGamingRoomSystem.close();
			connToDatasharing.commit();

			SessioneDAO.insertSessionImportEnd(connToDatasharing, actS, 1);

			logger.info("FINE IMPORT PREGRESSO");

			datestartIni = DateUtils.addDays(datestartIni, 1);
			datestartFin = DateUtils.addDays(datestartFin, 1);
		}

	}

	public static synchronized void execute(Date datestartIni,
			Date datestartFin, Date dateEnd, Connection connToBackOffice,
			Connection connToDatasharing, Connection connToConc,
			Connection connToNucleus) throws SQLException, DataLayerException {

		while (DateUtils.isDateAfter(dateEnd, datestartIni)) {

			Sessione actS = new Sessione();
			actS.setSTART_DATE(datestartIni);
			actS.setEND_DATE(datestartFin);

			actS.setUNIQUE_SESSION_ID(SessioneDAO.insertSessionImportStart(
					connToDatasharing, actS));
			logger.info("SESSIONE ATTUALE: " + actS);

			logger.debug("COSTRUISCO MAPPE INFO EXTRA");
			HashMap<String, VltLocation> mapVltLocation = VltLocationDAO
					.getVltLocationMap(connToBackOffice, actS.getEND_DATE());
			HashMap<Long, GameGs> mapGame = GameGsDAO
					.getGameMap(connToBackOffice);

			/*
			 * logger.debug("COSTRUISCO MAPPE PREV"); HashMap<String,MeterGames>
			 * mapMeterGamesPrev = MeterGamesDAO.getAllMeterGames(actS,
			 * connToDatasharing); HashMap<String,MeterVlt> mapMeterVltPrev =
			 * MeterVltDAO.getAllMeterVlt(actS, connToDatasharing);
			 * HashMap<String,CashdeskLocationHandpay>
			 * mapCashdeskLocationHandpayPrev =
			 * CashdeskLocationHandpayDAO.getAllCashdeskLocationHandpay(actS,
			 * connToDatasharing);
			 */

			ResultSet rsGamingRoomSystem = GamingRoomSystem.execute(connToConc,
					actS.getEND_DATE());

			int size = GamingRoomSystem.size(connToConc, actS.getEND_DATE());
			int i = 0;

			HashMap<String, MeterGames> mapMeterGamesAct = new HashMap<String, MeterGames>();
			HashMap<String, MeterVlt> mapMeterVltAct = new HashMap<String, MeterVlt>();
			HashMap<String, CashdeskLocationHandpay> mapCashdeskLocationHandpayAct = new HashMap<String, CashdeskLocationHandpay>();

			while (rsGamingRoomSystem.next()) {
				logger.info("FETCHED PREGRESS: " + (i++) + " of " + size);

				String clubId = rsGamingRoomSystem.getString(1);
				List<MeterSogeiReport> listMeterSogei = GamesPlayedSelect
						.execute(connToNucleus, actS.getSTART_DATE(),
								actS.getEND_DATE(), clubId);

				for (MeterSogeiReport meterSogei : listMeterSogei) {

					GameGs gameGs = new GameGs();
					if (mapGame.containsKey(meterSogei.getGame_id()))
						gameGs = mapGame.get(meterSogei.getGame_id());

					// PRENDO SOLO TUTTE LE VLT EFFETTIVAMENTE IN ESERCIZIO
					if (mapVltLocation.containsKey(meterSogei.getMachine_cn())) {

						VltLocation vltLoc = new VltLocation();
						vltLoc = mapVltLocation.get(meterSogei.getMachine_cn());

						MeterGames mg = new MeterGames();

						mg.setAams_code_id(vltLoc.getCode_id());
						mg.setAams_game_id(gameGs.getId());
						mg.setAams_location_id(vltLoc.getLocation_id());
						mg.setBet(meterSogei.getTotal_bet());
						mg.setGames_played(meterSogei.getGames_played());
						mg.setGames_won(0);
						mg.setJackpot_contribution(0);
						mg.setJackpot_win(0);
						mg.setMachine_gs_id(vltLoc.getVlt_id());
						mg.setSession_id(actS.getUNIQUE_SESSION_ID());
						mg.setWin(meterSogei.getTotal_win());
						mg.setClub_id(clubId);

						MeterVlt mv = new MeterVlt();

						mv.setAams_code_id(vltLoc.getCode_id());
						mv.setAams_location_id(vltLoc.getLocation_id());
						mv.setBill_in(meterSogei.getTotal_bill_in());
						mv.setCard_in(meterSogei.getTotal_card_in());
						mv.setCoin_in(meterSogei.getTotal_coin_in());
						mv.setMachine_gs_id(vltLoc.getVlt_id());
						mv.setSession_id(actS.getUNIQUE_SESSION_ID());
						mv.setTicket_in(meterSogei.getTotal_ticket_in());
						mv.setTicket_out(meterSogei.getTotal_ticket_out()
								+ meterSogei.getPayouts());
						mv.setTotal_drop(meterSogei.getTotal_ticket_out()
								- meterSogei.getTotal_ticket_in());
						mv.setTotal_in(meterSogei.getTotal_in());
						mv.setTotal_out(meterSogei.getTotal_out());
						mv.setTotal_prepaid_in(0);
						mv.setClub_id(clubId);

						CashdeskLocationHandpay cslh = new CashdeskLocationHandpay();

						cslh.setAams_code_id(vltLoc.getCode_id());
						cslh.setAams_location_id(vltLoc.getLocation_id());
						cslh.setHandpay_date(new Timestamp(actS.getSTART_DATE()
								.getTime()));
						cslh.setHandpay_value(meterSogei.getTotal_handpay());
						cslh.setMachine_gs_id(vltLoc.getVlt_id());
						cslh.setSession_id(actS.getUNIQUE_SESSION_ID());
						cslh.setClub_id(clubId);

						mg.updateHashMap(mapMeterGamesAct);

						mv.updateHashMap(mapMeterVltAct);

						cslh.updateHashMap(mapCashdeskLocationHandpayAct);
					}

				}

			}

			// hash not considerable meter
			HashMap<String, MeterGames> hashNotConsiderableGames = new HashMap<String, MeterGames>();
			HashMap<String, MeterVlt> hashNotConsiderableVlts = new HashMap<String, MeterVlt>();

			ArrayList<MeterGames> listMeterGames = new ArrayList<MeterGames>(
					mapMeterGamesAct.values());

			// new ArrayList<MeterGames>(MeterGames.delta(mapMeterGamesPrev,
			// mapMeterGamesAct).values());

			for (MeterGames mg : listMeterGames) {
				MeterGamesDAO.insert(mg, hashNotConsiderableGames,
						connToDatasharing);
			}

			ArrayList<MeterVlt> listMeterVlt = new ArrayList<MeterVlt>(
					mapMeterVltAct.values());

			// new ArrayList<MeterVlt>(MeterVlt.delta(mapMeterVltPrev,
			// mapMeterVltAct).values());

			for (MeterVlt mv : listMeterVlt) {
				MeterVltDAO.insert(mv, hashNotConsiderableGames,
						hashNotConsiderableVlts, connToDatasharing);
			}

			ArrayList<CashdeskLocationHandpay> listCashdeskLocationHandpay = new ArrayList<CashdeskLocationHandpay>(
					mapCashdeskLocationHandpayAct.values());

			// new
			// ArrayList<CashdeskLocationHandpay>(CashdeskLocationHandpay.delta(mapCashdeskLocationHandpayPrev,
			// mapCashdeskLocationHandpayAct).values());

			for (CashdeskLocationHandpay cslh : listCashdeskLocationHandpay) {
				CashdeskLocationHandpayDAO.insert(cslh,
						hashNotConsiderableGames, hashNotConsiderableVlts,
						connToDatasharing);
			}

			rsGamingRoomSystem.close();
			connToDatasharing.commit();

			SessioneDAO.insertSessionImportEnd(connToDatasharing, actS, 1);

			logger.info("FINE IMPORT PREGRESSO");

			datestartIni = DateUtils.addDays(datestartIni, 1);
			datestartFin = DateUtils.addDays(datestartFin, 1);
		}

	}

	@Override
	public void run() {

		Semaphore s = Semaphore.getInstance();

		try {
			while (s.isSignal()) {
				Thread.sleep(10000);
			}

			s.take();

			connToDatasharing = null;
			connToNucleus = null;
			connToConc = null;
			connToBackOffice = null;

			connToNucleus = DBConnectionManager.nucleusConnectionFactory();
			logger.info("CONNESSO NUC");
			connToConc = DBConnectionManager.concessionaryConnectionFactory();
			logger.info("CONNESSO CON");
			connToBackOffice = DBConnectionManager
					.backofficeConnectionFactory();
			logger.info("CONNESSO BACK");
			connToDatasharing = DBConnectionManager
					.dataSharingConnectionFactory();
			logger.info("CONNESSO DATASHARING");

			connToNucleus.setAutoCommit(false);
			connToConc.setAutoCommit(false);
			connToDatasharing.setAutoCommit(false);

			execute(datestartIni, datestartFin, dateEnd, connToBackOffice,
					connToDatasharing, connToConc, connToNucleus, importBean);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DataLayerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				DBConnectionManager.CloseConnection(connToNucleus);
				DBConnectionManager.CloseConnection(connToConc);
				DBConnectionManager.CloseConnection(connToBackOffice);
				DBConnectionManager.CloseConnection(connToDatasharing);

				s.release();

			} catch (DataLayerException e) {
				e.printStackTrace();
			}

		}
	}
}
