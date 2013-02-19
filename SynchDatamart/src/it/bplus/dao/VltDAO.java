package it.bplus.dao;

import it.bplus.primitive.Sessione;
import it.bplus.primitive.Vlt;
import it.bplus.primitive.VltExtra;
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

	public static ArrayList<Vlt> getAllVltDismissed(
			Connection connectionImport, Sessione s) throws SQLException {

		logger.info("Start - Get Vlt Dismissed");
		ArrayList<Vlt> listDismissedVlt = new ArrayList<Vlt>();

		String sqlImport = "select s.AAMS_VLT_CODE from aggregate.birsvlt s where s.DATE_OUT >= date(?)";

		PreparedStatement ps = connectionImport.prepareStatement(sqlImport);
		ps.setDate(1, new java.sql.Date(s.getSTART_DATE().getTime()));
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Vlt Vlt = new Vlt();
			Vlt.setAAMS_VLT_ID(rs.getString("AAMS_VLT_CODE"));

			listDismissedVlt.add(Vlt);
		}

		logger.info("End - Get Vlt Dismissed");

		rs.close();
		ps.close();
		return listDismissedVlt;

	}

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
			Vlt Vlt = new Vlt();
			Vlt.setAAMS_VLT_ID(resultSetImportVlt.getString("code_id")
					.toUpperCase());
			Vlt.setGS_VLT_CODE(resultSetImportVlt.getString("vlt_id")
					.toUpperCase());
			Vlt.setCIV(resultSetImportVlt.getString("civ"));
			Vlt.setMAC_ADDRESS(resultSetImportVlt.getString("macaddress"));
			Vlt.setLOCATION_ID(resultSetImportVlt.getString("location_id")
					.toUpperCase());
			Vlt.setID_VLT_MODEL(resultSetImportVlt.getInt("vlt_model_id"));
			Vlt.setGS_ID(resultSetImportVlt.getLong("gs_id"));
			Vlt.setCONNECTION_TYPE(resultSetImportVlt.getInt("server_link_id"));
			Vlt.setCREATION_DATE(resultSetImportVlt.getTimestamp("creation_ts"));
			Vlt.setCESSATION_DATE(resultSetImportVlt
					.getTimestamp("deletion_ts"));
			Vlt.setVARIATION_DATE(resultSetImportVlt
					.getTimestamp("variation_ts"));

			listVltEtna.add(Vlt);
		}

		logger.info("End - Get Vlt from Etna");

		resultSetImportVlt.close();
		statementImport.close();
		return listVltEtna;

	}

	public static HashMap<String, Vlt> getAllVltAggregate(
			Connection connectionImport) throws SQLException {
		HashMap<String, Vlt> hashVltAggregate = new HashMap<String, Vlt>();

		logger.info("Start - Get Vlt from Aggregate");

		String sqlImportStagingVlt = "select AAMS_VLT_CODE,GS_VLT_CODE,CIV,MAC_ADDRESS,AAMS_VLT_MODEL_CODE,ID_CONNECTION_TYPE,DATE_IN,DATE_OUT,DATE_VARIATION,"
				+ "AAMS_GAME_SYSTEM_CODE,AAMS_LOCATION_CODE from BIRSVLT";

		Statement statementImportStagingVlt = connectionImport
				.createStatement();
		ResultSet resultSetImportStagingVlt = statementImportStagingVlt
				.executeQuery(sqlImportStagingVlt);

		while (resultSetImportStagingVlt.next()) {
			Vlt Vlt = new Vlt();
			Vlt.setAAMS_VLT_ID(resultSetImportStagingVlt.getString(
					"AAMS_VLT_CODE").toUpperCase());
			Vlt.setGS_VLT_CODE(resultSetImportStagingVlt
					.getString("GS_VLT_CODE"));
			Vlt.setCIV(resultSetImportStagingVlt.getString("CIV"));
			Vlt.setMAC_ADDRESS(resultSetImportStagingVlt
					.getString("MAC_ADDRESS"));
			Vlt.setID_VLT_MODEL(resultSetImportStagingVlt
					.getInt("AAMS_VLT_MODEL_CODE"));
			Vlt.setCONNECTION_TYPE(resultSetImportStagingVlt
					.getInt("ID_CONNECTION_TYPE"));
			Vlt.setCREATION_DATE(resultSetImportStagingVlt
					.getTimestamp("DATE_IN"));
			Vlt.setCESSATION_DATE(resultSetImportStagingVlt
					.getTimestamp("DATE_OUT"));
			Vlt.setVARIATION_DATE(resultSetImportStagingVlt
					.getTimestamp("DATE_VARIATION"));
			Vlt.setGS_ID(resultSetImportStagingVlt
					.getLong("AAMS_GAME_SYSTEM_CODE"));
			Vlt.setLOCATION_ID(resultSetImportStagingVlt.getString(
					"AAMS_LOCATION_CODE").toUpperCase());

			hashVltAggregate.put(Vlt.getAAMS_VLT_ID(), Vlt);
		}

		logger.info("End - Get Vlt from Aggregate");

		statementImportStagingVlt.close();
		resultSetImportStagingVlt.close();

		return hashVltAggregate;
	}

	public static HashMap<String, VltExtra> getAllVltExtraAggregate(
			Connection connectionImport) throws SQLException {
		HashMap<String, VltExtra> hashVltAggregate = new HashMap<String, VltExtra>();

		logger.info("Start - Get Vlt from Aggregate");

		String sqlImportStagingVlt = "select AAMS_VLT_CODE,GS_VLT_CODE,pct_supplier from BIRSVLT vlt "
				+ "join BIRSVLTMODEL vltm on vlt.AAMS_VLT_MODEL_CODE = vltm.AAMS_VLT_MODEL_CODE";

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

			Double pct_supplier = resultSetImportStagingVlt
					.getDouble("pct_supplier");

			VltExtra VltE = new VltExtra(vlt, pct_supplier);

			hashVltAggregate.put(vlt.getAAMS_VLT_ID(), VltE);
		}

		logger.info("End - Get Vlt from Aggregate");

		statementImportStagingVlt.close();
		resultSetImportStagingVlt.close();

		return hashVltAggregate;
	}

	public static void insertVlt(Connection connEtna, Connection connExport)
			throws SQLException {

		ArrayList<Vlt> listVltEtna = getAllVltEtna(connEtna);
		HashMap<String, Vlt> hashVltAggregate = getAllVltAggregate(connExport);

		connExport.setAutoCommit(true);

		logger.info("Start - Insert Vlt to Aggregate");

		for (Vlt actVLt : listVltEtna) {

			if (!hashVltAggregate.containsKey(actVLt.getAAMS_VLT_ID()
					.toUpperCase())
					&& actVLt.getGS_ID() != 0
					&& actVLt.getGS_ID() != -1) {

				String sqlInsertvLT = "INSERT INTO BIRSVLT (AAMS_VLT_CODE,GS_VLT_CODE,CIV,MAC_ADDRESS,AAMS_VLT_MODEL_CODE,ID_CONNECTION_TYPE,"
						+ "DATE_IN,DATE_OUT,DATE_VARIATION,AAMS_GAME_SYSTEM_CODE,AAMS_LOCATION_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = connExport
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
				Vlt vltChanged = hashVltAggregate.get(actVLt.getAAMS_VLT_ID());

				if (!DateUtils.isDateEquals(vltChanged.getVARIATION_DATE(),
						actVLt.getVARIATION_DATE())
						&& actVLt.getGS_ID() != 0
						&& actVLt.getGS_ID() != -1) {
					String sqlInsertVlt = "UPDATE BIRSVLT SET GS_VLT_CODE=?,CIV=?,MAC_ADDRESS=?,AAMS_VLT_MODEL=?,ID_CONNECTION_TYPE=?,"
							+ "DATE_IN=?,DATE_OUT=?,DATE_VARIATION=?,AAMS_GAME_SYSTEM_CODE=?,AAMS_LOCATION_CODE=? WHERE AAMS_VLT_CODE=?";
					PreparedStatement ps = connExport
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

		connExport.setAutoCommit(false);

		logger.info("End - Insert Vlt to Aggregate");

	}
}
