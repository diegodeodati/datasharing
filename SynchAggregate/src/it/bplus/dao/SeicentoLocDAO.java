package it.bplus.dao;

import it.bplus.primitive.SeicentoLoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SeicentoLocDAO {

	protected static Logger logger = Logger.getLogger(SeicentoLocDAO.class);
	
	public static ArrayList<SeicentoLoc> getAllSeicentoLocEtna(Connection connectionImport,int maxId) throws SQLException{
		ArrayList<SeicentoLoc> listSeicentoLocEtna = new ArrayList<SeicentoLoc>();
		
		logger.info("Start - Get Seicento Loc from Etna");
		
		String sqlImportEtnaSeicento = "select id,location_id,gs_id,bet,win,tot_paid,tot_in,tot_out,bet_num,tot_bet,tot_win,tot_tot_paid,tot_tot_in,tot_tot_out," +
				"tot_bet_num,year,month,day from sc_location_meters where id>"+maxId+" and day is not null and month is not null limit 5000";
	
		
		Statement statementImportEtnaSeicento = connectionImport.createStatement();		
		ResultSet resultSetImportEtnaSeicento = statementImportEtnaSeicento.executeQuery(sqlImportEtnaSeicento);
		
		while (resultSetImportEtnaSeicento.next()){
			SeicentoLoc sei = new SeicentoLoc();
			sei.setId(resultSetImportEtnaSeicento.getInt("id"));
			sei.setLocation_id(resultSetImportEtnaSeicento.getString("location_id"));
			sei.setGs_id(resultSetImportEtnaSeicento.getLong("gs_id"));
			sei.setBet(resultSetImportEtnaSeicento.getLong("bet"));
			sei.setWin(resultSetImportEtnaSeicento.getLong("win"));
			sei.setTot_paid(resultSetImportEtnaSeicento.getLong("tot_paid"));
			sei.setTot_in(resultSetImportEtnaSeicento.getLong("tot_in"));
			sei.setTot_out(resultSetImportEtnaSeicento.getLong("tot_out"));
			sei.setBet_num(resultSetImportEtnaSeicento.getLong("bet_num"));
			sei.setTot_bet(resultSetImportEtnaSeicento.getLong("tot_bet"));
			sei.setTot_win(resultSetImportEtnaSeicento.getLong("tot_win"));
			sei.setTot_tot_paid(resultSetImportEtnaSeicento.getLong("tot_tot_paid"));
			sei.setTot_tot_in(resultSetImportEtnaSeicento.getLong("tot_tot_in"));
			sei.setTot_tot_out(resultSetImportEtnaSeicento.getLong("tot_tot_out"));
			sei.setTot_bet_num(resultSetImportEtnaSeicento.getLong("tot_bet_num"));
			sei.setYear(resultSetImportEtnaSeicento.getInt("year"));
			sei.setMonth(resultSetImportEtnaSeicento.getInt("month"));
			sei.setDay(resultSetImportEtnaSeicento.getInt("day"));
			
			
			listSeicentoLocEtna.add(sei);
			
		}
				
		logger.info("End - Get Seicento Loc from Etna");
		
		statementImportEtnaSeicento.close();
		resultSetImportEtnaSeicento.close();
		return listSeicentoLocEtna;
	}

	public static int getMaxSeicentoStaging(Connection conncectionExport)throws SQLException{
	logger.info("Start - Get Max Id Seicento Loc from Staging");
		
		String sqlMaxSeicentoStaging = "select max(id_meter) from birsaamsmeter";
	
		
		Statement statementMaxSeicentoStaging = conncectionExport.createStatement();		
		ResultSet resultSetMaxSeicentoStaging= statementMaxSeicentoStaging.executeQuery(sqlMaxSeicentoStaging);
		
		
		logger.info("End - Get Max Id Seicento from Staging");
		
		if(resultSetMaxSeicentoStaging.next()){
			int aux = resultSetMaxSeicentoStaging.getInt(1);
			statementMaxSeicentoStaging.close();			
			resultSetMaxSeicentoStaging.close();
			return aux;
		}
		else{
			statementMaxSeicentoStaging.close();
			resultSetMaxSeicentoStaging.close();
		    return 0;
		    
		}    
	}

    public static void insertSeicento(Connection connEtna,Connection connStaging) throws SQLException{
    	
    	int maxId = getMaxSeicentoStaging(connStaging);
    	ArrayList<SeicentoLoc> listSeicentoLocEtna = getAllSeicentoLocEtna(connEtna, maxId);
    	
    	connStaging.setAutoCommit(false);
    	
    	
    	
    	String sqlInsertSeicento = "INSERT INTO aggregate.birsaamsmeter " +
    								"(ID_METER, AAMS_LOCATION_CODE, BET, WIN, TOT_PAID, TOT_IN, TOT_OUT, BET_NUM, " +
    								"TOT_BET, TOT_WIN, TOT_TOT_PAID, TOT_TOT_IN, TOT_TOT_OUT, TOT_BET_NUM, ANNO, MESE, GIORNO, AAMS_GAMES_SYSTEM_CODE, DATA) " +
    								"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
    								"?, ?, ?, ?, ?)";

 	    PreparedStatement ps = connStaging.prepareStatement(sqlInsertSeicento);
 	    
 	   logger.info("Start - Insert Seicento Loc to Staging");
    	
    	for(SeicentoLoc sei:listSeicentoLocEtna){    		
    		ps.setLong(1,sei.getId());
    		ps.setString(2, sei.getLocation_id());
    		ps.setLong(3,sei.getBet());
    		ps.setLong(4,sei.getWin());
    		ps.setLong(5,sei.getTot_paid());
    		ps.setLong(6,sei.getTot_in());
    		ps.setLong(7,sei.getTot_out());
    		ps.setLong(8,sei.getBet_num());
    		ps.setLong(9,sei.getTot_bet());
    		ps.setLong(10,sei.getTot_win());
    		ps.setLong(11,sei.getTot_tot_paid());
    		ps.setLong(12,sei.getTot_tot_in());
    		ps.setLong(13,sei.getTot_tot_out());
    		ps.setLong(14,sei.getTot_bet_num());
    		ps.setInt(15,sei.getYear());
    		ps.setInt(16,sei.getMonth());
    		ps.setInt(17,sei.getDay());
    		ps.setLong(18,sei.getGs_id());
    		ps.setDate(19,sei.getDate());
    		
    		ps.executeUpdate();
    	}
    	
    	ps.close();
    	connStaging.commit();
    	
    	logger.info("End - Insert Seicento Loc to Staging");
    }
}








