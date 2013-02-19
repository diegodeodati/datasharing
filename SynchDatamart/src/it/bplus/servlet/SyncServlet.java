package it.bplus.servlet;

import it.bplus.timer.TaskBridgeSync;
import it.bplus.util.Constants;

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
public class SyncServlet extends HttpServlet implements Servlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(SyncServlet.class);
	private ResourceBundle resource = ResourceBundle.getBundle(Constants.SYNC_KEY);


	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
	}

	public void init(ServletConfig config) throws ServletException {

		logger.info("Inizio Sync Datamart Servlet");//							      
		long interval = Long.parseLong(config.getInitParameter("interval"))* 60 * 1000; 
		
		super.init(config);
		
        URL confURL = Thread.currentThread().getContextClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(confURL);

		try {
			String enable_automatic_sync=resource.getString("enable_automatic_sync_to_datamart");			
			if(enable_automatic_sync!=null && enable_automatic_sync.equals("true")){
				TaskBridgeSync action = new TaskBridgeSync();
				Timer timer = new Timer();
				timer.schedule(action, new Date(), interval);
				getServletContext().setAttribute("sync_to_datamart", timer);
				
			}

		}catch(Exception e){
		}

		logger.info("Fine Sync Datamart Servlet");
	}

}
