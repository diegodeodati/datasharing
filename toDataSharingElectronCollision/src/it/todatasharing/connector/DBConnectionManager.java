package it.todatasharing.connector;



import it.todatasharing.exception.DataLayerException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;



public class DBConnectionManager {

	protected static Logger logger = Logger.getLogger(DBConnectionManager.class);

	
	
		
	
	public static Connection dataSharingConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/mysql_datasharing");
			conn = ds.getConnection();
			if(conn.isClosed())
				throw new DataLayerException("La connessione è chiusa");			


		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi su mysql", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione su mysql", e);
		}
		return conn;	

	}	
	
	

	
	
	public static Connection backofficeConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/mysql_back_office");
			conn = ds.getConnection();
			if(conn.isClosed())
				throw new DataLayerException("La connessione è chiusa");			


		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi su mysql", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione su mysql", e);
		}
		return conn;	

	}	
	

	public static Connection cqiConnectionFactory() throws DataLayerException{
		Connection conn=null;

		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/sqlserver_cqi");
			conn = ds.getConnection();

		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi sul cqi", e);
		} catch (SQLException e) {			
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione sul cqi", e);
		} 
		return conn;	

	}

	public static Connection nucleusConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/sqlserver_novomatic_nuclues");
			conn = ds.getConnection();

		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi sul nucleus", e);
		} catch (SQLException e) {			
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione sul nucleus", e);
		} 
		return conn;	

	}
	
	

	public static Connection concessionaryConnectionFactory() throws DataLayerException{
		Connection conn=null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds;

			ds = (DataSource)envContext.lookup("jdbc/sqlserver_novomatic_concessionarydb");
			conn = ds.getConnection();

		} catch (NamingException e) {
			e.printStackTrace();
			throw new DataLayerException("Errore nella gestione delle risorse jndi sul cqi", e);
		} catch (SQLException e) {			
			e.printStackTrace();
			throw new DataLayerException("Errore nella apertura della connessione sul cqi", e);
		} 
		return conn;	

	}



	public static void CloseConnection(Connection conn) throws DataLayerException{
		try{
			if(conn!=null)
			conn.close(); // Return to connection pool
			conn = null;  // Make sure we don't close it twice
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new DataLayerException("Errore nella chiusura della connessione", e);
		} catch (Exception e) {
			logger.error(e.toString());
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
	
}
