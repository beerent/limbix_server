package com.remindme.server.request;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.reminder.ReminderManager;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;

public class RequestManager {
	private ResponseManager response_manager;
	
	public RequestManager(){
		response_manager = new ResponseManager();
	}
	
	/******************
	 * VERIFY SECTION *
	 ******************/

	public RequestResponse verifyCoreFields(Request request) {
		RequestResponse response = new RequestResponse();
		if(request.getUsername() == null){
			response = response_manager.missingUsername();
			return response;
		}
		
		if(request.getPassword() == null){
			response = response_manager.missingPassword();
			return response;
		}
		
		if(request.getRequestType() == null){
			response = response_manager.missingRequestType();
			return response;
		}
		return null;
	}

	public RequestResponse verifyRequestFields(Request request) {
		RequestResponse response = verifyCoreFields(request);
		if(response != null)
			return response;
		
		RequestType type = request.getRequestType();
		if(type == RequestType.add){
			return verifyAddReminderRequestFields(request);
		}else if(type == RequestType.get){
			return verifyGetReminderRequestFields(request);
		}else{
			response = response_manager.invalidRequestType();
			return response;
		}
	}

	private RequestResponse verifyGetReminderRequestFields(Request request) {
		/*
		 * NO REQUIREMENTS ON GET REQUEST AT THIS POINT
		 */
		return null;
	}

	private RequestResponse verifyAddReminderRequestFields(Request request) {
		if(request.getReminder() == null)
			return response_manager.missingReminder();
		return null;
	}

	
	
	
	
	/*******************
	 * CONFIRM SECTION *
	 *******************/
	
	public RequestResponse confirmFields(Request request){
		if(!request.containsRequiredFields())
			try {
				throw new Exception("cannot confirm fields, missing required fields");
			} catch (Exception e) {
				e.printStackTrace();
				return response_manager.missingRequiredField();
			}
		
		if(request.getRequestType() == RequestType.add)
			return confirmAddReminderFields(request);
		if(request.getRequestType() == RequestType.get)
			return confirmGetRemindersFields(request);
		return response_manager.unknownError();
	}

	//validate:
	//username/ password
	//reminder
	private RequestResponse confirmAddReminderFields(Request request){
		ReminderManager reminder_manager = new ReminderManager();
		
		String reminder = request.getReminder();
		boolean valid_reminder = reminder_manager.validateReminder(reminder);
		if(!valid_reminder)
			return response_manager.invalidReminder();
		return null;
	}

	private RequestResponse confirmGetRemindersFields(Request request) {
		/*int due_date_set = 0;
		if(request.getDueDateBefore() != null)
			due_date_set++;
		if(request.getDueDate() != null)
			due_date_set++;
		if(request.getDueDateAfter() != null)
			due_date_set++;
		if(due_date_set > 1)
			return response_manager.multipleDueDateConstraints();
		
		int created_date_set = 0;
		if(request.getCreatedDateBefore() != null)
			created_date_set++;
		if(request.getCreatedDate() != null)
			created_date_set++;
		if(request.getCreatedDateAfter() != null)
			created_date_set++;
		if(created_date_set > 1)
			return response_manager.multipleCreatedDateConstraints();*/
		return null;
	}
}
