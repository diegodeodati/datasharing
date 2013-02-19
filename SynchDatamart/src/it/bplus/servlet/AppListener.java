package it.bplus.servlet;

import it.bplus.db.DBConnectionManager;
import it.bplus.exception.DataLayerException;

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
				"sync_to_datamart");

		Connection connToAggregate = null;
		Connection connToDatamart = null;

		if (timer != null) {

			try {
				
				connToAggregate = DBConnectionManager
						.AggregateConnectionFactory();
				
				connToDatamart = DBConnectionManager.DatamartConnectionFactory();

				connToDatamart.setAutoCommit(false);
				connToDatamart.rollback();

				DBConnectionManager.CloseConnection(connToDatamart);
				DBConnectionManager.CloseConnection(connToAggregate);

				logger.debug("DBConnections Closed...");
			} catch (DataLayerException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			logger.info("Stopping Sync Datamart");
			timer.cancel();
			logger.info("Sync Datamart is stopped...");
		} else {
			logger.info("Timer was not found in ServletContext...");
		}

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

	}

}
