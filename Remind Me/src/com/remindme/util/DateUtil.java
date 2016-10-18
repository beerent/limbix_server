package com.remindme.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {
	public DateUtil(){}
	
	public DateTime getDateTime(String s){
		if(s.contains(".0"))
			s = s.substring(0, s.lastIndexOf(".0"));

		DateTimeFormatter formatter = getDateTimeFormatter();
		DateTime dt;
		
		try{
			dt = formatter.parseDateTime(s);
		}catch(Exception e){
			dt = null;
		}
		
		return dt;
	}
	
	public String JodaDateTimeToSQLDateTime(DateTime date_time){
		DateTimeFormatter formatter = getDateTimeFormatter();
		return date_time.toString(formatter);
	}
	
	private DateTimeFormatter getDateTimeFormatter(){
		return DateTimeFormat.forPattern("yyyy-MM-dd");
	}
	
	public DateTime getCurrentDateTime(){
		return new LocalDateTime().toDateTime();
	}
	
	public String dateTimeToSimpleString(DateTime date){
		return date.toString().substring(0, 10);
	}
}
