package it.bplus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static int day(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
	
	public static int week(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
	
	public static int tenDays(Date date) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int giorno = cal.get(Calendar.DAY_OF_MONTH);
        if (giorno==10)
        	return 1;
        if (giorno==20)
        	return 2;
        if (giorno==31 || giorno==30)
        	return 3;
        
        return (short) ((giorno / 10)+1);
    }
	
	public static Short fifteendays(Date date){
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int giorno = cal.get(Calendar.DAY_OF_MONTH);
        if (giorno==15)
        	return 1;
        if (giorno==30 || giorno==31)
        	return 2;
        
        return (short) ((cal.get(Calendar.DAY_OF_MONTH) / 15)+1);
	}
	
	
	public static int month(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1;
    }
	
	public static Short sixmonth(int mese){
		if (mese<=6)
			return 1;
		else return 2;
	}
	
	public static int year(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
	
	
	public static int hour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
	
	
	public static int minute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }


    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
    
   
    public static String formatter(Date date,String format){
    	SimpleDateFormat sf = new SimpleDateFormat(format);
    	String outDateFormat = sf.format(date);
    	return outDateFormat;
    }
    
    public static String unFormatter(String date,String format){
    	SimpleDateFormat sf = new SimpleDateFormat(format);
    	String outDateFormat = sf.format(date);
    	
    	return outDateFormat;
    }
        

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    } 
    
    
    
    public static Date creaData(int anno, int mese, int giorno, int ore, int minuti, int secondi, int millisecondi) {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false); // in caso di data non valida genera un'eccezione
        cal.set(Calendar.HOUR_OF_DAY, ore);
        cal.set(Calendar.MINUTE, minuti);
        cal.set(Calendar.SECOND, secondi);
        cal.set(Calendar.MILLISECOND, millisecondi);
        cal.set(Calendar.YEAR, anno);
        cal.set(Calendar.MONTH, mese-1);
        cal.set(Calendar.DAY_OF_MONTH, giorno);
        return cal.getTime();
    } 
    
    public static boolean isDateEquals(Date date1, Date date2) {
    	if(date1!=null && date2!=null)
        return toDate(date1).getTime() == toDate(date2).getTime();
    	else
    	if((date1!=null && date2==null) || (date1==null && date2!=null))
    		return false;
    	else
    		return true;
    	
    } 
    
    public static boolean isDateAfter(Date date1, Date date2) {
    	return toDate(date1).after(toDate(date2));
    } 
    
    public static boolean isDateBefore(Date date1, Date date2) {
    	return toDate(date1).before(toDate(date2));
    } 
  
    
    public static Date toDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }
    
    public static Date toDateUTC(Date date) throws ParseException {
        String pattern = "dd/MM/yyyy HH:mm:ss";
       
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
         
        SimpleDateFormat sdf1 = new SimpleDateFormat(pattern);
        Date timezone = sdf1.parse(sdf.format(date));

        return timezone;
    }
    
    
    public static String dateToString(Date date){
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);    	
    }
    
    
    public static boolean isPregress(Date dStart,Date dEnd){
    	int sHour = DateUtils.hour(dStart);
    	int sMinute = DateUtils.minute(dStart);
    	
    	int eHour = DateUtils.hour(dEnd);
    	int eMinute = DateUtils.minute(dEnd);	
    	
    	return sHour==0 && sMinute==00 && eHour==23 && eMinute==59;
    }
    
    public static boolean isEndOfDay(Date dStart,Date dEnd){    	
    	int eHour = DateUtils.hour(dEnd);
    	int eMinute = DateUtils.minute(dEnd);	
    	
    	return eHour==23 && eMinute==59;
    }
    
    public static boolean isRealTime(Date dStart,Date dEnd){
    	return !isPregress(dStart,dEnd);
    }
    }
