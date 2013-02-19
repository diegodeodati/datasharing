package it.todatasharing.beans;

import it.todatasharing.business.Semaphore;
import it.todatasharing.stored.ImportPregress;
import it.todatasharing.util.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.DateSelectEvent;

@ManagedBean(name = "importerBean")
@SessionScoped
public class ImporterBean {

	protected Logger logger = Logger.getLogger(ImporterBean.class);

	private Date dataS;
	private Date dataE;

	private Date yesterday = DateUtils.addDays(new Date(),-1);

	private boolean dateChanged = true;
	private boolean errorDate = false;
	
	private Integer percentage = 0;
	
	ImportPregress iPregress ;
	

	public ImporterBean() {
		dataS = DateUtils.addDays(new Date(),-1);
		dataE = DateUtils.addDays(new Date(),-1);
	}

	public boolean isAvailable() {
		Semaphore s = Semaphore.getInstance();
		return !s.isSignal();
	}
	
	public boolean isDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(boolean dateChanged) {
		this.dateChanged = dateChanged;
	}

	public boolean isErrorDate() {
		return errorDate;
	}

	public void setErrorDate(boolean errorDate) {
		this.errorDate = errorDate;
	}

	public void setDataS(Date ds) {
		dataS = ds;
	}

	public void setDataE(Date de) {
		dataE = de;
	}

	public Date getDataS() {
		return dataS;
	}

	public Date getDataE() {
		return dataE;
	}

	public Date getYesterday() {
		return yesterday;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}


	public void changeDataS(DateSelectEvent event) {
		dateChanged = true;
		if (DateUtils.isDateAfter(event.getDate(), dataE)
				&& !DateUtils.isDateEquals(event.getDate(), dataE)) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"ERRORE DATA", DateUtils.dateToString(
									event.getDate(), "dd/MM/yyyy")
									+ " successiva a "
									+ DateUtils.dateToString(dataE,
											"dd/MM/yyyy")));
			errorDate = true;
		} else {
			errorDate = false;
		}
	}

	public void changeDataE(DateSelectEvent event) {
		dateChanged = true;
		if (DateUtils.isDateAfter(dataS, event.getDate())
				&& !DateUtils.isDateEquals(dataS, event.getDate())) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"ERRORE DATA", DateUtils.dateToString(dataS,
									"dd/MM/yyyy")
									+ " successiva a "
									+ DateUtils.dateToString(event.getDate(),
											"dd/MM/yyyy")));
			errorDate = true;
		} else {
			errorDate = false;
		}
	}

	public void doThis() throws InterruptedException {

		final String DATETIMEFORMATTERH24_KEY = "dd/MM/yyyy HH:mm:ss";
				
		this.percentage = new Integer(0);

		try {
			DateFormat formatter24H = new SimpleDateFormat(
					DATETIMEFORMATTERH24_KEY);

			String startdatehourIni = DateUtils.dateToString(dataS,
					"dd/MM/yyyy") + " 00:00:01";
			String startdatehourFin = DateUtils.dateToString(dataS,
					"dd/MM/yyyy") + " 23:59:59";
			String enddatehour = DateUtils.dateToString(dataE, "dd/MM/yyyy")
					+ " 23:59:59";

			Date datestartIni = formatter24H.parse(startdatehourIni);
			Date datestartFin = formatter24H.parse(startdatehourFin);
			Date dateEnd = formatter24H.parse(enddatehour);
			
			iPregress = new ImportPregress(datestartIni,datestartFin,dateEnd, this);
			
			new Thread(iPregress).start();
					
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	 public void onComplete() {  
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Import del Pregresso", "Completato"));  
	}
	
	 public void cancel() {
			percentage = 0;
		}


}
