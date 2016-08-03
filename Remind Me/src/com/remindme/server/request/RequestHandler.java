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
	private DateUtil date_util;
	private ReminderManager reminder_manager;

	public RequestHandler(){
		this.response_manager = new ResponseManager();
		this.date_util = new DateUtil();
		this.reminder_manager = new ReminderManager();
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
		RequestType request_type = request.getRequestType();
		if(request_type == RequestType.add)
			request_response = handleAddReminderRequest(request);
		if(request_type == RequestType.get)
			request_response = handleGetRemindersRequest(request);
		
		return request_response;
	}

	/*
	 * does the work to add a reminder
	 */
	private RequestResponse handleAddReminderRequest(Request request){
		RequestResponse request_response = new RequestResponse();
	
		User user = request.getUser();
		String reminder_str = request.getReminder();
		String current_time = date_util.JodaDateTimeToSQLDateTime(date_util.getCurrentDateTime());
		DateTime due_date = null;
		if(request.getDueDate()!= null)
			due_date = request.getDueDate();
		ArrayList<String> tags = reminder_manager.getTags(reminder_str);
		
		Reminder reminder = reminder_manager.addReminder(user, reminder_str, current_time, date_util.JodaDateTimeToSQLDateTime(due_date));
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
	
	private RequestResponse handleGetRemindersRequest(Request request) {
		User user = request.getUser();
		ArrayList<String> tags = request.getTags();
		DateTime due_date_before = request.getDueDateBefore();
		DateTime due_date = request.getDueDate();
		DateTime due_date_after = request.getDueDateAfter();
		DateTime created_date_before = request.getCreatedDateBefore();
		DateTime created_date = request.getCreatedDate();
		DateTime created_date_after = request.getCreatedDateAfter();
		Integer reminder_id = request.getReminderId();
		String complete = "1";
		if(request.getComplete() == false)
			complete = "0";
		
		ArrayList<Reminder> reminders = 
				this.reminder_manager.getReminders(user, tags, due_date_before, due_date,
						due_date_after, created_date_before, created_date, created_date_after,
						reminder_id, complete);
		
		if(reminders.size() >= 500)
			return this.response_manager.tooManyRemindersFound();
		return this.response_manager.getRemindersSuccess(reminders);
		
	}

}
