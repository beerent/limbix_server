package com.remindme.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {
	public DateUtil(){}
	
	public DateTime getDateTime(String s){
		if(s == null)
			return null;
		
		if(s == "null")
			return null;
		
		//if(s.split(" ").length == 1){
		//	s = s + " 00:00:00";
		//}
		
		if(s.contains(".0"))
			s = s.substring(0, s.lastIndexOf(".0"));
		
		DateTimeFormatter formatter = getDateTimeFormatter();
		DateTime dt = formatter.parseDateTime(s);

		return dt;
	}
	
	public String JodaDateTimeToSQLDateTime(DateTime date_time){
		if(date_time == null)
			return null;
		DateTimeFormatter formatter = getDateTimeFormatter();
		return date_time.toString(formatter);
	}
	
	private DateTimeFormatter getDateTimeFormatter(){
		return DateTimeFormat.forPattern("yyyy-MM-dd");
	}
	
	public DateTime getCurrentDateTime(){
		return new LocalDateTime().toDateTime();
	}
}
