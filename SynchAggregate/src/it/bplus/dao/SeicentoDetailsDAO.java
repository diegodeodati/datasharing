package it.bplus.dao;

import it.bplus.primitive.SeicentoDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class SeicentoDetailsDAO {


	protected static Logger logger = Logger.getLogger(SeicentoDetailsDAO.class);
	
	public static ArrayList<SeicentoDetails> getAllSeicentoDetEtna(Connection connectionImport,int maxId) throws SQLException{
		ArrayList<SeicentoDetails> listSeicentoDetEtna = new ArrayList<SeicentoDetails>();
		
		logger.info("Start - Get Seicento Details from Etna");
		
		String sqlImportEtnaSeicentoDet = "select id,game_id,code_id,gs_id,bet,win,bet_num,tot_bet,tot_win,"
			+"tot_bet_num,year,month,day,msg_id,date from sc_vlt_game_meters where id>"+maxId+" and date is not null limit 20000";
	
		
		Statement statementImportEtnaSeicentoDet = connectionImport.createStatement();		
		ResultSet resultSetImportEtnaSeicentoDet = statementImportEtnaSeicentoDet.executeQuery(sqlImportEtnaSeicentoDet);
		
		while (resultSetImportEtnaSeicentoDet.next()){
			SeicentoDetails seiDet = new SeicentoDetails();
			seiDet.setId(resultSetImportEtnaSeicentoDet.getInt("id"));
			seiDet.setGame_id(resultSetImportEtnaSeicentoDet.getLong("game_id"));
			seiDet.setCode_id(resultSetImportEtnaSeicentoDet.getString("code_id"));
			seiDet.setGs_id(resultSetImportEtnaSeicentoDet.getLong("gs_id"));
			seiDet.setBet(resultSetImportEtnaSeicentoDet.getLong("bet"));
			seiDet.setWin(resultSetImportEtnaSeicentoDet.getLong("win"));
			seiDet.setBet_num(resultSetImportEtnaSeicentoDet.getLong("bet_num"));
			seiDet.setTot_bet(resultSetImportEtnaSeicentoDet.getLong("tot_bet"));
			seiDet.setTot_win(resultSetImportEtnaSeicentoDet.getLong("tot_win"));
			seiDet.setTot_bet_num(resultSetImportEtnaSeicentoDet.getLong("tot_bet_num"));
			seiDet.setYear(resultSetImportEtnaSeicentoDet.getInt("year"));
			seiDet.setMonth(resultSetImportEtnaSeicentoDet.getInt("month"));
			seiDet.setDay(resultSetImportEtnaSeicentoDet.getInt("day"));
			seiDet.setMsg_id(resultSetImportEtnaSeicentoDet.getLong("msg_id"));
			
			seiDet.setDate(resultSetImportEtnaSeicentoDet.getDate("date"));
			
			
			listSeicentoDetEtna.add(seiDet);
			
		}
				
		logger.info("End - Get Seicento Details from Etna");
		
		statementImportEtnaSeicentoDet.close();
		resultSetImportEtnaSeicentoDet.close();
		return listSeicentoDetEtna;
	}

	public static int getMaxSeicentoDetStaging(Connection conncectionExport)throws SQLException{
	logger.info("Start - Get Max Id Seicento Details from Staging");
		
		String sqlMaxSeicentoStaging = "select max(id_meter_details) from birsaamsmetersdetails";
	
		
		Statement statementMaxSeicentoStaging = conncectionExport.createStatement();		
		ResultSet resultSetMaxSeicentoStaging= statementMaxSeicentoStaging.executeQuery(sqlMaxSeicentoStaging);
		
		
		logger.info("End - Get Max Id Seicento Details from Staging");
		
		if(resultSetMaxSeicentoStaging.next()){
			int aux = resultSetMaxSeicentoStaging.getInt(1);
			resultSetMaxSeicentoStaging.close();
			statementMaxSeicentoStaging.close();
			return aux;
		}
		else{
			resultSetMaxSeicentoStaging.close();
			statementMaxSeicentoStaging.close();
		    return 0;
		}
	}

    public static void insertSeicentoDetails(Connection connEtna,Connection connStaging) throws SQLException{
    	
    	int maxId = getMaxSeicentoDetStaging(connStaging);
    	
    	logger.info("maxid details "+maxId);
    	ArrayList<SeicentoDetails> listSeicentoDetEtna = getAllSeicentoDetEtna(connEtna, maxId);
    	
    	connStaging.setAutoCommit(true);
    	
    	String sqlInsertSeicento = "INSERT INTO BIRSAAMSMETERSDETAILS (ID_METER_DETAILS,AAMS_GAMES_CODE,AAMS_VLT_CODE,BET,WIN,BET_NUM,TOT_BET,TOT_WIN,"+
    	"TOT_BET_NUM,ANNO,MESE,GIORNO,DATA,AAMS_GAME_SYSTEM_CODE,MSG_ID)"+
    	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 	    PreparedStatement ps = connStaging.prepareStatement(sqlInsertSeicento);
 	    
 	   logger.info("Start - Insert Seicento Details to Staging");
    	
    	for(SeicentoDetails seiDet:listSeicentoDetEtna){
    		ps.setLong(1,seiDet.getId());
    		ps.setLong(2,seiDet.getGame_id());
    		ps.setString(3, seiDet.getCode_id());
    		ps.setLong(4,seiDet.getBet());
    		ps.setLong(5,seiDet.getWin());
    		ps.setLong(6,seiDet.getBet_num());
    		ps.setLong(7,seiDet.getTot_bet());
    		ps.setLong(8,seiDet.getTot_win());
    		ps.setLong(9,seiDet.getTot_bet_num());
    		ps.setInt(10,seiDet.getYear());
    		ps.setInt(11,seiDet.getMonth());
    		ps.setInt(12,seiDet.getDay());
    		ps.setDate(13,seiDet.getDate());
    		ps.setLong(14,seiDet.getGs_id());
    		ps.setLong(15,seiDet.getMsg_id());
    		
    		ps.executeUpdate();
    	}
    	
    	
    	ps.close();
    	connStaging.setAutoCommit(false);
    	
    	logger.info("End - Insert Seicento Details to Staging");
    }
	
}
