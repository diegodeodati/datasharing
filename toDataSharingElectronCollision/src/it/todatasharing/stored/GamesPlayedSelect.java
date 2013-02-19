package it.todatasharing.stored;



import it.todatasharing.exception.DataLayerException;
import it.todatasharing.primitive.MeterSogeiReport;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author SevenWinzoz
 *
 */
public class GamesPlayedSelect {

	static CallableStatement proc_clubid = null;
	static ResultSet rs = null;
	static List<MeterSogeiReport> meterNovomaticList;
	protected static Logger logger = Logger.getLogger(GamesPlayedSelect.class);

	/** Richiama la stored procedure Report.gamesPlayedSelect_SOGEI_2
	 * 
	 * @param conn Connessione verso la 10 dalla parte nucleus
	 * @param date Data per la quale si vuole effettuare il la query
	 * @param clubId ClubId per il quale si vuole effettuare la chiamata
	 * @return ResultSet contenente tutti i meter relativi alla sala "clubId" desiderata
	 * @throws DataLayerException
	 */
	public static List<MeterSogeiReport> execute(Connection conn,java.util.Date dateSTART,java.util.Date dateEND,String clubId) throws DataLayerException{
		try {
			logger.debug("Start Report.gamesPlayedSelect_SOGEI_3");
			meterNovomaticList = new ArrayList<MeterSogeiReport>();
			proc_clubid = conn.prepareCall("{ call Report.gamesPlayedSelect_SOGEI_3(?,?,?,?) }");
			proc_clubid.setDate(1,new java.sql.Date(dateSTART.getTime()));
			proc_clubid.setDate(2,new java.sql.Date(dateEND.getTime()));
			proc_clubid.setString(3, "");
			proc_clubid.setString(4, clubId);

			rs = proc_clubid.executeQuery();
			
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			
			while(rs.next()){
				MeterSogeiReport m = new MeterSogeiReport();
				m.setClub_id(rs.getInt("ClubId"));	
				m.setMachine_cn(rs.getString("MachineCN")); 					
				m.setGame_id(rs.getInt("GameID"));
				m.setTotal_in(Double.valueOf(twoDForm.format(rs.getDouble("TotalIn")*100)).longValue());
				
				m.setTotal_out(Double.valueOf(twoDForm.format((rs.getDouble("TotalOut")+rs.getDouble("Payouts"))*100)).longValue());
				
				
				
				
				m.setTotal_ticket_in(Double.valueOf(twoDForm.format(rs.getDouble("TotalTicketIn")*100)).longValue());
				
				m.setTotal_ticket_out(Double.valueOf(twoDForm.format(rs.getDouble("TotalTicketOut")*100)).longValue());
				
				m.setTotal_coin_in(Double.valueOf(twoDForm.format(rs.getDouble("TotalCoinIn")*100)).longValue());
				m.setTotal_bill_in(Double.valueOf(twoDForm.format(rs.getDouble("TotalBillIn")*100)).longValue());
				m.setTotal_card_in(Double.valueOf(twoDForm.format(rs.getDouble("TotalCardIn")*100)).longValue());
				m.setTotal_bet(Double.valueOf(twoDForm.format(rs.getDouble("TotalBet")*100)).longValue());
				m.setTotal_win(Double.valueOf(twoDForm.format(rs.getDouble("TotalWon")*100)).longValue());
				m.setGames_played(Double.valueOf(twoDForm.format(rs.getInt("GamesPlayed"))).longValue());				
				m.setTotal_handpay(m.getTotal_out()-m.getTotal_ticket_out());
				
				if((m.getTotal_bet()>0 || m.getTotal_in()>0 || m.getTotal_out()>0) && !m.getMachine_cn().equals("-1"))
				meterNovomaticList.add(m);
			}
			
			proc_clubid.close();
			rs.close();
			logger.debug("End Report.gamesPlayedSelect_SOGEI_3");
			
			return meterNovomaticList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataLayerException("call Report.gamesPlayedSelect_SOGEI_3", e);
		}
	}

}
