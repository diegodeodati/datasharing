package it.bplus.servlet;

import it.bplus.db.DBConnectionManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class Nagios extends BaseServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(Nagios.class);
	

	Connection connAggregate;
	Connection connDatamart;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Nagios() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(ServletConfig sc) throws ServletException{
		super.init(sc);
	}




	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("doGet");
		try {
			
			connAggregate=DBConnectionManager.AggregateConnectionFactory();
			connDatamart=DBConnectionManager.DatamartConnectionFactory();

			PrintWriter out = response.getWriter();

			if (connAggregate!=null){
				out.println("AGGREGATE_UP");
			} else {
				out.println("AGGREGATE_DOWN");
			}
			
			if (connDatamart!=null){
				out.println("DATAMART_UP");
			} else {
				out.println("DATAMART_DOWN");
			}			

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw new ServletException(e.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {

	}

}
