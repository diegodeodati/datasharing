package it.bplus.dao;

import it.bplus.primitive.Seicento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SeicentoDAO {

	protected static Logger logger = Logger.getLogger(SeicentoDAO.class);
	
	public static ArrayList<Seicento> getAllSeicentoEtna(Connection connectionImport,int maxId) throws SQLException{
		ArrayList<Seicento> listSeicentoEtna = new ArrayList<Seicento>();
		
		logger.info("Start - Get Seicento from Etna");
		
		String sqlImportEtnaSeicento = "select id,code_id,gs_id,bet,win,tot_paid,tot_in,tot_out,bet_num,tot_bet,tot_win,tot_tot_paid,tot_tot_in,tot_tot_out,"
			+"tot_bet_num,year,month,day,msg_id,date from sc_vlt_meters where id>"+maxId+" and date is not null";
	
		
		Statement statementImportEtnaSeicento = connectionImport.createStatement();		
		ResultSet resultSetImportEtnaSeicento = statementImportEtnaSeicento.executeQuery(sqlImportEtnaSeicento);
		
		while (resultSetImportEtnaSeicento.next()){
			Seicento sei = new Seicento();
			sei.setId(resultSetImportEtnaSeicento.getInt("id"));
			sei.setCode_id(resultSetImportEtnaSeicento.getString("code_id"));
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
			sei.setMsg_id(resultSetImportEtnaSeicento.getLong("msg_id"));
			
			
			sei.setDate(resultSetImportEtnaSeicento.getDate("date"));
			
			listSeicentoEtna.add(sei);
			
		}
				
		logger.info("End - Get Seicento from Etna");
		
		statementImportEtnaSeicento.close();
		resultSetImportEtnaSeicento.close();
		return listSeicentoEtna;
	}

	public static int getMaxSeicentoStaging(Connection conncectionExport)throws SQLException{
	logger.info("Start - Get Max Id Seicento from Staging");
		
		String sqlMaxSeicentoStaging = "select max(id_meter) from birsaamsmeters";
	
		
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
    	ArrayList<Seicento> listSeicentoEtna = getAllSeicentoEtna(connEtna, maxId);
    	
    	connStaging.setAutoCommit(false);
    	
    	String sqlInsertSeicento = "INSERT INTO BIRSAAMSMETERS (ID_METER,AAMS_VLT_CODE,BET,WIN,TOT_PAID,TOT_IN,TOT_OUT,BET_NUM,TOT_BET,TOT_WIN,"+
    	"TOT_TOT_PAID,TOT_TOT_IN,TOT_TOT_OUT,TOT_BET_NUM,ANNO,MESE,GIORNO,DATA,AAMS_GAMES_SYSTEM_CODE,MSG_ID)"+
    	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 	    PreparedStatement ps = connStaging.prepareStatement(sqlInsertSeicento);
 	    
 	   logger.info("Start - Insert Seicento to Staging");
    	
    	for(Seicento sei:listSeicentoEtna){
    		ps.setLong(1,sei.getId());
    		ps.setString(2, sei.getCode_id());
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
    		ps.setDate(18,sei.getDate());
    		ps.setLong(19,sei.getGs_id());
    		ps.setLong(20,sei.getMsg_id());
    		
    		ps.executeUpdate();
    	}
    	
    	ps.close();
    	connStaging.commit();
    	
    	logger.info("End - Insert Seicento to Staging");
    }
}








