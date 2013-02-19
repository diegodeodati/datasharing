package it.bplus.dao;

import it.bplus.primitive.Gestore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class GestoreDAO {

	protected static Logger logger = Logger.getLogger(GestoreDAO.class);

	public static ArrayList<Gestore> getAllGestoreEtna(
			Connection connectionImport) throws SQLException {

		logger.info("Start - Get Gestore from Etna");
		Statement statementImport = connectionImport.createStatement();
		ArrayList<Gestore> listGestoreEtna = new ArrayList<Gestore>();

		String sqlImportEtnaGes = "select id,name,fax,tel,email,fiscal_code,vat_id,toponimo_id,address,address_number,postcode,comune_name"
				+ " from sc_manager where toponimo_id is not null";
		ResultSet resultSetImportGes = statementImport
				.executeQuery(sqlImportEtnaGes);

		while (resultSetImportGes.next()) {
			Gestore ges = new Gestore();
			ges.setCod_gestore(resultSetImportGes.getString("id"));
			ges.setDenominazione(resultSetImportGes.getString("name"));
			ges.setPiva(resultSetImportGes.getString("vat_id"));
			ges.setCf(resultSetImportGes.getString("fiscal_code"));

			if (resultSetImportGes.getString("tel") != null
					|| resultSetImportGes.getString("tel") != "")
				ges.setTelefono(resultSetImportGes.getString("tel"));
			else
				ges.setTelefono(resultSetImportGes.getString("mobile"));

			ges.setFax(resultSetImportGes.getString("fax"));
			ges.setEmail(resultSetImportGes.getString("email"));
			ges.setToponimo(resultSetImportGes.getInt("toponimo_id"));
			ges.setIndirizzo(resultSetImportGes.getString("address"));
			ges.setNumero_civico(resultSetImportGes.getString("address_number"));
			ges.setCap(resultSetImportGes.getString("postcode"));
			ges.setComune(resultSetImportGes.getString("comune_name"));

			listGestoreEtna.add(ges);
		}

		logger.info("End - Get Gestore from Etna");

		resultSetImportGes.close();
		statementImport.close();
		return listGestoreEtna;

	}

	public static HashMap<String,Gestore> getAllGestoreStaging(
			Connection connectionEmport) throws SQLException {
		HashMap<String,Gestore> hashGestoreStaging = new HashMap<String,Gestore>();

		logger.info("Start - Get Gestore from Staging");

		String sqlImportStagingGes = "select COD_GESTORE,DENOMINAZIONE,PIVA,TELEFONO,FAX,EMAIL,TOPONIMO,INDIRIZZO,CIVICO,CAP,COMUNE,CF from BIRSGESTORE";

		Statement statementImportStagingeGes= connectionEmport
				.createStatement();
		ResultSet resultSetImportStagingGes = statementImportStagingeGes
				.executeQuery(sqlImportStagingGes);

		while (resultSetImportStagingGes.next()) {
			Gestore ges = new Gestore();
			ges.setCod_gestore(resultSetImportStagingGes
					.getString("COD_GESTORE"));
			ges.setDenominazione(resultSetImportStagingGes
					.getString("DENOMINAZIONE"));
			ges.setPiva(resultSetImportStagingGes.getString("PIVA"));
			ges.setTelefono(resultSetImportStagingGes.getString("TELEFONO"));
			ges.setFax(resultSetImportStagingGes.getString("FAX"));
			ges.setEmail(resultSetImportStagingGes.getString("EMAIL"));
			ges.setToponimo(resultSetImportStagingGes.getInt("TOPONIMO"));
			ges.setIndirizzo(resultSetImportStagingGes.getString("INDIRIZZO"));
			ges.setNumero_civico(resultSetImportStagingGes.getString("CIVICO"));
			ges.setCap(resultSetImportStagingGes.getString("CAP"));
			ges.setComune(resultSetImportStagingGes.getString("COMUNE"));
			ges.setCf(resultSetImportStagingGes.getString("CF"));
			hashGestoreStaging.put(ges.getCod_gestore(), ges);
		}

		logger.info("End - Get Esercente from Staging");

		statementImportStagingeGes.close();
		resultSetImportStagingGes.close();

		return hashGestoreStaging;
	}

	public static void insertGestore(Connection connEtna,
			Connection connStaging) throws SQLException {

		ArrayList<Gestore> listGesEtna = getAllGestoreEtna(connEtna);
		HashMap<String, Gestore> hashGesStaging = getAllGestoreStaging(connStaging);

		connStaging.setAutoCommit(true);

		logger.info("Start - Insert Gestore to Staging");

		for (Gestore actGes : listGesEtna) {

			if (!hashGesStaging.containsKey(actGes.getCod_gestore())) {

				String sqlInsertEse = "INSERT INTO BIRSGESTORE (COD_GESTORE,DENOMINAZIONE,PIVA,TELEFONO,FAX,EMAIL,TOPONIMO,INDIRIZZO,CIVICO,"
						+ "CAP,COMUNE,CF)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = connStaging
						.prepareStatement(sqlInsertEse);

				ps.setString(1, actGes.getCod_gestore());
				ps.setString(2, actGes.getDenominazione().toUpperCase());
				ps.setString(3, actGes.getPiva());
				ps.setString(4, actGes.getTelefono());
				ps.setString(5, actGes.getFax());
				ps.setString(6, actGes.getEmail());
				ps.setInt(7, actGes.getToponimo());
				ps.setString(8, actGes.getIndirizzo());
				ps.setString(9, actGes.getNumero_civico());
				ps.setString(10, actGes.getCap());
				ps.setString(11, actGes.getComune());
				ps.setString(12, actGes.getCf());

				ps.executeUpdate();

				ps.close();
			} else {
				Gestore gesChanged = hashGesStaging.get(actGes.getCod_gestore());

				if (!gesChanged.equals(actGes)) {
					String sqlInsertVlt = "UPDATE BIRSGESTORE SET DENOMINAZIONE=? , PIVA=? , TELEFONO=? , FAX=? , EMAIL=? , TOPONIMO=? , INDIRIZZO=? , CIVICO=? ,"
							+ " CAP=? , COMUNE=? , CF=? WHERE COD_GESTORE like ?";
					PreparedStatement ps = connStaging
							.prepareStatement(sqlInsertVlt);

					ps.setString(1, actGes.getDenominazione().toUpperCase());
					ps.setString(2, actGes.getPiva());
					ps.setString(3, actGes.getTelefono());
					ps.setString(4, actGes.getFax());
					ps.setString(5, actGes.getEmail());
					ps.setInt(6, actGes.getToponimo());
					ps.setString(7, actGes.getIndirizzo());
					ps.setString(8, actGes.getNumero_civico());
					ps.setString(9, actGes.getCap());
					ps.setString(10, actGes.getComune());
					ps.setString(11, actGes.getCf());

					ps.setString(12, actGes.getCod_gestore());

					logger.debug("ACT GES: " + actGes.toString());
					logger.debug("GES CHANGE: " + gesChanged.toString());

					ps.executeUpdate();

					ps.close();
				}
			}
		}

		connStaging.setAutoCommit(false);

		logger.info("End - Insert Gestore to Staging");

	}

}
