package it.bplus.dao;

import it.bplus.primitive.Sessione;
import it.bplus.primitive.Vlt;
import it.bplus.util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class VltDAO {

	protected static Logger logger = Logger.getLogger(VltDAO.class);

	public static ArrayList<Vlt> getAllVltEtna(Connection connectionImport)
			throws SQLException {

		logger.info("Start - Get Vlt from Etna");
		Statement statementImport = connectionImport.createStatement();
		ArrayList<Vlt> listVltEtna = new ArrayList<Vlt>();

		String sqlImportEtnaVlt = "select vlt_id,code_id,macaddress,civ,location_id,vlt_model_id,gs_id,server_link_id,"
				+ "creation_ts,deletion_ts,variation_ts from sc_vlt";
		ResultSet resultSetImportVlt = statementImport
				.executeQuery(sqlImportEtnaVlt);

		while (resultSetImportVlt.next()) {
			Vlt vlt = new Vlt();
			vlt.setAAMS_VLT_ID(resultSetImportVlt.getString("code_id")
					.toUpperCase());
			vlt.setGS_VLT_CODE(resultSetImportVlt.getString("vlt_id")
					.toUpperCase());
			vlt.setCIV(resultSetImportVlt.getString("civ"));
			vlt.setMAC_ADDRESS(resultSetImportVlt.getString("macaddress"));
			vlt.setLOCATION_ID(resultSetImportVlt.getString("location_id")
					.toUpperCase());
			vlt.setID_VLT_MODEL(resultSetImportVlt.getInt("vlt_model_id"));
			vlt.setGS_ID(resultSetImportVlt.getLong("gs_id"));
			vlt.setCONNECTION_TYPE(resultSetImportVlt.getInt("server_link_id"));
			vlt.setCREATION_DATE(resultSetImportVlt.getTimestamp("creation_ts"));
			vlt.setCESSATION_DATE(resultSetImportVlt
					.getTimestamp("deletion_ts"));
			vlt.setVARIATION_DATE(resultSetImportVlt
					.getTimestamp("variation_ts"));

			listVltEtna.add(vlt);
		}

		logger.info("End - Get Vlt from Etna");

		resultSetImportVlt.close();
		statementImport.close();
		return listVltEtna;

	}

	public static HashMap<String, Vlt> getAllVltAggregate(
			Connection connectionImport) throws SQLException {
		HashMap<String, Vlt> hashVltStaging = new HashMap<String, Vlt>();

		logger.info("Start - Get Vlt from Aggregate");

		String sqlImportStagingVlt = "select AAMS_VLT_CODE,GS_VLT_CODE,CIV,MAC_ADDRESS,AAMS_VLT_MODEL_CODE,ID_CONNECTION_TYPE,DATE_IN,DATE_OUT,DATE_VARIATION,"
				+ "AAMS_GAME_SYSTEM_CODE,AAMS_LOCATION_CODE from BIRSVLT";

		Statement statementImportStagingVlt = connectionImport
				.createStatement();
		ResultSet resultSetImportStagingVlt = statementImportStagingVlt
				.executeQuery(sqlImportStagingVlt);

		while (resultSetImportStagingVlt.next()) {
			Vlt vlt = new Vlt();
			vlt.setAAMS_VLT_ID(resultSetImportStagingVlt.getString(
					"AAMS_VLT_CODE").toUpperCase());
			vlt.setGS_VLT_CODE(resultSetImportStagingVlt
					.getString("GS_VLT_CODE"));
			vlt.setCIV(resultSetImportStagingVlt.getString("CIV"));
			vlt.setMAC_ADDRESS(resultSetImportStagingVlt
					.getString("MAC_ADDRESS"));
			vlt.setID_VLT_MODEL(resultSetImportStagingVlt
					.getInt("AAMS_VLT_MODEL_CODE"));
			vlt.setCONNECTION_TYPE(resultSetImportStagingVlt
					.getInt("ID_CONNECTION_TYPE"));
			vlt.setCREATION_DATE(resultSetImportStagingVlt
					.getTimestamp("DATE_IN"));
			vlt.setCESSATION_DATE(resultSetImportStagingVlt
					.getTimestamp("DATE_OUT"));
			vlt.setVARIATION_DATE(resultSetImportStagingVlt
					.getTimestamp("DATE_VARIATION"));
			vlt.setGS_ID(resultSetImportStagingVlt
					.getLong("AAMS_GAME_SYSTEM_CODE"));
			vlt.setLOCATION_ID(resultSetImportStagingVlt.getString(
					"AAMS_LOCATION_CODE").toUpperCase());

			hashVltStaging.put(vlt.getAAMS_VLT_ID(), vlt);
		}

		logger.info("End - Get Vlt from Aggregate");

		statementImportStagingVlt.close();
		resultSetImportStagingVlt.close();

		return hashVltStaging;
	}

	public static ArrayList<String> getActiveVlt(Connection connExport,
			Sessione s) throws SQLException {

		logger.info("Start - Get Active Vlt");
		ArrayList<String> listActiveVlt = new ArrayList<String>();

		String sql = "select AAMS_VLT_CODE,AAMS_LOCATION_CODE,AAMS_GAME_SYSTEM_CODE "
				+ " from birsgsmeters b where date(b.DATA) = date(?) "
				+ "and b.AAMS_GAME_SYSTEM_CODE = ? group by b.AAMS_VLT_CODE";

		PreparedStatement ps = connExport.prepareStatement(sql);
		ps.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));
		ps.setLong(2, s.getAAMS_GAME_SYSTEM_CODE());

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			listActiveVlt.add(rs.getString("AAMS_VLT_CODE").toUpperCase());
		}

		logger.info("End - Get Active Vlt");

		rs.close();
		ps.close();
		return listActiveVlt;

	}

	public static ArrayList<Vlt> getAllVltToFill(Connection connectionExport,
			Sessione s) throws SQLException {
		logger.info("Start - Get Vlt To Fill from Etna");
		ArrayList<Vlt> listVltEtna = new ArrayList<Vlt>();

		String sql = "select xx.AAMS_VLT_CODE code_id,h.aams_location_code location_id ,v.aams_game_system_code aams_game_system_code "
				+ "from vlthistory h inner join birsvlt v on h.AAMS_VLT_CODE = v.AAMS_VLT_CODE inner join "
				+ "(select max(DATE_CHANGE) maximo, AAMS_VLT_CODE , aams_location_code from vlthistory s where date(DATE_CHANGE) < ? or "
				+ "(date(DATE_CHANGE) <= ? and (select COUNT(*) from vlthistory where AAMS_VLT_CODE=s.AAMS_VLT_CODE )%2= 1) group by AAMS_VLT_CODE ) "
				+ "xx on h.AAMS_VLT_CODE = xx.AAMS_VLT_CODE and  h.DATE_CHANGE = xx.maximo where "
				+ "(v.DATE_OUT is null or v.DATE_OUT > ?) "
				+ "and (date(h.DATE_CHANGE) < ? or (date(DATE_CHANGE) <= ? and (select COUNT(*) from vlthistory where AAMS_VLT_CODE=xx.AAMS_VLT_CODE)%2 = 1)) "
				+ "and v.aams_game_system_code = ?";

		PreparedStatement ps = connectionExport.prepareStatement(sql);
		ps.setDate(1, new java.sql.Date(s.getEND_DATE().getTime()));
		ps.setDate(2, new java.sql.Date(s.getEND_DATE().getTime()));
		ps.setDate(3, new java.sql.Date(s.getEND_DATE().getTime()));
		ps.setDate(4, new java.sql.Date(s.getEND_DATE().getTime()));
		ps.setDate(5, new java.sql.Date(s.getEND_DATE().getTime()));
		ps.setLong(6, s.getAAMS_GAME_SYSTEM_CODE());

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Vlt vlt = new Vlt();
			vlt.setAAMS_VLT_ID(rs.getString("code_id").toUpperCase());
			vlt.setLOCATION_ID(rs.getString("location_id").toUpperCase());
			vlt.setGS_ID(rs.getLong("aams_game_system_code"));

			listVltEtna.add(vlt);
		}

		logger.info("End - Get Vlt To Fill from Etna");

		rs.close();
		ps.close();
		return listVltEtna;

	}

	public static void insertVlt(Connection connEtna, Connection connStaging)
			throws SQLException {

		ArrayList<Vlt> listVltEtna = getAllVltEtna(connEtna);
		HashMap<String, Vlt> hashVltStaging = getAllVltAggregate(connStaging);

		connStaging.setAutoCommit(true);

		logger.info("Start - Insert Vlt to Aggregate");

		for (Vlt actVLt : listVltEtna) {

			if (!hashVltStaging.containsKey(actVLt.getAAMS_VLT_ID()
					.toUpperCase())
					&& actVLt.getGS_ID() != 0
					&& actVLt.getGS_ID() != -1) {

				String sqlInsertvLT = "INSERT INTO BIRSVLT (AAMS_VLT_CODE,GS_VLT_CODE,CIV,MAC_ADDRESS,AAMS_VLT_MODEL_CODE,ID_CONNECTION_TYPE,"
						+ "DATE_IN,DATE_OUT,DATE_VARIATION,AAMS_GAME_SYSTEM_CODE,AAMS_LOCATION_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = connStaging
						.prepareStatement(sqlInsertvLT);

				ps.setString(1, actVLt.getAAMS_VLT_ID().toUpperCase());
				ps.setString(2, actVLt.getGS_VLT_CODE());
				ps.setString(3, actVLt.getCIV());
				ps.setString(4, actVLt.getMAC_ADDRESS());
				ps.setInt(5, actVLt.getID_VLT_MODEL());

				ps.setInt(6, actVLt.getCONNECTION_TYPE());

				ps.setTimestamp(7, actVLt.getCREATION_DATE());
				ps.setTimestamp(8, actVLt.getCESSATION_DATE());
				ps.setTimestamp(9, actVLt.getVARIATION_DATE());

				ps.setLong(10, actVLt.getGS_ID());
				ps.setString(11, actVLt.getLOCATION_ID());

				ps.executeUpdate();

				ps.close();
			} else {
				Vlt vltChanged = hashVltStaging.get(actVLt.getAAMS_VLT_ID());

				if (!vltChanged.equals(actVLt)) {
					if (!DateUtils.isDateEquals(vltChanged.getVARIATION_DATE(),
							actVLt.getVARIATION_DATE())
							&& actVLt.getGS_ID() != 0
							&& actVLt.getGS_ID() != -1) {
						String sqlInsertVlt = "UPDATE BIRSVLT SET GS_VLT_CODE=?,CIV=?,MAC_ADDRESS=?,AAMS_VLT_MODEL_CODE=?,ID_CONNECTION_TYPE=?,"
								+ "DATE_IN=?,DATE_OUT=?,DATE_VARIATION=?,AAMS_GAME_SYSTEM_CODE=?,AAMS_LOCATION_CODE=? WHERE AAMS_VLT_CODE=?";
						PreparedStatement ps = connStaging
								.prepareStatement(sqlInsertVlt);

						ps.setString(1, actVLt.getGS_VLT_CODE().toUpperCase());
						ps.setString(2, actVLt.getCIV());
						ps.setString(3, actVLt.getMAC_ADDRESS());
						ps.setInt(4, actVLt.getID_VLT_MODEL());

						ps.setInt(5, actVLt.getCONNECTION_TYPE());

						ps.setTimestamp(6, actVLt.getCREATION_DATE());
						ps.setTimestamp(7, actVLt.getCESSATION_DATE());
						ps.setTimestamp(8, actVLt.getVARIATION_DATE());

						ps.setLong(9, actVLt.getGS_ID());
						ps.setString(10, actVLt.getLOCATION_ID().toUpperCase());

						ps.setString(11, actVLt.getAAMS_VLT_ID().toUpperCase());

						ps.executeUpdate();

						ps.close();
					}

				}
			}
		}

		connStaging.setAutoCommit(false);

		logger.info("End - Insert Vlt to Aggregate");

	}

}
