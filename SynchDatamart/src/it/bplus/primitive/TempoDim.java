package it.bplus.primitive;

// Generated 31-mar-2011 10.51.22 by Hibernate Tools 3.4.0.CR1

import it.bplus.util.DateUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tempodim generated by hbm2java
 */
public class TempoDim implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	private Date data;
	private int hour;
	private int minute;
	private int day;
	private int week;
	private int tendays;
	private int fifteendays;
	private int month;
	private int sixmonth;
	private int year;

	public TempoDim() {
	}

	public TempoDim(long id) {
		this.id = id;
	}

	public TempoDim(long id, Date data, int hour,int minute, int day, int week,
			int tendays, int fifteendays, int month, int sixmonth,
			int year) {
		this.id = id;
		this.data = data;
		this.hour = hour;
		this.minute = minute;
		this.day = day;
		this.week = week;
		this.tendays = tendays;
		this.fifteendays = fifteendays;
		this.month = month;
		this.sixmonth = sixmonth;
		this.year = year;
	}
	
	public TempoDim(long id, Date data) {
		this.id = id;
		this.data = data;
		this.hour = DateUtils.hour(data);
		this.minute = DateUtils.minute(data);
		this.day = DateUtils.day(data);
		this.week = DateUtils.week(data);
		this.tendays = DateUtils.tenDays(data);
		this.fifteendays = DateUtils.fifteendays(data);
		this.month = DateUtils.month(data);
		this.sixmonth = DateUtils.sixmonth(this.month);
		this.year = DateUtils.year(data);
	}
	
	
	public TempoDim(Date data) {
		this.data = data;
		this.hour = DateUtils.hour(data);
		this.minute = DateUtils.minute(data);
		this.day = DateUtils.day(data);
		this.week = DateUtils.week(data);
		this.tendays = DateUtils.tenDays(data);
		this.fifteendays = DateUtils.fifteendays(data);
		this.month = DateUtils.month(data);
		this.sixmonth = DateUtils.sixmonth(this.month);
		this.year = DateUtils.year(data);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getData() {		
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	public void setAllData(Date data) {
		this.data = data;
		this.hour = DateUtils.hour(data);
		this.minute = DateUtils.minute(data);
		this.day = DateUtils.day(data);
		this.week = DateUtils.week(data);
		this.tendays = DateUtils.tenDays(data);
		this.fifteendays = DateUtils.fifteendays(data);
		this.month = DateUtils.month(data);
		this.sixmonth = DateUtils.sixmonth(this.month);
		this.year = DateUtils.year(data);
	}
	

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getTendays() {
		return tendays;
	}

	public void setTendays(int tendays) {
		this.tendays = tendays;
	}

	public int getFifteendays() {
		return fifteendays;
	}

	public void setFifteendays(int fifteendays) {
		this.fifteendays = fifteendays;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getSixmonth() {
		return sixmonth;
	}

	public void setSixmonth(int sixmonth) {
		this.sixmonth = sixmonth;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TempoDim other = (TempoDim) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (day != other.day)
			return false;
		if (fifteendays != other.fifteendays)
			return false;
		if (hour != other.hour)
			return false;
		if (id != other.id)
			return false;
		if (minute != other.minute)
			return false;
		if (month != other.month)
			return false;
		if (sixmonth != other.sixmonth)
			return false;
		if (tendays != other.tendays)
			return false;
		if (week != other.week)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tempodim [id=" + id + ", data=" + data + ", hour=" + hour
				+ ", minute=" + minute + ", day=" + day + ", week=" + week
				+ ", tendays=" + tendays + ", fifteendays=" + fifteendays
				+ ", month=" + month + ", sixmonth=" + sixmonth + ", year="
				+ year + "]";
	}

}
