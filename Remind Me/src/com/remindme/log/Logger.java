package com.remindme.log;

public class Logger {
	private static Logger logger = null;
	
	private Logger(){}
	
	public static Logger getInstance(){
		if(logger == null){
			Logger.logger = new Logger();
		}
		Logger.logger.log("starting logger");
		return Logger.logger;
	}
	
	private void log(String s){
		System.out.println(s);
	}
}
