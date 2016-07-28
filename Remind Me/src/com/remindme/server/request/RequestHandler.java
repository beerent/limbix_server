package com.remindme.server.request;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.reminder.ReminderManager;
import com.remindme.user.User;
import com.remindme.user.UserManager;

public class RequestHandler {

	public boolean containsRequiredFields(Request request) {
		if(request.getRequestType() == null)
			return false;
		if(request.getRequestType() == RequestType.add)
			return containsValidAddReminderFields(request);
		return false;
	}
	
	private boolean containsValidAddReminderFields(Request request){
		if(request.getRequestType() == null)
			return false;
		if(request.getUser() == null)
			return false;
		if(request.getReminder() == null)
			return false;
		return true;
	}
	
	public boolean confirmFields(Request request){
		if(!request.containsRequiredFields())
			try {
				throw new Exception("cannot confirm fields, missing required fields");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		
		if(request.getRequestType() == RequestType.add)
			return confirmNewReminderFields(request);
		return false;
	}
	
	
	//validate:
	//username/ password
	//reminder
	private boolean confirmNewReminderFields(Request request){
		ReminderManager reminder_manager = new ReminderManager();
		
		String reminder = request.getReminder();
		boolean valid_reminder = reminder_manager.validateReminder(reminder);
		if(!valid_reminder)
			return false;
		return true;
		
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
		ReminderManager reminder_manager = new ReminderManager();
		User user = request.getUser();
		String reminder = request.getReminder();
		DateTime due_date = request.getDueDate();
		ArrayList<String> tags = reminder_manager.getTags(reminder);
		
		int reminder_id = reminder_manager.addReminder(user, reminder);
		if(reminder_id == -1){
			System.out.println("request from " + request.getUsername() + " failed to add");
		}else{
			System.out.println("request from " + request.getUsername() + " was a success!");
			reminder_manager.addTags(reminder_id, tags);
		}
		RequestResponse request_response = new RequestResponse();
		
		return request_response;
	}

}
