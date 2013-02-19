package it.todatasharing.timer;


import it.todatasharing.business.ImporterDelegate;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;


public class Task extends TimerTask {
//	protected Log logger = 		LogFactory.getLog(this.getClass());
	protected static Logger logger = Logger.getLogger(Task.class);
	
	public Task(){

	}


	public void run() {
		Date d = new Date();		  
	   
		logger.info("Start Task Import toDataSharing: "+d.getTime());

		try {
			ImporterDelegate.doImport();
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error("General Exception : "+e );
		}

		logger.info("End Task Import toDataSharing: "+d.getTime());
	}

}