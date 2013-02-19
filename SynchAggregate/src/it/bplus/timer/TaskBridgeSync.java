package it.bplus.timer;

import it.bplus.business.ImporterDelegate;
import it.bplus.exception.BusinessLayerException;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class TaskBridgeSync extends TimerTask  {

	protected static Logger logger = Logger.getLogger(TaskBridgeSync.class);
	public TaskBridgeSync(){}

	public void run() {
		Date d = new Date();
		logger.info("Start Task Import to AGGREGATE: "+d.getTime());

		try {
			ImporterDelegate.doImport();
		} catch (BusinessLayerException e) {
			logger.error("Business Layer Exception : "+e );
		} catch (Exception e) {
			logger.error("General Exception : "+e );
		}

		logger.info("End Task Import to AGGREGATE: "+d.getTime());
	}



}