package com.remindme.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {
	public DateUtil(){}
	
	public DateTime getDateTime(String s){
		if(s == null)
			return null;
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		DateTime dt = formatter.parseDateTime(s);
		return dt;
	}
}
