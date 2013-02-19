/**
 * 
 */
package it.bplus.business;

import it.bplus.dao.ContrattoDAO;
import it.bplus.dao.EsercenteDAO;
import it.bplus.dao.GameDAO;
import it.bplus.dao.GestoreDAO;
import it.bplus.dao.LocationDAO;
import it.bplus.dao.MaintenanceDAO;
import it.bplus.dao.MeterDAO;
import it.bplus.dao.SeicentoDetailsDAO;
import it.bplus.dao.SeicentoLocDAO;
import it.bplus.dao.SeicentoVltDAO;
import it.bplus.dao.SessioneDAO;
import it.bplus.dao.VltDAO;
import it.bplus.dao.VltGameDAO;
import it.bplus.dao.VltHistoryDAO;
import it.bplus.dao.VltMilionarieDAO;
import it.bplus.db.DBConnectionManager;
import it.bplus.exception.BusinessLayerException;
import it.bplus.primitive.Sessione;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author luca tiberia
 * 
 */
public class ImporterDelegate {

	protected static Logger logger = Logger.getLogger(ImporterDelegate.class);

	public static void doImport() throws BusinessLayerException, ParseException {

		Connection connToBackOffice = null;
		Connection connToAggregate = null;
		Connection connToDataSharingInspired = null;
		Connection connToDataSharingNovomatic = null;
		Sessione actualSession = null;

		try {

			Date d = new java.util.Date();
			System.out.println("INIZIO SYNC " + d.toLocaleString());
			connToBackOffice = DBConnectionManager
					.BackOfficeConnectionFactory();
			
			
			
			connToAggregate = DBConnectionManager.AggregateConnectionFactory();			
			
			connToDataSharingInspired = DBConnectionManager
					.DataSharingInspiredConnectionFactory();				
			
			connToDataSharingNovomatic = DBConnectionManager
					.DataSharingNovomaticConnectionFactory();
			
			

			logger.info("Log Start Sync");
			SessioneDAO.insertSessionSyncStart(connToAggregate);

			System.out.println("SYNC Location");
			logger.info("SYNC Location");
			LocationDAO.insertLocation(connToBackOffice, connToAggregate);
			System.out.println("SYNC Vlt");
			logger.info("SYNC Vlt");
			VltDAO.insertVlt(connToBackOffice, connToAggregate);
			System.out.println("SYNC Maintenance");
			logger.info("SYNC Maintenance");
			MaintenanceDAO.insertMaintenance(connToBackOffice, connToAggregate);
			System.out.println("SYNC Vlt History");
			logger.info("SYNC Vlt History");
			VltHistoryDAO.insertVltHistory(connToBackOffice, connToAggregate);
			logger.info("SYNC Vlt Game");
			VltGameDAO.insertVltGame(connToBackOffice, connToAggregate);
			System.out.println("SYNC Games");
			logger.info("SYNC Games");
			GameDAO.insertGame(connToBackOffice, connToAggregate);
			System.out.println("SYNC 600 Loc");
			logger.info("SYNC 600 Loc");
			SeicentoLocDAO.insertSeicento(connToBackOffice, connToAggregate);			
			System.out.println("SYNC 600 Vlt");
			logger.info("SYNC 600 Vlt");
			SeicentoVltDAO.insertSeicento(connToBackOffice, connToAggregate);
			System.out.println("SYNC 600 DETAILS");
			logger.info("SYNC 600 DETAILS");
			SeicentoDetailsDAO.insertSeicentoDetails(connToBackOffice,
					connToAggregate);
			System.out.println("SYNC GESTORE");
			logger.info("SYNC GESTORE");
			GestoreDAO.insertGestore(connToBackOffice, connToAggregate);
			System.out.println("SYNC ESERCENTI");
			logger.info("SYNC ESERCENTI");
			EsercenteDAO.insertEsercente(connToBackOffice, connToAggregate);
			System.out.println("SYNC CONTRATTI");
			logger.info("SYNC CONTRATTI");
			ContrattoDAO.insertContratto(connToBackOffice, connToAggregate);
			logger.info("SYNC VLT MILIONARIE");
			VltMilionarieDAO.insertVltMilionarie(connToBackOffice,connToAggregate);
			

			logger.info("Log End Sync");
			long maxSession = SessioneDAO
					.getMaxSyncSessionAggregate(connToAggregate);
		
			SessioneDAO.insertSessionSyncEnd(maxSession, 1, connToAggregate);

			System.out.println("FINE SYNC");

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Errore SQL nella gestione del sync" + e.toString());

			System.out.println("ERRORE SQL");

			try {
				logger.info("Log End Sync");
				long maxSession = SessioneDAO
						.getMaxSyncSessionAggregate(connToAggregate);
				SessioneDAO
						.insertSessionSyncEnd(maxSession, 0, connToAggregate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Errore SQL nella gestione della scrittura del log sync"
						+ e.toString());
			}

			throw new BusinessLayerException(
					"Errore generico nella gestione del sync", e);
		} catch (Exception e) {

			System.out.println("ERRORE GENERICO");
			e.printStackTrace();
			logger.error("Errore generico nella gestione del sync"
					+ e.toString());

			try {
				logger.info("Log End Sync");
				long maxSession = SessioneDAO
						.getMaxSyncSessionAggregate(connToAggregate);
				SessioneDAO
						.insertSessionSyncEnd(maxSession, 0, connToAggregate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Errore Generico nella gestione della scrittura del log sync"
						+ e.toString());
			}
			
			throw new BusinessLayerException(
					"Errore generico nella gestione del sync", e);
		}

		//INSPIRED
	
		try {

			long maxSessionAggregateInspired = SessioneDAO
					.getMaxImportSessionAggregate(connToAggregate, 1711000065);
			ArrayList<Sessione> sessionsInspired = SessioneDAO
					.getSessionsInspired(connToDataSharingInspired,
							maxSessionAggregateInspired);

			logger.info("INIZIO IMPORT INSPIRED");

			for (Sessione s : sessionsInspired) {
				System.out.println(s);
				actualSession = s;

				logger.info("Log Start Import");
				SessioneDAO.insertSessionImportStart(s, connToAggregate);

				logger.info("IMPORT METERS");
				MeterDAO.insertMeterInspired(connToDataSharingInspired,
						connToAggregate, s.getUNIQUE_SESSION_ID());

				MeterDAO.insertMeterVltToFill(connToAggregate, s);
	
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(s, 1, connToAggregate);
			}

			Date d = new java.util.Date();
			logger.info("FINE IMPORT INSPIRED " + d.toLocaleString());
		} catch (SQLException e) {

			System.out.println("ERRORE SQL");
			e.printStackTrace();
			logger.error("Errore SQL nella gestione dell'import" + e.toString());

			try {
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(actualSession, 0,
						connToAggregate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Errore nella gestione della scrittura del log import"
						+ e.toString());
			}

			throw new BusinessLayerException(
					"Errore generico nella gestione dell'import", e);
		} catch (Exception e) {

			System.out.println("ERRORE GENERICO");

			e.printStackTrace();
			logger.error("Errore generico nella gestione dell'import"
					+ e.toString());
			
			try {
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(actualSession, 0,
						connToAggregate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Errore nella gestione della scrittura del log import"
						+ e.toString());
			}
			

			throw new BusinessLayerException(
					"Errore generico nella gestione dell'import", e);
		}
		
		//NOVOMATIC
		
		try {

			long maxSessionAggregateNovomatic = SessioneDAO
					.getMaxImportSessionAggregate(connToAggregate, 1711000045);
			ArrayList<Sessione> sessionsNovomatic = SessioneDAO
					.getSessionsNovomatic(connToDataSharingNovomatic,
							maxSessionAggregateNovomatic);

			logger.info("INIZIO IMPORT NOVOMATIC");

			for (Sessione s : sessionsNovomatic) {
				System.out.println(s);
				actualSession = s;

				logger.info("Log Start Import");
				SessioneDAO.insertSessionImportStart(s, connToAggregate);

				logger.info("IMPORT METERS");
				MeterDAO.insertMeterNovomatic(connToDataSharingNovomatic,
						connToAggregate, s.getUNIQUE_SESSION_ID());
				
				
				logger.info("IMPORT METERS VLT TO FILL");
				MeterDAO.insertMeterVltToFill(connToAggregate, s);
				
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(s, 1, connToAggregate);
			}

			Date d = new java.util.Date();
			logger.info("FINE IMPORT NOVOMATIC " + d.toLocaleString());
		} catch (SQLException e) {

			System.out.println("ERRORE SQL");
			e.printStackTrace();
			logger.error("Errore SQL nella gestione dell'import" + e.toString());

			try {
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(actualSession, 0,
						connToAggregate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Errore nella gestione della scrittura del log import"
						+ e.toString());
			}

			throw new BusinessLayerException(
					"Errore generico nella gestione dell'import", e);
		} catch (Exception e) {

			System.out.println("ERRORE GENERICO");

			e.printStackTrace();
			logger.error("Errore generico nella gestione dell'import"
					+ e.toString());
			
			try {
				logger.info("Log End Import");
				SessioneDAO.insertSessionImportEnd(actualSession, 0,
						connToAggregate);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("Errore nella gestione della scrittura del log import"
						+ e.toString());
			}
			

			throw new BusinessLayerException(
					"Errore generico nella gestione dell'import", e);
		}

		finally {
			try {
				DBConnectionManager.CloseConnection(connToBackOffice);
				DBConnectionManager.CloseConnection(connToAggregate);
				DBConnectionManager.CloseConnection(connToDataSharingInspired);
				DBConnectionManager.CloseConnection(connToDataSharingNovomatic);
				
			} catch (Exception e) {

				e.printStackTrace();
				logger.error("DB Connection Close Error : " + e);
			}

		}
	}

}
