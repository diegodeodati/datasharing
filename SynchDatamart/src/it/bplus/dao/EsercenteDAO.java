package it.bplus.dao;

import it.bplus.primitive.Esercente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class EsercenteDAO {

protected static Logger logger = Logger.getLogger(EsercenteDAO.class);
	
	public static ArrayList<Esercente> getAllEsercenteEtna(Connection connectionImport)  throws SQLException{
		
		logger.info("Start - Get Esercente from Etna");
		Statement statementImport = connectionImport.createStatement();
		ArrayList<Esercente> listEsercenteEtna = new ArrayList<Esercente>();
		
		String sqlImportEtnaEse = "select id,name,fiscal_code,vat_id,toponimo_id,address,address_number,postcode,tel,mobile,fax,cadastral_code,email,person_surname, person_name,person_fiscal_code,comune_name"
			+" from sc_merchant where toponimo_id is not null";
		ResultSet resultSetImportEse = statementImport.executeQuery(sqlImportEtnaEse);
		
		while(resultSetImportEse.next()){
			Esercente ese = new Esercente();
			ese.setCod_esercente(resultSetImportEse.getInt("id"));
			ese.setDenominazione(resultSetImportEse.getString("name"));
			ese.setPiva(resultSetImportEse.getString("vat_id"));
			ese.setCompany_cf(resultSetImportEse.getString("fiscal_code"));
			
			if(resultSetImportEse.getString("tel")!=null || resultSetImportEse.getString("tel")!="")
			ese.setTelefono(resultSetImportEse.getString("tel"));
			else
			ese.setTelefono(resultSetImportEse.getString("mobile"));
				
			ese.setFax(resultSetImportEse.getString("fax"));
			ese.setEmail(resultSetImportEse.getString("email"));
			ese.setToponimo(resultSetImportEse.getInt("toponimo_id"));
			ese.setIndirizzo(resultSetImportEse.getString("address"));
			ese.setNumero_civico(resultSetImportEse.getString("address_number"));
			ese.setCap(resultSetImportEse.getString("postcode"));
			ese.setComune(resultSetImportEse.getString("comune_name"));
			ese.setCadastral_code(resultSetImportEse.getString("cadastral_code"));
			ese.setPerson_name(resultSetImportEse.getString("person_name"));
			ese.setPerson_surname(resultSetImportEse.getString("person_surname"));
			ese.setPerson_cf(resultSetImportEse.getString("person_fiscal_code"));
			
			
			
			listEsercenteEtna.add(ese);
		}
		
		logger.info("End - Get Esercente from Etna");
		
		resultSetImportEse.close();
		statementImport.close();		
		return listEsercenteEtna;
		
	}

	public static HashMap<Integer, Esercente> getAllEsercenteStaging(Connection connectionEmport) throws SQLException  {
		   HashMap<Integer, Esercente> hashEsercenteStaging = new HashMap<Integer, Esercente>();
			
		   logger.info("Start - Get Esercente from Staging");
			
			String sqlImportStagingEse = "select COD_ESERCENTE,DENOMINAZIONE,PIVA,COMPANY_CF,TELEFONO,FAX,EMAIL,TOPONIMO,INDIRIZZO,CIVICO,CAP,COMUNE,CADASTRAL_CODE,PERSON_NAME,PERSON_SURNAME,PERSON_CF from BIRSESERCENTE";
		
			
			Statement statementImportStagingeEse = connectionEmport.createStatement();		
			ResultSet resultSetImportStagingEse = statementImportStagingeEse.executeQuery(sqlImportStagingEse);
		
			while(resultSetImportStagingEse.next()){
				Esercente ese = new Esercente();
				ese.setCod_esercente(resultSetImportStagingEse.getInt("COD_ESERCENTE"));
				ese.setDenominazione(resultSetImportStagingEse.getString("DENOMINAZIONE"));
				ese.setPiva(resultSetImportStagingEse.getString("PIVA"));
				ese.setCompany_cf(resultSetImportStagingEse.getString("COMPANY_CF"));
				ese.setTelefono(resultSetImportStagingEse.getString("TELEFONO"));
				ese.setFax(resultSetImportStagingEse.getString("FAX"));
				ese.setEmail(resultSetImportStagingEse.getString("EMAIL"));
				ese.setToponimo(resultSetImportStagingEse.getInt("TOPONIMO"));
				ese.setIndirizzo(resultSetImportStagingEse.getString("INDIRIZZO"));
				ese.setNumero_civico(resultSetImportStagingEse.getString("CIVICO"));
				ese.setCap(resultSetImportStagingEse.getString("CAP"));
				ese.setComune(resultSetImportStagingEse.getString("COMUNE"));
				ese.setCadastral_code(resultSetImportStagingEse.getString("CADASTRAL_CODE"));
				ese.setPerson_name(resultSetImportStagingEse.getString("PERSON_NAME"));
				ese.setPerson_surname(resultSetImportStagingEse.getString("PERSON_SURNAME"));
				ese.setPerson_cf(resultSetImportStagingEse.getString("PERSON_CF"));
				
				hashEsercenteStaging.put(ese.getCod_esercente(),ese);		
			}
			
			logger.info("End - Get Esercente from Staging");
			
			statementImportStagingeEse.close();
			resultSetImportStagingEse.close();			
			
		return hashEsercenteStaging;
	}

	
	public static void insertEsercente(Connection connEtna,Connection connStaging) throws SQLException{
		 
		   ArrayList<Esercente> listEseEtna = getAllEsercenteEtna(connEtna);
		   HashMap<Integer,Esercente> hashEseStaging = getAllEsercenteStaging(connStaging);
		   
		   connStaging.setAutoCommit(true);
		   
		   
		   logger.info("Start - Insert Esercente to Staging");
		   
		   for(Esercente actEse:listEseEtna){
			   
			   if(!hashEseStaging.containsKey(actEse.getCod_esercente()))
			   {
				   
				   
				   String sqlInsertEse = "INSERT INTO BIRSESERCENTE (COD_ESERCENTE,DENOMINAZIONE,PIVA,COMPANY_CF,TELEFONO,FAX,EMAIL,TOPONIMO,INDIRIZZO,CIVICO," +
				   		"CAP,COMUNE,CADASTRAL_CODE,PERSON_NAME,PERSON_SURNAME,PERSON_CF)"+
				   " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				   PreparedStatement ps = connStaging.prepareStatement(sqlInsertEse);
				   
				   ps.setInt(1,actEse.getCod_esercente());
				   ps.setString(2,actEse.getDenominazione().toUpperCase());
				   ps.setString(3,actEse.getPiva());
				   ps.setString(4, actEse.getCompany_cf());
				   ps.setString(5, actEse.getTelefono());  
				   ps.setString(6, actEse.getFax());  
				   ps.setString(7, actEse.getEmail());  
				   ps.setInt(8, actEse.getToponimo());
				   ps.setString(9,actEse.getIndirizzo());
				   ps.setString(10, actEse.getNumero_civico());
				   ps.setString(11,actEse.getCap());
				   ps.setString(12,actEse.getComune());
				   ps.setString(13,actEse.getCadastral_code());
				   ps.setString(14,actEse.getPerson_name());
				   ps.setString(15,actEse.getPerson_surname());
				   ps.setString(16,actEse.getPerson_cf());
				   
				   ps.executeUpdate();
				   
				   ps.close();
			   }
			   else{
				   Esercente eseChanged = hashEseStaging.get(actEse.getCod_esercente());
				   
				   if(!eseChanged.equals(actEse)){
					   String sqlInsertVlt = "UPDATE BIRSESERCENTE SET DENOMINAZIONE=? , PIVA=? , COMPANY_CF=? , TELEFONO=? , FAX=? , EMAIL=? , TOPONIMO=? , INDIRIZZO=? , CIVICO=? ," +
				   		" CAP=? , COMUNE=? , CADASTRAL_CODE=? , PERSON_NAME=? , PERSON_SURNAME=? , PERSON_CF=? WHERE COD_ESERCENTE like ?";
					   PreparedStatement ps = connStaging.prepareStatement(sqlInsertVlt);
					   
					   ps.setString(1,actEse.getDenominazione().toUpperCase());
					   ps.setString(2,actEse.getPiva());
					   ps.setString(3,actEse.getCompany_cf());
					   ps.setString(4,actEse.getTelefono());
					   ps.setString(5, actEse.getFax());
					   ps.setString(6, actEse.getEmail());
					   ps.setInt(7, actEse.getToponimo());
					   ps.setString(8,actEse.getIndirizzo());
					   ps.setString(9,actEse.getNumero_civico());
					   ps.setString(10,actEse.getCap());
					   ps.setString(11,actEse.getComune());
					   ps.setString(12,actEse.getCadastral_code());
					   
					   ps.setString(13,actEse.getPerson_name());
					   ps.setString(14,actEse.getPerson_surname());
					   ps.setString(15,actEse.getPerson_cf());
					   
					   ps.setInt(16,actEse.getCod_esercente());
					   
					   logger.debug("ACT ESE: "+actEse.toString());
					   logger.debug("ESE CHANGE: "+eseChanged.toString());
					   
					   ps.executeUpdate();
					   
					   ps.close();
				   }		
			   }
		   }  
			   
		   connStaging.setAutoCommit(false);
		   
		   logger.info("End - Insert Esercente to Staging");
		   
	   }
	
	
}
