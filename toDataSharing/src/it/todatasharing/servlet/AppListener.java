package it.todatasharing.servlet;



import it.todatasharing.connector.DBConnectionManager;
import it.todatasharing.exception.DataLayerException;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class AppListener implements ServletContextListener,Serializable {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(AppListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Timer timer = (Timer) sce.getServletContext().getAttribute(
				"sync_to_datasharing");

		Connection connToDatasharing=null;
		Connection connToNucleus=null;
		Connection connToConc=null;
		Connection connToBackOffice = null;

		if (timer != null) {

			try {
				
				connToNucleus=DBConnectionManager.nucleusConnectionFactory();
				connToConc = DBConnectionManager.concessionaryConnectionFactory();
				connToBackOffice = DBConnectionManager.backofficeConnectionFactory();
				connToDatasharing=DBConnectionManager.dataSharingConnectionFactory();
				
				connToDatasharing.setAutoCommit(false);
				connToDatasharing.rollback();

				
				logger.debug("DBConnections Closed...");
			} catch (DataLayerException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally{
				try {
					DBConnectionManager.CloseConnection(connToNucleus);
					DBConnectionManager.CloseConnection(connToConc);
					DBConnectionManager.CloseConnection(connToBackOffice);
					DBConnectionManager.CloseConnection(connToDatasharing);
				} catch (DataLayerException e) {
					e.printStackTrace();
				}

			}
			
			logger.info("Stopping Sync Datasharing");
			timer.cancel();
			logger.info("Sync Datasharing is stopped...");
		} else {
			logger.info("Timer was not found in ServletContext...");
		}

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}

}
