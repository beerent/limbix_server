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
	private UserManager user_manager;

	public RequestHandler(){
		this.response_manager = new ResponseManager();
		this.date_util = new DateUtil();
		this.reminder_manager = new ReminderManager();
		this.user_manager = new UserManager();
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
		if(request_type == RequestType.add) request_response = handleAddReminderRequest(request);
		if(request_type == RequestType.get) request_response = handleGetRemindersRequest(request);
		if(request_type == RequestType.update_reminder) request_response = handleUpdateReminderRequest(request);
		if(request_type == RequestType.register_user) request_response = handleRegisterUserRequest(request);
		
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
		System.out.println(reminders.size());
		return this.response_manager.getRemindersSuccess(reminders);
		
	}
	
	private RequestResponse handleUpdateReminderRequest(Request request){

		int reminder_id = request.getReminderId();
		String reminder = request.getReminder();
		DateTime due_date = request.getDueDate();
		Boolean completed = request.getComplete();
		Boolean deleted = request.getDeleted();
		Reminder reminder_obj = this.reminder_manager.updateReminder(reminder_id, reminder, due_date, completed, deleted);
		if(reminder_obj == null)
			return this.response_manager.unableToUpdateReminder();
		return this.response_manager.updateReminderSuccess();
	}
	
	private RequestResponse handleRegisterUserRequest(Request request) {
		String username = request.getUsername();
		String password = request.getPassword();
		String first = request.getFirstName();
		String last = request.getLastName();
		String email = request.getEmail();
		this.user_manager.registerUser(username, email, first, last, password);
		return this.response_manager.registerUserSuccess();
	}

}
