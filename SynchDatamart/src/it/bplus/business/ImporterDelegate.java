/**
 * 
 */
package it.bplus.business;

import it.bplus.dao.ClienteDimDAO;
import it.bplus.dao.LocationDAO;
import it.bplus.dao.MeterDAO;
import it.bplus.dao.SessioneDAO;
import it.bplus.dao.SistemaGiocoDimDAO;
import it.bplus.dao.SpazioDimDAO;
import it.bplus.dao.TempoDimDAO;
import it.bplus.dao.VltDAO;
import it.bplus.dao.VltMilionarieDAO;
import it.bplus.db.DBConnectionManager;
import it.bplus.exception.BusinessLayerException;
import it.bplus.exception.DataLayerException;
import it.bplus.primitive.ClienteDim;
import it.bplus.primitive.LocationExtra;
import it.bplus.primitive.Meter;
import it.bplus.primitive.Sessione;
import it.bplus.primitive.SistemaGiocoDim;
import it.bplus.primitive.SpazioDim;
import it.bplus.primitive.TempoDim;
import it.bplus.primitive.VltExtra;
import it.bplus.primitive.VltMilionarie;
import it.bplus.util.DateUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author luca tiberia
 * 
 */
public class ImporterDelegate {

	protected static Logger logger = Logger.getLogger(ImporterDelegate.class);

	public static void doImport() throws BusinessLayerException, ParseException {

		Connection connToAggregate = null;
		Connection connToDatamart = null;
		Sessione actualSession = null;

		try {

			Date d = new java.util.Date();

			connToAggregate = DBConnectionManager.AggregateConnectionFactory();
			connToDatamart = DBConnectionManager.DatamartConnectionFactory();

			connToDatamart.setAutoCommit(false);

			System.out.println("INIZIO IMPORT " + d.toLocaleString());

			/* COSTRUISCO LE MAPPE INTERNE PER l'ASSOCIAZIONE METER - DIMENSIONI */

			System.out.println("SYNC Spazio Dim");
			logger.info("SYNC Spazio Dim");
			HashMap<String, SpazioDim> hashSpazioDimDatamart = SpazioDimDAO
					.getAllSpazioDimDatamart(connToDatamart);
			SpazioDimDAO.insertSpazioDim(hashSpazioDimDatamart,
					connToAggregate, connToDatamart);

			System.out.println("SYNC Cliente Dim");
			logger.info("SYNC  Cliente Dim");
			HashMap<String, ClienteDim> hashClienteDimDatamart = ClienteDimDAO
					.getAllClienteDimDatamart(connToDatamart);
			ClienteDimDAO.insertClienteDim(hashClienteDimDatamart,
					connToAggregate, connToDatamart);

			System.out.println("SYNC Sistema Gioco Dim");
			logger.info("SYNC  Sistema Gioco Dim");
			HashMap<String, SistemaGiocoDim> hashSysDimDatamart = SistemaGiocoDimDAO
					.getAllSistemaGiocoDimDatamart(connToDatamart);

			System.out.println("SYNC Tempo Dim");
			logger.info("SYNC  Tempo Dim");
			HashMap<java.util.Date, TempoDim> hashTempoDimDatamart = TempoDimDAO
					.getAllTempoDimDatamart(connToDatamart);

			HashMap<String, SpazioDim> hashLocationSpazioDim = SpazioDimDAO
					.getAllLocationSpazioDimDatamart(connToDatamart);

			HashMap<String, ClienteDim> hashLocationClienteDim = ClienteDimDAO
					.getAllLocationClienteDimDatamart(connToDatamart);

			HashMap<String, LocationExtra> hashLocationExtra = LocationDAO
					.getAllLocationExtraAggregate(connToDatamart);

			HashMap<String, VltExtra> hashVltExtra = VltDAO
					.getAllVltExtraAggregate(connToAggregate);
			long maxSessionDatamartInspired = SessioneDAO
					.getMaxImportSessionDatamart(connToDatamart, 1711000065);

			System.out.println("MAX SESSION INSPIRED "
					+ maxSessionDatamartInspired);

			ArrayList<Sessione> sessionsInspired = SessioneDAO
					.getSessionsInspired(connToAggregate,
							maxSessionDatamartInspired);

			connToDatamart.setAutoCommit(false);

			for (Sessione s : sessionsInspired) { // System.out.println(s);
				actualSession = s;

				logger.info("Log Start Import");
				SessioneDAO.insertSessionImportStart(s, connToDatamart);

				ArrayList<Meter> mlist = MeterDAO.getMeterInspired(
						connToAggregate, s.getUNIQUE_SESSION_ID());

				for (Meter m : mlist) {
					SistemaGiocoDim sysDim = new SistemaGiocoDim();
					sysDim.setAAMS_GAME_CODE(m.getAams_game_code());
					sysDim.setAAMS_GAMESYSTEM_CODE(m.getAams_game_system_code());
					sysDim.setAAMS_LOCATION_CODE(m.getAams_location_code());
					sysDim.setAAMS_VLT_CODE(m.getAams_vlt_code());

					if (hashVltExtra.containsKey(m.getAams_vlt_code())) {

						sysDim.setGS_VLT_CODE(hashVltExtra
								.get(m.getAams_vlt_code()).getV()
								.getGS_VLT_CODE());

						SistemaGiocoDimDAO.insertSistemaGiocoDim(
								hashSysDimDatamart, sysDim, connToDatamart);
					}

					TempoDimDAO.insertTempoDim(hashTempoDimDatamart,
							m.getData(), connToDatamart);

					MeterDAO.insertMeterInspired(m, hashSpazioDimDatamart,
							hashClienteDimDatamart, hashSysDimDatamart,
							hashTempoDimDatamart, hashLocationSpazioDim,
							hashLocationClienteDim, hashLocationExtra,
							hashVltExtra, connToDatamart);
				}

				SessioneDAO.insertSessionImportEnd(s, 1, connToDatamart);

				
				logger.info("Log Start Generamento Meter Location e Fasce Orarie");

				MeterDAO.generateMeterLocation(connToDatamart, s);
				MeterDAO.generateMeterLocationHour(connToDatamart, s);

				logger.info("Log End Generamento Meter Location e Fasce Orarie");

				connToDatamart.commit();

				logger.info("Log End Import");
			}

			d = new java.util.Date();
			System.out.println("FINE IMPORT INSPIRED " + d.toLocaleString());

			long maxSessionDatamartNovomatic = SessioneDAO
					.getMaxImportSessionDatamart(connToDatamart, 1711000045);

			System.out.println("MAX SESSION NOVOMATIC "
					+ maxSessionDatamartNovomatic);

			ArrayList<Sessione> sessionsNovomatic = SessioneDAO
					.getSessionsNovomatic(connToAggregate,
							maxSessionDatamartNovomatic);

			for (Sessione s : sessionsNovomatic) {
				actualSession = s;

				logger.info("Log Start Import");
				SessioneDAO.insertSessionImportStart(actualSession,
						connToDatamart);

				if (DateUtils.isPregress(actualSession.getSTART_DATE(),
						actualSession.getEND_DATE()))
					MeterDAO.deleteMetersForPregress(actualSession,
							connToDatamart);

				ArrayList<Meter> mlist = MeterDAO.getMeterNovomatic(
						connToAggregate, s.getUNIQUE_SESSION_ID());

				for (Meter m : mlist) {

					// System.out.println(m);
					SistemaGiocoDim sysDim = new SistemaGiocoDim();
					sysDim.setAAMS_GAME_CODE(m.getAams_game_code());
					sysDim.setAAMS_GAMESYSTEM_CODE(m.getAams_game_system_code());
					sysDim.setAAMS_LOCATION_CODE(m.getAams_location_code());
					sysDim.setAAMS_VLT_CODE(m.getAams_vlt_code());

					/*
					 * VINCOLI DI INTEGRITA BACKOFFICE SONO STATI VIOLATI !!!
					 * ATTENZIONE AI MODELLI
					 */
					if (hashVltExtra.containsKey(m.getAams_vlt_code())) {
						sysDim.setGS_VLT_CODE(hashVltExtra
								.get(m.getAams_vlt_code()).getV()
								.getGS_VLT_CODE());

						SistemaGiocoDimDAO.insertSistemaGiocoDim(
								hashSysDimDatamart, sysDim, connToDatamart);
					}

					TempoDimDAO.insertTempoDim(hashTempoDimDatamart,
							m.getData(), connToDatamart);

					MeterDAO.insertMeterNovomatic(m, hashSpazioDimDatamart,
							hashClienteDimDatamart, hashSysDimDatamart,
							hashTempoDimDatamart, hashLocationSpazioDim,
							hashLocationClienteDim, hashLocationExtra,
							hashVltExtra, connToDatamart);
				}

				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(s, 1, connToDatamart);

				//System.out.println("Allineamento VLT Milionarie");
				logger.info("Log Start Check Repair Vlt Milionarie");

				ArrayList<VltMilionarie> listVltMilionarie = new ArrayList<VltMilionarie>();

				if (DateUtils.isPregress(actualSession.getSTART_DATE(),
						actualSession.getEND_DATE()))
					listVltMilionarie = VltMilionarieDAO
							.getAllVltMilionarieDay(connToDatamart,
									actualSession);
				else
					listVltMilionarie = VltMilionarieDAO
							.getAllVltMilionarie(connToDatamart);

				MeterDAO.updateMeterForVltMilionarie(connToDatamart,
						listVltMilionarie, hashLocationExtra, hashVltExtra);

				logger.info("Log End Check Repair Vlt Milionarie");

				logger.info("Log Start Generamento Meter Location e Fasce Orarie");

				MeterDAO.generateMeterLocation(connToDatamart, s);
				MeterDAO.generateMeterLocationHour(connToDatamart, s);

				logger.info("Log End Generamento Meter Location e Fasce Orarie");

				connToDatamart.commit();

			}

			/* IMPORT */

			d = new java.util.Date();
			System.out.println("FINE IMPORT NOVOMATIC " + d.toLocaleString());

		} catch (SQLException e) {

			e.printStackTrace();
			logger.error("Errore SQL nella gestione dell'import" + e.toString());

			try {
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(actualSession, -1,
						connToDatamart);

				connToDatamart.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.error("Errore nella gestione della scrittura del log import"
						+ e.toString());
			}

			throw new BusinessLayerException(
					"Errore SQL nella gestione dell'import", e);
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Errore generico nella gestione dell'import"
					+ e.toString());

			try {
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(actualSession, -1,
						connToDatamart);

				connToDatamart.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.error("Errore nella gestione della scrittura del log import"
						+ e.toString() + e.getStackTrace());
			}

			throw new BusinessLayerException(
					"Errore generico nella gestione dell'import", e);
		}

		finally {
			try {
				DBConnectionManager.CloseConnection(connToAggregate);
				DBConnectionManager.CloseConnection(connToDatamart);
			} catch (DataLayerException e) {

				e.printStackTrace();
				logger.error("DB Connection Close Error : " + e);
			}

		}
	}

}
