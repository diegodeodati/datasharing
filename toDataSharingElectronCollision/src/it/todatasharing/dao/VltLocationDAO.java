package it.todatasharing.dao;

import it.todatasharing.primitive.VltLocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class VltLocationDAO {
	
	protected static Logger logger = Logger.getLogger(VltLocationDAO.class);
	
	public static HashMap<String,VltLocation> getVltLocationMap(Connection connBackOffice, Date dEnd) throws SQLException {

		logger.info("Start - Get Vlt Location Map from BackOffice");

		String sql = "select v.vlt_id vlt_id,xx.code_id code_id,h.location_id location_id from sc_vlt_location_history h "+
					 "inner join sc_vlt v on h.code_id = v.code_id "+
					 "inner join "+
					 "(select max(ts) maximo, code_id , location_id "+
					 "from sc_vlt_location_history s where date(ts) <? OR "+
				     "(date(ts) <=? and (select COUNT(*) from sc_vlt_location_history where code_id=s.code_id )%2= 1) "+
				     "group by code_id )"+ 
			 	     "xx on h.code_id = xx.code_id and  h.ts = xx.maximo where "+
				     "(v.deletion_ts is null or v.deletion_ts > ?) "+
				     "and (date(h.ts) <? or (date(ts) <=? and (select COUNT(*) from sc_vlt_location_history "+
				     "where code_id=xx.code_id)%2 = 1))";
		
		
								
				/*"select v.vlt_id vlt_id,xx.code_id code_id,h.location_id location_id from sc_vlt_location_history h " +
				     "inner join sc_vlt v on h.code_id = v.code_id " +
				     "inner join ( select max(ts) maximo, code_id , location_id " +
				     "from sc_vlt_location_history where ts <=? group by code_id ) " +
				     "xx on h.code_id = xx.code_id and  h.ts = xx.maximo where " +
				     "(v.deletion_ts is null or v.deletion_ts >= ?) " +
				     "and h.ts <=?";*/

		PreparedStatement ps = connBackOffice.prepareStatement(sql);
		ps.setDate(1, new java.sql.Date(dEnd.getTime()));
		ps.setDate(2, new java.sql.Date(dEnd.getTime()));
		ps.setDate(3, new java.sql.Date(dEnd.getTime()));
		ps.setDate(4, new java.sql.Date(dEnd.getTime()));
		ps.setDate(5, new java.sql.Date(dEnd.getTime()));

		ResultSet rs = ps.executeQuery();
		HashMap<String,VltLocation> map = new HashMap<String, VltLocation>();
		
		while (rs.next()) {
			VltLocation vltLoc = new VltLocation();
			
			vltLoc.setVlt_id(rs.getString("vlt_id"));
			vltLoc.setCode_id(rs.getString("code_id"));
			vltLoc.setLocation_id(rs.getString("location_id"));
			
			
			map.put(rs.getString("vlt_id"),vltLoc);
		}

		logger.info("End - Get Vlt Location Map from BackOffice");

		ps.close();
		rs.close();
		
		return map;
	}

}
