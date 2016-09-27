package com.remindme.server.request;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.filter.FilterManager;
import com.remindme.reminder.Reminder;
import com.remindme.reminder.ReminderManager;
import com.remindme.reminder.Tag;
import com.remindme.reminder.TagManager;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;
import com.remindme.user.User;
import com.remindme.user.UserManager;
import com.remindme.util.DateUtil;

public class RequestHandler {
	private ResponseManager response_manager;
	private ReminderManager reminder_manager;
	private DateUtil date_util;
	private UserManager user_manager;
	private FilterManager filter_manager;
	private TagManager tag_manager;

	public RequestHandler(){
		this.response_manager = new ResponseManager();
		this.reminder_manager = new ReminderManager();
		this.date_util = new DateUtil();
		this.user_manager = new UserManager();
		this.filter_manager = new FilterManager();
		this.tag_manager = new TagManager();
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
		if(request_type == RequestType.login) request_response = handleLoginRequest(request);
		else if(request_type == RequestType.add) request_response = handleAddReminderRequest(request);
		else if(request_type == RequestType.get) request_response = handleGetRemindersRequest(request);
		else if(request_type == RequestType.get_tags) request_response = handleGetTagsRequest(request);
		else if(request_type == RequestType.update_reminder) request_response = handleUpdateReminderRequest(request);
		else if(request_type == RequestType.register_user) request_response = handleRegisterUserRequest(request);
		else if(request_type == RequestType.update_user) request_response = handleUpdateUserRequest(request);
		else if(request_type == RequestType.add_filter) request_response = handleAddFilterRequest(request);
		
		return request_response;
	}
	
	private RequestResponse handleLoginRequest(Request request){
		return this.response_manager.loginUserSuccess();
	}

	/*
	 * does the work to add a reminder
	 */
	private RequestResponse handleAddReminderRequest(Request request){
		TagManager tag_manager = new TagManager();
		RequestResponse request_response = null;
		
		User user               = request.getUser();
		String reminder_str     = request.getReminder();
		String current_date_str = date_util.JodaDateTimeToSQLDateTime(date_util.getCurrentDateTime());
		ArrayList<String> tags  = tag_manager.getTags(reminder_str);
		
		String due_date_str = null;
		try{ due_date_str = this.date_util.JodaDateTimeToSQLDateTime(request.getDueDate()); }catch(Exception e){}
		
		Reminder reminder = reminder_manager.addReminder(user, reminder_str, current_date_str, due_date_str);
		
		//REMINDER FAILED TO ADD
		if(reminder == null){
			request_response = this.response_manager.unknownError();
		
		//REMINDER WAS SUCCESSFULLY ADDED
		}else{
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
		String complete = null, deleted = null;
		if(request.getComplete() != null){
			if(request.getComplete() == true)
				complete = "1";
			else
				complete = "0";
		}
		
		if(request.getDeleted() != null){
			if(request.getDeleted() == true)
				deleted = "1";
			else
				deleted = "0";
		}
		
		
		ArrayList<Reminder> reminders = 
				this.reminder_manager.getReminders(user, tags, due_date_before, due_date,
						due_date_after, created_date_before, created_date, created_date_after,
						reminder_id, complete, deleted);
		
		//MAX AMOUNT OF REMINDERS ALLOWED
		if(reminders.size() >= 500)
			return this.response_manager.tooManyRemindersFound();
		
		return this.response_manager.getRemindersSuccess(reminders);
	}
	
	private RequestResponse handleGetTagsRequest(Request request) {
		User user = request.getUser();
		DateTime due_date_before = request.getDueDateBefore();
		DateTime due_date = request.getDueDate();
		DateTime due_date_after = request.getDueDateAfter();
		DateTime created_date_before = request.getCreatedDateBefore();
		DateTime created_date = request.getCreatedDate();
		DateTime created_date_after = request.getCreatedDateAfter();
		String complete = null, deleted = null;
		
		if(request.getComplete() != null){
			if(request.getComplete() == true)
				complete = "1";
			else
				complete = "0";
		}
		
		if(request.getDeleted() != null){
			if(request.getDeleted() == true)
				deleted = "1";
			else
				deleted = "0";
		}
		
		
		ArrayList<Tag> tags = 
				this.tag_manager.getTags(user, due_date_before, due_date,
						due_date_after, created_date_before, created_date, created_date_after,
						complete, deleted);
	
		
		return this.response_manager.getTagsSuccess(tags);
	}
	
	private RequestResponse handleUpdateReminderRequest(Request request){

		int reminder_id = request.getReminderId();
		String reminder = request.getReminder();
		DateTime due_date = request.getDueDate();
		Boolean remove_due_date = request.getRemoveDueDate();
		Boolean completed = request.getComplete();
		Boolean deleted = request.getDeleted();
		
		Reminder reminder_obj = this.reminder_manager.updateReminder(reminder_id, reminder, due_date, remove_due_date, completed, deleted);
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
		boolean success = this.user_manager.registerUser(username, email, first, last, password);
		if(!success)
			return this.response_manager.registerUserFailed();
		return this.response_manager.registerUserSuccess();
	}
	
	private RequestResponse handleUpdateUserRequest(Request request) {
		String new_username = request.getNewUsername();
		String new_password = request.getNewPassword1();
		String new_email = request.getNewEmail();
		String new_first = request.getNewFirstName();
		String new_last = request.getNewLastName();
		
		User user = this.user_manager.updateUser(request.getUser(), new_username, new_password, new_email, new_first, new_last);
		if(user == null)
			return this.response_manager.unableToUpdateUser();
		return this.response_manager.updateUserSuccess();
	}
	
	private RequestResponse handleAddFilterRequest(Request request) {
		User user = request.getUser();
		ArrayList<String> tags = request.getTags();
		DateTime due_date_before = request.getDueDateBefore();
		DateTime due_date = request.getDueDate();
		DateTime due_date_after = request.getDueDateAfter();
		DateTime created_date_before = request.getCreatedDateBefore();
		DateTime created_date = request.getCreatedDate();
		DateTime created_date_after = request.getCreatedDateAfter();
		Integer reminder_id = request.getReminderId();
		String complete = null, deleted = null;
		if(request.getComplete() != null){
			if(request.getComplete() == true)
				complete = "1";
			else
				complete = "0";
		}
		
		if(request.getDeleted() != null){
			if(request.getDeleted() == true)
				deleted = "1";
			else
				deleted = "0";
		}
		
		
		this.filter_manager.addFilter(user, tags, due_date_before, due_date,
				due_date_after, created_date_before, created_date, created_date_after,
				reminder_id, complete, deleted);
		
		//MAX AMOUNT OF REMINDERS ALLOWED
		//if(reminders.size() >= 500)
		//	return this.response_manager.tooManyRemindersFound();
		
		//return this.response_manager.getRemindersSuccess(reminders);
		return null;
	}

}
