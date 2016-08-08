package com.remindme.server.request;

import com.remindme.reminder.Reminder;
import com.remindme.reminder.ReminderManager;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;
import com.remindme.user.UserManager;

public class RequestManager {
	private ResponseManager response_manager;
	private ReminderManager reminder_manager;
	private UserManager user_manager;
	
	public RequestManager(){
		this.reminder_manager = new ReminderManager();
		this.response_manager = new ResponseManager();
		this.user_manager     = new UserManager();
	}
	
	/******************
	 * VERIFY SECTION *
	 ******************/

	public RequestResponse verifyRequestType(Request request) {
		RequestResponse response = new RequestResponse();

		if(request.getRequestType() == null){
			response = response_manager.missingRequestType();
			return response;
		}
		
		return null;
	}
	
	/* RETURNS NULL IF REQUEST USERNAME AND PASSWORD EXIST */
	/* RETURNS A REQUEST RESPONSE IF USERNAME OR PASSWORD DO NOT EXIST */
	
	public RequestResponse verifyRequestUsernameAndPasswordExist(Request request){
		RequestResponse response = new RequestResponse();
		if(request.getUsername() == null){
			response = response_manager.missingUsername();
			return response;
		}
		
		if(request.getPassword() == null){
			response = response_manager.missingPassword();
			return response;
		}
		
		return null;
	}

	public RequestResponse verifyRequestFields(Request request) {
		RequestType type = request.getRequestType();
		if(type == RequestType.add) return verifyAddReminderRequestFields(request);
		else if(type == RequestType.get) return verifyGetRemindersRequestFields(request);
		else if(type == RequestType.update_reminder) return verifyUpdateReminderRequestFields(request);
		else if(type == RequestType.register_user) return verifyRegisterUserFields(request);
		else if(type == RequestType.update_user) return verifyUpdateUserFields(request);
		else return response_manager.invalidRequestType();
	}

	private RequestResponse verifyAddReminderRequestFields(Request request) {
		if(request.getReminder() == null)
			return response_manager.missingReminder();
		return null;
	}
	
	private RequestResponse verifyGetRemindersRequestFields(Request request) {
		/*
		 * NO REQUIREMENTS ON GET REQUEST AT THIS POINT
		 */
		return null;
	}
	
	private RequestResponse verifyUpdateReminderRequestFields(Request request) {
		if(request.getReminderId() == null)
			return this.response_manager.invalidReminderId();
		if(request.getReminder() == null && 
				request.getDueDate() == null &&
				request.getComplete() == null &&
				request.getDeleted() == null)
			return this.response_manager.nothingToUpdate();
		return null;
	}

	private RequestResponse verifyRegisterUserFields(Request request){
		if(request.getUsername() == null) return response_manager.missingUsername();
		if(request.getPassword() == null) return response_manager.missingPassword();
		if(request.getPassword2() == null) return response_manager.missingPassword2();
		if(request.getFirstName() == null) return response_manager.missingFirstName();
		if(request.getLastName() == null) return response_manager.missingLastName();
		if(request.getEmail() == null) return response_manager.missingEmail();
		return null;
	} 

	private RequestResponse verifyUpdateUserFields(Request request) {
		String pass1 = request.getNewPassword1();
		String pass2 = request.getNewPassword2();
		if(pass1 != null && pass2 == null)
			return this.response_manager.missingSecondNewPassword();
		if(pass2 != null && pass1 == null)
			return this.response_manager.missingFirstNewPassword();
		if(request.getNewFirstName() == null &&
				request.getNewLastName() == null &&
				request.getEmail() == null &&
				request.getNewPassword1() == null &&
				request.getNewPassword2() == null &&
				request.getNewUsername() == null)
			return this.response_manager.nothingToUpdate();
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
		
		if(request.getRequestType() == RequestType.add) return confirmAddReminderFields(request);
		if(request.getRequestType() == RequestType.get) return confirmGetRemindersFields(request);
		if(request.getRequestType() == RequestType.update_reminder) return confirmUpdateReminderFields(request);
		if(request.getRequestType() == RequestType.register_user) return confirmRegisterUserFields(request);
		if(request.getRequestType() == RequestType.update_user) return confirmUpdateUserFields(request);
	
		return response_manager.unknownError();
	}

	//validate:
	//username/ password
	//reminder
	private RequestResponse confirmAddReminderFields(Request request){		
		String reminder = request.getReminder();
		return this.reminder_manager.validateReminderString(reminder);
	}

	private RequestResponse confirmGetRemindersFields(Request request) {
		return null;
	}
	
	private RequestResponse confirmUpdateReminderFields(Request request) {
		//verify request id exists for this user
		Reminder reminder = this.reminder_manager.getReminder(request.getReminderId());
		if(reminder == null)
			return this.response_manager.reminderIdDoesNotExistForUser();
		
		RequestResponse response = null;
		
		//if reminder string is invalid, return error
		if(request.getReminder() != null)
			response = this.reminder_manager.validateReminderString(reminder.toString());
		if(response != null)
			return response;
		
		//if reminder due date is invalid, return error
		if(request.getDueDate() != null)
			response = this.reminder_manager.validateDueDate(reminder.getDueDate());
		if(response != null)
			return response;
		
		//if reminder complete is not valid, return error
		if(request.getComplete() != null)
			response = this.reminder_manager.validateComplete(reminder.isComplete());
		if(response != null)
			return response;
		
		//if reminder deleted is not valid, return error
		if(request.getDeleted() != null)
			response = this.reminder_manager.validateDeleted(reminder.isDeleted());
		if(response != null)
			return response;
		
		return null;
	}
	
	private RequestResponse confirmRegisterUserFields(Request request) {
		RequestResponse response;
		
		//verify username
		String username = request.getUsername();
		response = this.user_manager.confirmUsername(username);
		if (response != null)
			return response;
			
		//verify passwords
		String password = request.getPassword();
		String password2 = request.getPassword2();
		response = this.user_manager.confirmPasswords(password, password2);
		if(response != null)
			return response;
		
		//verify first and last name
		String first_name = request.getFirstName();
		String last_name = request.getFirstName();
		response = this.user_manager.verifyName(first_name);
		if(response != null)
			return response;
		response = this.user_manager.verifyName(last_name);
		if(response != null)
			return response;
		
		//verify email
		String email = request.getEmail();
		response = this.user_manager.validEmail(email);
		
		return response;
	}
	
	private RequestResponse confirmUpdateUserFields(Request request){
		RequestResponse response;
		
		String username = request.getNewUsername();
		if(username != null){
			response = this.user_manager.confirmUsername(username);
			if (response != null)
				return response;
		}
		
		String new_password1 = request.getNewPassword1();
		String new_password2 = request.getNewPassword2();
		if(new_password1 != null && new_password2 != null){
			response = this.user_manager.confirmPasswords(new_password1, new_password2);
			if(response != null)
				return response;
		}
		
		String new_email = request.getNewEmail();
		if(new_email != null){
			response = this.user_manager.validEmail(new_email);
			if(response != null)
				return response;
		}
		
		String new_first = request.getNewFirstName();
		String new_last = request.getNewLastName();
		if(new_first != null ){
			response = this.user_manager.verifyName(new_first);
			if (response != null)
				return response;
		}
		if(new_last != null ){
			response = this.user_manager.verifyName(new_first);
			if (response != null)
				return response;
		}
		
		return null;
	}
	
}
