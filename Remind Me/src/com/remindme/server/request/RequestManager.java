package com.remindme.server.request;

import com.remindme.reminder.ReminderManager;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;

public class RequestManager {
	private ResponseManager response_manager;
	
	public RequestManager(){
		response_manager = new ResponseManager();
	}

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

	public RequestResponse vertifyRequestFields(Request request) {
		RequestResponse response = verifyCoreFields(request);
		if(response != null)
			return response;
		
		RequestType type = request.getRequestType();
		if(type == RequestType.add){
			return verifyAddReminderRequestFields(request);
		}else{
			response = response_manager.invalidRequestType();
			return response;
		}
	}

	private RequestResponse verifyAddReminderRequestFields(Request request) {
		if(request.getReminder() == null)
			return response_manager.missingReminder();
		return null;
	}
	
	public RequestResponse confirmFields(Request request){
		if(!request.containsRequiredFields())
			try {
				throw new Exception("cannot confirm fields, missing required fields");
			} catch (Exception e) {
				e.printStackTrace();
				return response_manager.missingRequiredField();
			}
		
		if(request.getRequestType() == RequestType.add)
			return confirmNewReminderFields(request);
		return response_manager.unknownError();
	}
	
	//validate:
	//username/ password
	//reminder
	private RequestResponse confirmNewReminderFields(Request request){
		ReminderManager reminder_manager = new ReminderManager();
		
		String reminder = request.getReminder();
		boolean valid_reminder = reminder_manager.validateReminder(reminder);
		if(!valid_reminder)
			return response_manager.invalidReminder();
		return null;
	}


}
