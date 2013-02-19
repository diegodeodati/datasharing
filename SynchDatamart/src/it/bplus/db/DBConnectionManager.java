package it.bplus.db;




import it.bplus.exception.DataLayerException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;



public class DBConnectionManager {

	protected static Logger logger = Logger.getLogger(DBConnectionManager.class);

	
	public static Connection DatamartConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/mysql_datamart");
			conn = ds.getConnection();
			
			System.out.println("CONNESSO DATAMART");

		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi sul datamart", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione sul datamart", e);
		}
		return conn;	

	}
	


	public static Connection AggregateConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/mysql_aggregate");
			conn = ds.getConnection();
			
			System.out.println("CONNESSO AGGREGATO");

		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi sull aggregato", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione sull aggregato", e);
		}
		return conn;	

	}

	
	public static void CloseConnection(ResultSet rs, Statement stmt, Connection conn) throws DataLayerException{
		try{
			conn.close(); // Return to connection pool
			conn = null;  // Make sure we don't close it twice
		} catch (SQLException e) {
			logger.error(e);
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) { ; }
				rs = null;
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { ; }
				stmt = null;
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}

		}
	}

	public static void CloseConnection(Connection conn) throws DataLayerException{
		try{
			if(conn!=null)
			conn.close(); // Return to connection pool
			conn = null;  // Make sure we don't close it twice
		} catch (SQLException e) {
			logger.error(e);
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		} catch (Exception e) {
			logger.error(e);
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		}  
		finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}

		}
	}
	
	
	public static void CloseConnection(Statement stmt, Connection conn) throws DataLayerException{
		try{
			conn.close(); // Return to connection pool
			conn = null;  // Make sure we don't close it twice
		} catch (SQLException e) {
			logger.error(e);
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) { ; }
				stmt = null;
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) { ; }
				conn = null;
			}

		}
	}
}
