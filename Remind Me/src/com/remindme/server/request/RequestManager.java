package com.remindme.server.request;

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
		this.user_manager = new UserManager();
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
		RequestResponse response;		
		RequestType type = request.getRequestType();
		if(type == RequestType.add) return verifyAddReminderRequestFields(request);
		else if(type == RequestType.get) return verifyGetReminderRequestFields(request);
		else if(type == RequestType.register_user) return verifyRegisterUserFields(request);
		else return response_manager.invalidRequestType();
	}

	private RequestResponse verifyAddReminderRequestFields(Request request) {
		if(request.getReminder() == null)
			return response_manager.missingReminder();
		return null;
	}
	
	private RequestResponse verifyGetReminderRequestFields(Request request) {
		/*
		 * NO REQUIREMENTS ON GET REQUEST AT THIS POINT
		 */
		return null;
	}

	private RequestResponse verifyRegisterUserFields(Request request){
		/*
		 * 		request.setRequestType(request_type);
		request.setUsername(username);
		request.setPassword(password1);
		request.setPassword2(password2);
		request.setFirstName(first_name);
		request.setLastName(last_name);
		request.setEmail(email);
		 */
		
		if(request.getUsername() == null) return response_manager.missingUsername();
		if(request.getPassword() == null) return response_manager.missingPassword();
		if(request.getPassword2() == null) return response_manager.missingPassword2();
		if(request.getFirstName() == null) return response_manager.missingFirstName();
		if(request.getLastName() == null) return response_manager.missingLastName();
		if(request.getEmail() == null) return response_manager.missingEmail();
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
		if(request.getRequestType() == RequestType.register_user) return confirmRegisterUserFields(request);
	
		return response_manager.unknownError();
	}

	//validate:
	//username/ password
	//reminder
	private RequestResponse confirmAddReminderFields(Request request){		
		String reminder = request.getReminder();
		boolean valid_reminder = reminder_manager.validateReminder(reminder);
		if(!valid_reminder)
			return response_manager.invalidReminder();
		if(reminder.length() > 300)
			return response_manager.reminderTooLong();
		return null;
	}

	private RequestResponse confirmGetRemindersFields(Request request) {
		return null;
	}
	
	private RequestResponse confirmRegisterUserFields(Request request) {
		
		//verify username
		String username = request.getUsername();
		if(!this.user_manager.usernameLongEnough(username))
			return this.response_manager.usernameTooShort();
		if(!this.user_manager.usernameShortEnough(username))
			return this.response_manager.usernameTooLong();
		if(!this.user_manager.usernameValidCharacters(username))
			return this.response_manager.invalidUsername();	
		if(!this.user_manager.containsAlpha(username))
			return this.response_manager.usernameMissingAlphaCharacter();
		if(this.user_manager.usernameExists(username))
			return this.response_manager.usernameAlreadyExists();
			
		//verify passwords
		String password = request.getPassword();
		String password2 = request.getPassword2();
		if(!this.user_manager.passwordsMatch(password, password2))
			return this.response_manager.passwordsDoNotMatch();
		if(!this.user_manager.passwordLongEnough(password))
			return this.response_manager.passwordTooShort();
		if(!this.user_manager.passwordShortEnough(password))
			return this.response_manager.passwordTooLong();
		if(!this.user_manager.containsAlpha(password))
			return this.response_manager.passwordMissingAlphaCharacter();
		if(!this.user_manager.containsNumeric(password))
			return this.response_manager.passwordMissingNumericCharacter();
		
		//verify first and last name
		String first_name = request.getFirstName();
		String last_name = request.getFirstName();
		if(!this.user_manager.firstLastNameLongEnough(first_name))
			return this.response_manager.firstNameTooShort();
		if(!this.user_manager.firstLastNameLongEnough(last_name))
			return this.response_manager.lastNameTooShort();
		if(!this.user_manager.firstLastNameShortEnough(first_name))
			return this.response_manager.firstNameTooLong();
		if(!this.user_manager.firstLastNameShortEnough(last_name))
			return this.response_manager.lastNameTooLong();
		if(!this.user_manager.validFirstOrLastName(first_name))
			return this.response_manager.invalidFirstName();
		if(!this.user_manager.validFirstOrLastName(last_name))
			return this.response_manager.invalidLastName();		
		
		//verify email
		String email = request.getEmail();
		if(!this.user_manager.validEmail(email))
			return this.response_manager.invalidEmail();
		
		return null;
	}
	
}
