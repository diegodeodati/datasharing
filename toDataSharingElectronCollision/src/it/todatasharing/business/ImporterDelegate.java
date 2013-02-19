/**
 * 
 */
package it.todatasharing.business;



import it.todatasharing.connector.DBConnectionManager;
import it.todatasharing.dao.SessioneDAO;
import it.todatasharing.exception.DataLayerException;
import it.todatasharing.primitive.Sessione;
import it.todatasharing.stored.ImportPregress;
import it.todatasharing.stored.ImportRealTime;
import it.todatasharing.util.Constants;
import it.todatasharing.util.DateUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * @author luca tiberia
 *
 */
public class ImporterDelegate {	
	
	protected static Logger logger = Logger.getLogger(ImporterDelegate.class);
	private static ResourceBundle resource = ResourceBundle.getBundle(Constants.TODATASHARING_KEY);

	public static void doImport() throws  ParseException, DataLayerException, SQLException, InterruptedException{
		
		Semaphore s = Semaphore.getInstance();
		
		
		while(s.isSignal()){
			Thread.sleep(10000);
		}
		
		s.take();
		
		Date date = new Date(new java.util.Date().getTime());		
		
		int m = DateUtils.minute(date);
		int diff = (m % 30);
		java.util.Date dStart = DateUtils.addMinuteAndSecond(date, -diff-30,0);        
		java.util.Date dEnd = DateUtils.addMinuteAndSecond(date, -diff-1,59);

		java.util.Date dStartUTC = DateUtils.toDateUTC(dStart);
		java.util.Date dEndUTC = DateUtils.toDateUTC(dEnd);
		
		Connection connToDatasharing=null;
		Connection connToNucleus=null;
		Connection connToConc=null;
		Connection connToBackOffice = null;
		//long session_id =-1; ???
		
		
		
		
		try {
			
			
			connToNucleus=DBConnectionManager.nucleusConnectionFactory();
			logger.info("CONNESSO NUC");			
			connToConc = DBConnectionManager.concessionaryConnectionFactory();
			logger.info("CONNESSO CON");	
			connToBackOffice = DBConnectionManager.backofficeConnectionFactory();
			logger.info("CONNESSO BACK");	
			connToDatasharing=DBConnectionManager.dataSharingConnectionFactory();
			logger.info("CONNESSO DATASHARING");	
			
			
			
			connToNucleus.setAutoCommit(false);
			connToConc.setAutoCommit(false);
			connToDatasharing.setAutoCommit(false);
			
			//connToBackOffice.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connToDatasharing.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			Sessione actS = new Sessione();
			actS.setSTART_DATE(dStartUTC);
			actS.setEND_DATE(dEndUTC);
			
			
			Sessione prevS = SessioneDAO.getLastSessionInserted(connToDatasharing, actS);
			logger.info("SESSIONE PRECEDENTE: "+prevS);
						
			
			
			ImportRealTime.execute(prevS,actS,connToBackOffice,connToDatasharing,connToConc,connToNucleus);
			String enable_reimport=resource.getString("enable_reimport");	
			
			if(actS.isTimeForReimport() && enable_reimport.equals("true") && !prevS.equals(actS)){

				Date datestartIniToday = DateUtils.zeroUno(actS.getSTART_DATE());
				Date datestartFinToday = DateUtils.ventitreCinquantanove(actS.getSTART_DATE());
				Date dateEndToday = DateUtils.ventitreCinquantanove(actS.getEND_DATE());
				
				Date datestartIniYesterday = DateUtils.calcPreviousDay(datestartIniToday);
				Date datestartFinYesterday = DateUtils.calcPreviousDay(datestartFinToday);
				Date dateEndToYesterday = DateUtils.calcPreviousDay(dateEndToday);
				
				ImportPregress.execute(datestartIniYesterday, datestartFinYesterday, dateEndToYesterday, connToBackOffice, connToDatasharing, connToConc, connToNucleus);
			}else{
				logger.info("PREGRESSO GIA RE-IMPORTATO");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Errore generico nella gestione dell'import"+e.toString()+" - "+e.getStackTrace());
			connToDatasharing.rollback();
			throw new DataLayerException("Errore generico nella gestione dell'import",e);
		} 	
		finally {
			try {
				DBConnectionManager.CloseConnection(connToDatasharing);
				DBConnectionManager.CloseConnection(connToNucleus);	
				DBConnectionManager.CloseConnection(connToConc);	
				DBConnectionManager.CloseConnection(connToBackOffice);
				s.release();
			} catch (DataLayerException e) {

				e.printStackTrace();
				logger.error("DB Connection Close Error : "+e);
			}

		}		
	}

}
