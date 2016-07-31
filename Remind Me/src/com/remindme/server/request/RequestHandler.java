package com.remindme.server.request;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.reminder.Reminder;
import com.remindme.reminder.ReminderManager;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;
import com.remindme.user.User;
import com.remindme.user.UserManager;
import com.remindme.util.DateUtil;

public class RequestHandler {
	private ResponseManager response_manager;

	public RequestHandler(){
		this.response_manager = new ResponseManager();
	}
	
	public RequestResponse handleRequest(Request request) {
		if(!request.confirmedFields()){
			try {
				throw new Exception("cannot handle request, invalid fields.");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		RequestResponse request_response = null;
		if(request.getRequestType() == RequestType.add)
			request_response = handleAddReminderRequest(request);
		
		return request_response;
	}
	
	private RequestResponse handleAddReminderRequest(Request request){
		RequestResponse request_response = new RequestResponse();
		
		DateUtil date_util = new DateUtil();
		ReminderManager reminder_manager = new ReminderManager();
		User user = request.getUser();
		String reminder_str = request.getReminder();
		String current_time = date_util.JodaDateTimeToSQLDateTime(date_util.getCurrentDateTime());
		String due_date = null;
		if(request.getDueDate()!= null)
			due_date = date_util.JodaDateTimeToSQLDateTime(request.getDueDate());
		ArrayList<String> tags = reminder_manager.getTags(reminder_str);
		
		Reminder reminder = reminder_manager.addReminder(user, reminder_str, current_time, due_date);
		if(reminder == null){
			System.out.println("request from " + request.getUsername() + " failed to add");
			request_response = this.response_manager.unknownError();
		}else{
			System.out.println("request from " + request.getUsername() + " was a success!");
			reminder_manager.map_tags(reminder, tags);
			request_response = this.response_manager.addReminderSuccess();
		}
		
		return request_response;
	}

}
