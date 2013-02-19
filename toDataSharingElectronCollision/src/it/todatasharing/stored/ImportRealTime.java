package it.todatasharing.stored;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ImportRealTime {

	protected static Logger logger = Logger.getLogger(ImportRealTime.class);

	public static synchronized void execute(Sessione prevS, Sessione actS,
			Connection connToBackOffice, Connection connToDatasharing,
			Connection connToConc, Connection connToNucleus)
			throws SQLException, DataLayerException {
		if (!prevS.equals(actS) || prevS.getUNIQUE_SESSION_ID() == 0) {

			actS.setUNIQUE_SESSION_ID(SessioneDAO.insertSessionImportStart(
					connToDatasharing, actS));
			logger.info("SESSIONE ATTUALE: " + actS);

			logger.info("COSTRUISCO MAPPE INFO EXTRA");
			HashMap<String, VltLocation> mapVltLocation = VltLocationDAO
					.getVltLocationMap(connToBackOffice, actS.getEND_DATE());
			HashMap<Long, GameGs> mapGame = GameGsDAO
					.getGameMap(connToBackOffice);

			logger.info("COSTRUISCO MAPPE PREV");
			HashMap<String, MeterGames> mapMeterGamesPrev = MeterGamesDAO
					.getAllMeterGames(actS, connToDatasharing);
			HashMap<String, MeterVlt> mapMeterVltPrev = MeterVltDAO
					.getAllMeterVlt(actS, connToDatasharing);
			HashMap<String, CashdeskLocationHandpay> mapCashdeskLocationHandpayPrev = CashdeskLocationHandpayDAO
					.getAllCashdeskLocationHandpay(actS, connToDatasharing);

			ResultSet rsGamingRoomSystem = GamingRoomSystem.execute(connToConc,
					actS.getEND_DATE());

			HashMap<String, MeterGames> mapMeterGamesAct = new HashMap<String, MeterGames>();
			HashMap<String, MeterVlt> mapMeterVltAct = new HashMap<String, MeterVlt>();
			HashMap<String, CashdeskLocationHandpay> mapCashdeskLocationHandpayAct = new HashMap<String, CashdeskLocationHandpay>();

			int size = GamingRoomSystem.size(connToConc, actS.getEND_DATE());
			int i = 0;
			while (rsGamingRoomSystem.next()) {
				logger.info("FETCHED:" + (i++) + " of " + size);

				String clubId = rsGamingRoomSystem.getString(1);
				List<MeterSogeiReport> listMeterSogei = GamesPlayedSelect
						.execute(connToNucleus, actS.getSTART_DATE(),
								actS.getEND_DATE(), clubId);

				for (MeterSogeiReport meterSogei : listMeterSogei) {
					// System.out.println(meterSogei);

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
					MeterGames.delta(mapMeterGamesPrev, mapMeterGamesAct)
							.values());

			for (MeterGames mg : listMeterGames) {
				MeterGamesDAO.insert(mg, hashNotConsiderableGames,
						connToDatasharing);
			}

			ArrayList<MeterVlt> listMeterVlt = new ArrayList<MeterVlt>(MeterVlt
					.delta(mapMeterVltPrev, mapMeterVltAct).values());

			for (MeterVlt mv : listMeterVlt) {
				MeterVltDAO.insert(mv, hashNotConsiderableGames,
						hashNotConsiderableVlts, connToDatasharing);
			}

			ArrayList<CashdeskLocationHandpay> listCashdeskLocationHandpay = new ArrayList<CashdeskLocationHandpay>(
					CashdeskLocationHandpay.delta(
							mapCashdeskLocationHandpayPrev,
							mapCashdeskLocationHandpayAct).values());

			for (CashdeskLocationHandpay cslh : listCashdeskLocationHandpay) {
				CashdeskLocationHandpayDAO.insert(cslh,
						hashNotConsiderableGames, hashNotConsiderableVlts,
						connToDatasharing);
			}

			rsGamingRoomSystem.close();
			connToDatasharing.commit();

			SessioneDAO.insertSessionImportEnd(connToDatasharing, actS, 1);

			logger.info("FINE IMPORT");

		} else {
			logger.info("SESSIONE GIA IMPORTATA");
		}
	}
}
