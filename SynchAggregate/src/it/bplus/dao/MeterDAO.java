package it.bplus.dao;

import it.bplus.primitive.MeterInfoInspired;
import it.bplus.primitive.MeterInfoNovomatic;
import it.bplus.primitive.MeterInspired;
import it.bplus.primitive.MeterNovomatic;
import it.bplus.primitive.MeterSogei;
import it.bplus.primitive.Sessione;
import it.bplus.primitive.Vlt;
import it.bplus.util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

public class MeterDAO {

protected static Logger logger = Logger.getLogger(MeterDAO.class);
	
	public static ArrayList<MeterInspired> getMeterInspired(Connection connectionImport,long maxId) throws SQLException{
		ArrayList<MeterInspired> listMeterInspired = new ArrayList<MeterInspired>();
		
		logger.info("Start - Get Meter Inspired");
		
		String sqlImportMeterInspired = "select mg.BET,mg.WIN,IFNULL(mv.TOTAL_IN,0) as TOTAL_IN ,IFNULL(mv.TOTAL_OUT,0) as TOTAL_OUT,mg.GAMES_PLAYED,mg.GAMES_WON, " +
				 "IFNULL(mg.JACKPOT_WIN,0) as JP_WIN, IFNULL(mg.JACKPOT_CONTRIBUTION,0) as JP_CONTRIBUTION, " +
				 "IFNULL(csh.HANDPAY_VALUE,0) as HANDPAY,IFNULL(mv.TICKET_IN,0) as TICKET_IN,IFNULL(mv.TICKET_OUT,0) as TICKET_OUT, " +
				 "IFNULL(mv.BILL_IN,0) as BILL_IN, IFNULL(mv.COIN_IN,0) COIN_IN , " +
				 "IFNULL(mv.CARD_IN,0) as CARD_IN ,IFNULL(mv.TOTAL_PREPAID_IN,0) as TOTAL_PREPAID_IN, " +
				 "DATE_FORMAT(sl.START_DATE , '%Y-%m-%d %H:%i:%s.%f') as DATA,year(sl.START_DATE) as YEAR,month(sl.START_DATE) as MONTH, " +
				 "day(sl.START_DATE) as DAY , hour(sl.START_DATE) as HOUR, MINUTE(sl.START_DATE) as MINUTE, " +
				 "mg.AAMS_CODE_ID,mg.AAMS_GAME_ID,mg.AAMS_LOCATION_ID,mg.SESSION_ID " +
				 "from meters_games mg " +
				 "left join meters_vlt mv on mg.AAMS_CODE_ID = mv.AAMS_CODE_ID and mv.SESSION_ID=mg.SESSION_ID " +
				 "inner join session_log sl on mg.SESSION_ID = sl.SESSION_ID " +
				 "left join cashdesk_location_handpay csh on csh.MACHINE_GS_ID = mg.MACHINE_GS_ID  " +
				 "and csh.SESSION_ID = mg.SESSION_ID " +
				 "WHERE  sl.SESSION_ID="+maxId;
	
		
		Statement statementImportMeterInspired = connectionImport.createStatement();		
		ResultSet resultSetImportMeterInspired = statementImportMeterInspired.executeQuery(sqlImportMeterInspired);
		
		while (resultSetImportMeterInspired.next()){
			
			
			MeterSogei m= new MeterSogei();
			m.setBet(resultSetImportMeterInspired.getDouble("BET"));
			m.setWin(resultSetImportMeterInspired.getDouble("WIN"));
			m.setTotal_in(resultSetImportMeterInspired.getDouble("TOTAL_IN"));
			m.setTotal_out(resultSetImportMeterInspired.getDouble("TOTAL_OUT"));
			m.setGames_played(resultSetImportMeterInspired.getInt("GAMES_PLAYED"));
			m.setGames_win(resultSetImportMeterInspired.getInt("GAMES_WON"));
			m.setJackpot_win(resultSetImportMeterInspired.getDouble("JP_WIN"));
			m.setJackpot_contribution(resultSetImportMeterInspired.getDouble("JP_CONTRIBUTION"));
			m.setHandpay(resultSetImportMeterInspired.getDouble("HANDPAY"));
			m.setTotal_ticket_in(resultSetImportMeterInspired.getDouble("TICKET_IN"));
			m.setTotal_ticket_out(resultSetImportMeterInspired.getDouble("TICKET_OUT"));
			m.setTotal_bill_in(resultSetImportMeterInspired.getDouble("BILL_IN"));
			m.setTotal_coin_in(resultSetImportMeterInspired.getDouble("COIN_IN"));
			m.setTotal_card_in(resultSetImportMeterInspired.getDouble("CARD_IN"));
			
			
			MeterInfoInspired mi = new MeterInfoInspired();
			mi.setAamsLocationCode(resultSetImportMeterInspired.getString("AAMS_LOCATION_ID"));
			mi.setAamsVltCode(resultSetImportMeterInspired.getString("AAMS_CODE_ID"));
			mi.setAamsGameCode(resultSetImportMeterInspired.getLong("AAMS_GAME_ID"));
			mi.setDataRif(resultSetImportMeterInspired.getTimestamp("DATA"));
			mi.setSessionId(resultSetImportMeterInspired.getLong("SESSION_ID"));
			
			MeterInspired msi= new MeterInspired(m,mi);
			
			listMeterInspired.add(msi);
		}
				
		logger.info("End - Get Meter Inspired");
		
		statementImportMeterInspired.close();
		resultSetImportMeterInspired.close();
		return listMeterInspired;
	}
	
	
	public static void insertMeterVltToFill(Connection connExport,Sessione s) throws SQLException{
		
		
		
		if(DateUtils.isPregress(s.getSTART_DATE(),s.getEND_DATE()) || (s.getAAMS_GAME_SYSTEM_CODE()!=1711000045 && DateUtils.day(s.getEND_DATE())<DateUtils.day(new Date()) )){
		
		logger.info("Start - Meter Vlt To Fill");
			
    	ArrayList<Vlt> listVlt = VltDAO.getAllVltToFill(connExport, s);

    	connExport.setAutoCommit(false);
    	
    	String sqlInsertMeterInspired = "INSERT IGNORE INTO BIRSGSMETERS (DATA,ANNO,MESE,GIORNO,ORA,MINUTO, "+
    			"AAMS_GAME_CODE,AAMS_VLT_CODE,AAMS_LOCATION_CODE,AAMS_GAME_SYSTEM_CODE,UNIQUE_SESSION_ID)"+
    	" VALUES (?,?,?,?,?,?,?,?,?,?,?)";
 	    PreparedStatement ps = connExport.prepareStatement(sqlInsertMeterInspired);
 	    
 	   logger.info("Start - Insert Meter Fill to Aggregate");
    	
    	for(Vlt v:listVlt){
    		
    	    		
    		ps.setTimestamp(1,new java.sql.Timestamp(s.getSTART_DATE().getTime()));
    		ps.setInt(2, DateUtils.year(s.getSTART_DATE()));
    		ps.setInt(3, DateUtils.month(s.getSTART_DATE()));
    		ps.setInt(4, DateUtils.day(s.getSTART_DATE()));
    		ps.setInt(5, DateUtils.hour(s.getSTART_DATE()));
    		ps.setInt(6, DateUtils.minute(s.getSTART_DATE()));
    		
    		ps.setLong(7,-1);
    		ps.setString(8,v.getAAMS_VLT_ID());
    		ps.setString(9,v.getLOCATION_ID());
    		ps.setLong(10,s.getAAMS_GAME_SYSTEM_CODE());
    		ps.setLong(11,s.getUNIQUE_SESSION_ID());
    		
    		ps.executeUpdate();
    		
    		
    		    		
    	}
    	
    	logger.info("End - Meter Vlt To Fill");    	
    	ps.close();
    	//connExport.commit();
		
	}
		
	}
	
	public static ArrayList<MeterNovomatic> getMeterNovomatic(Connection connectionImport,long maxId) throws SQLException{
		ArrayList<MeterNovomatic> listMeterNovomatic = new ArrayList<MeterNovomatic>();
		
		logger.info("Start - Get Meter Novomatic");
		
		String sqlImportMeterNovomatic = "select mg.BET,mg.WIN,IFNULL(mv.TOTAL_IN,0) as TOTAL_IN ,IFNULL(mv.TOTAL_OUT,0) as TOTAL_OUT,mg.GAMES_PLAYED,mg.GAMES_WON, " +
										 "IFNULL(mg.JACKPOT_WIN,0) as JP_WIN, IFNULL(mg.JACKPOT_CONTRIBUTION,0) as JP_CONTRIBUTION, " +
										 "IFNULL(csh.HANDPAY_VALUE,0) as HANDPAY,IFNULL(mv.TICKET_IN,0) as TICKET_IN,IFNULL(mv.TICKET_OUT,0) as TICKET_OUT, " +
										 "IFNULL(mv.BILL_IN,0) as BILL_IN, IFNULL(mv.COIN_IN,0) COIN_IN , " +
										 "IFNULL(mv.CARD_IN,0) as CARD_IN ,IFNULL(mv.TOTAL_PREPAID_IN,0) as TOTAL_PREPAID_IN, " +
										 "DATE_FORMAT(sl.START_DATE , '%Y-%m-%d %H:%i:%s.%f') as DATA,year(sl.START_DATE) as YEAR,month(sl.START_DATE) as MONTH, " +
										 "day(sl.START_DATE) as DAY , hour(sl.START_DATE) as HOUR, MINUTE(sl.START_DATE) as MINUTE, " +
										 "mg.AAMS_CODE_ID,mg.AAMS_GAME_ID,mg.AAMS_LOCATION_ID,mg.SESSION_ID " +
										 "from meters_games mg " +
										 "left join meters_vlt mv on mg.AAMS_CODE_ID = mv.AAMS_CODE_ID and mv.SESSION_ID=mg.SESSION_ID " +
										 "inner join session_log sl on mg.SESSION_ID = sl.SESSION_ID " +
										 "left join cashdesk_location_handpay csh on csh.MACHINE_GS_ID = mg.MACHINE_GS_ID  " +
										 "and csh.SESSION_ID = mg.SESSION_ID " +
										 "WHERE  sl.SESSION_ID="+maxId;
					
				
				
						/*select mg.`BET`,mg.`WIN`,mv.`TOTAL_IN`,mv.`TOTAL_OUT`,mg.`GAMES_PLAYED`,mg.`GAMES_WON`," +
				"IFNULL(mg.`JACKPOT_WIN`,0) as JP_WIN, IFNULL(mg.`JACKPOT_CONTRIBUTION`,0) as JP_CONTRIBUTION," +
				"IFNULL(csh.`HANDPAY_VALUE`,0) as HANDPAY,mv.`TICKET_IN`,mv.`TICKET_OUT`,mv.`BILL_IN`," +
				"mv.`COIN_IN`,mv.`CARD_IN`,mv.`TOTAL_PREPAID_IN`," +
				"DATE_FORMAT(sl.`START_DATE` , '%Y-%m-%d %H:%i:%s.%f') as DATA,year(sl.`START_DATE`) as YEAR,month(sl.`START_DATE`) as MONTH ,day(sl.`START_DATE`) as DAY , hour(sl.`START_DATE`) as HOUR, MINUTE(sl.`START_DATE`) as MINUTE," +
				"mv.`AAMS_CODE_ID`,mg.`AAMS_GAME_ID`,mv.`AAMS_LOCATION_ID`,mg.`SESSION_ID`  " +
				"from meters_games mg " +
				"inner join meters_vlt mv on mg.`AAMS_CODE_ID` = mv.`AAMS_CODE_ID` and mv.`SESSION_ID`=mg.`SESSION_ID`" +
				"inner join session_log sl on mg.`SESSION_ID` = sl.`SESSION_ID`" +
				"left join `cashdesk_location_handpay` csh on csh.`MACHINE_GS_ID` = mg.`MACHINE_GS_ID` " +
				"and csh.`SESSION_ID` = mg.`SESSION_ID`" +
				"WHERE sl.`SESSION_SUCCESS`=1 and sl.`SESSION_ID`="+maxId+" "+
				"group by mv.`AAMS_LOCATION_ID`,mv.`AAMS_CODE_ID`,mg.`AAMS_GAME_ID`, DATA";*/
	
		
		Statement statementImportMeterNovomatic = connectionImport.createStatement();		
		ResultSet resultSetImportMeterNovomatic = statementImportMeterNovomatic.executeQuery(sqlImportMeterNovomatic);
		
		while (resultSetImportMeterNovomatic.next()){
			
			MeterSogei m= new MeterSogei();
			m.setBet(resultSetImportMeterNovomatic.getDouble("BET"));
			m.setWin(resultSetImportMeterNovomatic.getDouble("WIN"));
			m.setTotal_in(resultSetImportMeterNovomatic.getDouble("TOTAL_IN"));
			m.setTotal_out(resultSetImportMeterNovomatic.getDouble("TOTAL_OUT"));
			m.setGames_played(resultSetImportMeterNovomatic.getInt("GAMES_PLAYED"));
			m.setGames_win(resultSetImportMeterNovomatic.getInt("GAMES_WON"));
			m.setJackpot_win(resultSetImportMeterNovomatic.getDouble("JP_WIN"));
			m.setJackpot_contribution(resultSetImportMeterNovomatic.getDouble("JP_CONTRIBUTION"));
			m.setHandpay(resultSetImportMeterNovomatic.getDouble("HANDPAY"));
			m.setTotal_ticket_in(resultSetImportMeterNovomatic.getDouble("TICKET_IN"));
			m.setTotal_ticket_out(resultSetImportMeterNovomatic.getDouble("TICKET_OUT"));
			m.setTotal_bill_in(resultSetImportMeterNovomatic.getDouble("BILL_IN"));
			m.setTotal_coin_in(resultSetImportMeterNovomatic.getDouble("COIN_IN"));
			m.setTotal_card_in(resultSetImportMeterNovomatic.getDouble("CARD_IN"));
			
			
			MeterInfoNovomatic mi = new MeterInfoNovomatic();
			mi.setAamsLocationCode(resultSetImportMeterNovomatic.getString("AAMS_LOCATION_ID"));
			mi.setAamsVltCode(resultSetImportMeterNovomatic.getString("AAMS_CODE_ID")); 
			mi.setAamsGameCode(resultSetImportMeterNovomatic.getLong("AAMS_GAME_ID"));
			mi.setDataRif(resultSetImportMeterNovomatic.getTimestamp("DATA"));			
			mi.setSessionId(resultSetImportMeterNovomatic.getLong("SESSION_ID"));
			
			MeterNovomatic msn= new MeterNovomatic(m,mi);
			
			listMeterNovomatic.add(msn);
		}
				
		logger.info("End - Get Meter Novomatic");
		
		statementImportMeterNovomatic.close();
		resultSetImportMeterNovomatic.close();
		return listMeterNovomatic;
	}

	public static int getMaxMeterAggregate(Connection conncectionExport,long aams_game_system_code)throws SQLException{
	logger.info("Start - Get Max Meter from Aggregate "+aams_game_system_code);
		
		String sqlMaxMeterAggregate = "select IFNULL(max(b.`UNIQUE_SESSION_ID`),0) UNIQUE_SESSION_ID " +
				"from `birsgsmeters` b " +
				"INNER JOIN `birsimportlog` l " +
				"on b.`UNIQUE_SESSION_ID` = l.`UNIQUE_SESSION_ID` " +
				"and b.`AAMS_GAME_SYSTEM_CODE` = l.`AAMS_GAME_SYSTEM_CODE`" +
				"WHERE l.`EXIT_CODE`=1 and l.`AAMS_GAME_SYSTEM_CODE` = "+aams_game_system_code;
	
		
		Statement statementMaxMeterAggregate = conncectionExport.createStatement();		
		ResultSet resultSetMaxMeterAggregate= statementMaxMeterAggregate.executeQuery(sqlMaxMeterAggregate);
		
		
		logger.info("End - Get Max Meter from Aggregate "+aams_game_system_code);
		
		if(resultSetMaxMeterAggregate.next()){
			int aux = resultSetMaxMeterAggregate.getInt(1);
			statementMaxMeterAggregate.close();			
			resultSetMaxMeterAggregate.close();
			return aux;
		}
		else{
			statementMaxMeterAggregate.close();
			resultSetMaxMeterAggregate.close();
		    return 0;
		    
		}    
	}

    public static void insertMeterInspired(Connection connImport,Connection connExport,long actualSessionInspiredId) throws SQLException{
    	
    	//int maxId = getMaxMeterAggregate(connExport,1711000065);
    	ArrayList<MeterInspired> listMeterInspired = getMeterInspired(connImport, actualSessionInspiredId);

    	connExport.setAutoCommit(false);
    	
    	String sqlInsertMeterInspired = "INSERT IGNORE INTO BIRSGSMETERS (DATA,ANNO,MESE,GIORNO,ORA,MINUTO,BET,WIN,JACKPOT_WIN,JACKPOT_CONTRIBUTION,TOT_IN,TOT_OUT,GAMES_PLAYED,GAMES_WON," +
    			"TOTAL_HANDPAY,TOTAL_TICKET_IN,TOTAL_TICKET_OUT,TOTAL_BILL_IN,TOTAL_COIN_IN,TOTAL_PREPAID_IN,TOTAL_CARD_IN," +
    			"AAMS_GAME_CODE,AAMS_VLT_CODE,AAMS_LOCATION_CODE,AAMS_GAME_SYSTEM_CODE,UNIQUE_SESSION_ID)"+
    	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 	    PreparedStatement ps = connExport.prepareStatement(sqlInsertMeterInspired);
 	    
 	   logger.info("Start - Insert Meter Inspired to Aggregate");
    	
    	for(MeterInspired mi:listMeterInspired){
    		
    	    		
    		ps.setTimestamp(1,new java.sql.Timestamp(mi.getE().getDataRif().getTime()));
    		ps.setInt(2, DateUtils.year(mi.getE().getDataRif()));
    		ps.setInt(3, DateUtils.month(mi.getE().getDataRif()));
    		ps.setInt(4, DateUtils.day(mi.getE().getDataRif()));
    		ps.setInt(5, DateUtils.hour(mi.getE().getDataRif()));
    		ps.setInt(6, DateUtils.minute(mi.getE().getDataRif()));
    		
    		
    		
    		ps.setDouble(7,mi.getM().getBet());
    		ps.setDouble(8,mi.getM().getWin());
    		ps.setDouble(9,mi.getM().getJackpot_win());
    		ps.setDouble(10,mi.getM().getJackpot_contribution());
    		ps.setDouble(11,mi.getM().getTotal_in());
    		ps.setDouble(12,mi.getM().getTotal_out());
    		ps.setDouble(13,mi.getM().getGames_played());
    		ps.setDouble(14,mi.getM().getGames_win());
    		ps.setDouble(15,mi.getM().getHandpay());
    		ps.setDouble(16,mi.getM().getTotal_ticket_in());
    		ps.setDouble(17,mi.getM().getTotal_ticket_out());
    		ps.setDouble(18,mi.getM().getTotal_bill_in());
    		ps.setDouble(19,mi.getM().getTotal_coin_in());
    		ps.setDouble(20,mi.getM().getTotal_prepaid_in());
    		ps.setDouble(21,mi.getM().getTotal_card_in());
    		
    		
    		
    		ps.setLong(22,mi.getE().getAamsGameCode());
    		ps.setString(23,mi.getE().getAamsVltCode());
    		ps.setString(24,mi.getE().getAamsLocationCode());
    		ps.setLong(25,mi.getE().getGameSystemCode());
    		ps.setLong(26,mi.getE().getSessionId());
    		
    		ps.executeUpdate();
    		    		
    	}
    	
    	ps.close();
    	connExport.commit();
    	
    	logger.info("End - Insert Meter Inspired to Aggregate");
    }
	
    public static void insertMeterNovomatic(Connection connImport,Connection connExport,long actualSessionNovomaticId) throws SQLException{
    	
    	//int maxId = getMaxMeterAggregate(connExport,1711000065);
    	ArrayList<MeterNovomatic> listMeterNovomatic= getMeterNovomatic(connImport, actualSessionNovomaticId);

    	connExport.setAutoCommit(false);
    	
    	String sqlInsertMeterInspired = "INSERT INTO BIRSGSMETERS (DATA,ANNO,MESE,GIORNO,ORA,MINUTO,BET,WIN,JACKPOT_WIN,JACKPOT_CONTRIBUTION,TOT_IN,TOT_OUT,GAMES_PLAYED,GAMES_WON," +
    			"TOTAL_HANDPAY,TOTAL_TICKET_IN,TOTAL_TICKET_OUT,TOTAL_BILL_IN,TOTAL_COIN_IN,TOTAL_PREPAID_IN,TOTAL_CARD_IN," +
    			"AAMS_GAME_CODE,AAMS_VLT_CODE,AAMS_LOCATION_CODE,AAMS_GAME_SYSTEM_CODE,UNIQUE_SESSION_ID)"+
    	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 	    PreparedStatement ps = connExport.prepareStatement(sqlInsertMeterInspired);
 	    
 	   logger.info("Start - Insert Meter Novomatic to Aggregate");
    	
    	for(MeterNovomatic mi:listMeterNovomatic){
    		
    	    		
    		ps.setTimestamp(1,new java.sql.Timestamp(mi.getE().getDataRif().getTime()));
    		ps.setInt(2, DateUtils.year(mi.getE().getDataRif()));
    		ps.setInt(3, DateUtils.month(mi.getE().getDataRif()));
    		ps.setInt(4, DateUtils.day(mi.getE().getDataRif()));
    		ps.setInt(5, DateUtils.hour(mi.getE().getDataRif()));
    		ps.setInt(6, DateUtils.minute(mi.getE().getDataRif()));
    		
    		
    		
    		ps.setDouble(7,mi.getM().getBet());
    		ps.setDouble(8,mi.getM().getWin());
    		ps.setDouble(9,mi.getM().getJackpot_win());
    		ps.setDouble(10,mi.getM().getJackpot_contribution());
    		ps.setDouble(11,mi.getM().getTotal_in());
    		ps.setDouble(12,mi.getM().getTotal_out());
    		ps.setDouble(13,mi.getM().getGames_played());
    		ps.setDouble(14,mi.getM().getGames_win());
    		ps.setDouble(15,mi.getM().getHandpay());
    		ps.setDouble(16,mi.getM().getTotal_ticket_in());
    		ps.setDouble(17,mi.getM().getTotal_ticket_out());
    		ps.setDouble(18,mi.getM().getTotal_bill_in());
    		ps.setDouble(19,mi.getM().getTotal_coin_in());
    		ps.setDouble(20,mi.getM().getTotal_prepaid_in());
    		ps.setDouble(21,mi.getM().getTotal_card_in());
    		
    		
    		
    		ps.setLong(22,mi.getE().getAamsGameCode());
    		ps.setString(23,mi.getE().getAamsVltCode());
    		ps.setString(24,mi.getE().getAamsLocationCode());
    		ps.setLong(25,mi.getE().getGameSystemCode());
    		ps.setLong(26,mi.getE().getSessionId());
    		
    		ps.executeUpdate();
    		    		
    	}
    	
    	ps.close();
    	connExport.commit();
    	
    	logger.info("End - Insert Meter Novomatic to Aggregate");
    }
}
