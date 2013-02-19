package it.todatasharing.servlet;


import it.todatasharing.timer.Task;
import it.todatasharing.util.Constants;
import it.todatasharing.util.DateUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * gestisce in maniera automatica l'import in base all'intervallo specificato nel file di properties.
 * @author luca tiberia
 *
 */
public class SchedulerServlet extends HttpServlet implements Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(SchedulerServlet.class);
	private ResourceBundle resource = ResourceBundle.getBundle(Constants.TODATASHARING_KEY);


	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
	}

	public void init(ServletConfig config) throws ServletException {

		logger.info("Inizio SchedulerServlet");//							      
		long interval = Long.parseLong(config.getInitParameter("interval"))* 60 * 1000; 
		
		super.init(config);
        URL confURL = Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(confURL);

		try {
			String enable_automatic_importer=resource.getString("enable_automatic_importer");			
			if(enable_automatic_importer!=null && enable_automatic_importer.equals("true")){
				Task action = new Task();
				Timer timer = new Timer();
				
				Date dateAct  = new Date();
				
				Date dateStartInizio = DateUtils.creaData(DateUtils.year(dateAct),DateUtils.month(dateAct),DateUtils.day(dateAct),8,0,0,0);
				
				
				timer.schedule(action, dateStartInizio, interval); // era dateStartInizio
				getServletContext().setAttribute("sync_to_datasharing", timer);
				
			}

		}catch(Exception e){
		}

		logger.info("Fine SchedulerServlet");
	}

}

