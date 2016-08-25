package com.remindme.server.request;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.simple.JSONObject;

import com.remindme.reminder.ReminderManager;
import com.remindme.util.DateUtil;
import com.remindme.util.JSONUtil;

public class RequestBuilder {

	ReminderManager reminder_manager;
	DateUtil date_util;

	public RequestBuilder(){
		this.reminder_manager = new ReminderManager();
		this.date_util = new DateUtil();
	}
	
	/*TYPES SUPPORTED:
	 * 
	 * TYPES IN PROGRESS:
	 *   - add
	 *   - get
	 *   - registeruser
	 *   - update
	 *   
	 * TYPES TO DO:
	 *   
	 *   
	 *
	 */
	public Request buildRequest(String response) {
		JSONUtil json_util = new JSONUtil();

		/* CREATE JSON OBJECT FROM REQUEST STRING*/
		System.out.println(response);
		JSONObject json = json_util.getJSONObect(response);
		json = (JSONObject) json.get("request");
		
		/*
		 * VERIFY CORE FIELDS ARE PRESENT
		 * without these, the rest of the request handling
		 * process cannot continue.
		 */
		String username = (String) json.get("username");
		String password = (String) json.get("password");
		String type = (String) json.get("type");
		
		Request request = new Request();
		
		/* 
		 * store username and password to request object
		 */
		request.setUsername(username);
		request.setPassword(password);

		if(type == null){
			request.setRequestType(null);
			return request;
		}
		
		/**********************************************************
		 *       IDENTIFY THE REQUEST AND RETURN THE OBJECT       *     
		 **********************************************************/
		// LOGIN REQUEST
		if(type.equals("login")) buildLoginUserRequest(request, json);
		// ADD REMINDER REQUEST
		if(type.equals("add")) buildAddReminderRequest(request, json);
		
		// GET REMINDER REQUEST
		else if(type.equals("get")) buildGetRemindersRequest(request, json);
		
		// UPDATE REMINDER REQUEST
		else if(type.equals("update_reminder")) buildUpdateReminderRequest(request, json);
		
		// REGISTER USER REQUEST
		else if(type.equals("register_user")) buildRegisterUserRequest(request, json);
		
		//UPDATE USER
		else if(type.equals("update_user")) buildUpdateUserRequest(request, json);
		
		return request;
	}
	
	private void buildLoginUserRequest(Request request, JSONObject json){
		RequestType request_type = RequestType.login;
		request.setRequestType(request_type);
	}

	/*
	 * Adds the appropriate fields for an ADD REMINDER request to 
	 * the request object.
	 */
	private void buildAddReminderRequest(Request request, JSONObject json){
		RequestType request_type = RequestType.add;
		String reminder = (String) json.get("reminder");
		String tags_str = (String) json.get("reminder");
		String due_date_str = (String) json.get("due_date");
		
		ArrayList<String> tags = null;
		DateTime due_date = null;
		
		try{ tags = this.reminder_manager.getTags(tags_str); }catch(Exception e){}
		try{ due_date = this.date_util.getDateTime(due_date_str); }catch(Exception e){}
		
		request.setRequestType(request_type);
		request.setReminder(reminder);
		request.setTags(tags);
		request.setDueDate(due_date);
	}
	
	/*
	 *  Adds the appropriate fields for a GET REMINDER request to
	 *  the request object.
	 */
	private void buildGetRemindersRequest(Request request, JSONObject json) {
		RequestType request_type = RequestType.get;		
		String tags_str = (String) json.get("tags");		
		String due_date_before_str = (String) json.get("due_date_before");		
		String due_date_str = (String) json.get("due_date");	
		String due_date_after_str = (String) json.get("due_date_after");		
		String created_before_str = (String) json.get("created_before");		
		String created_str = (String) json.get("created");		
		String created_after_str = (String) json.get("created_after");		
		String complete_str = (String) json.get("complete");
		
		ArrayList<String> tags = null;
		DateTime due_date_before = null;
		DateTime due_date = null;
		DateTime due_date_after = null;
		DateTime created_before = null;
		DateTime created = null;
		DateTime created_after = null;
		Integer reminder_id = null;
		Boolean complete = null;
		
		try { tags = this.reminder_manager.getStringsFromCommaDeliminatedString(tags_str); }catch(Exception e){}
		try { due_date_before = this.date_util.getDateTime(due_date_before_str); }catch(Exception e){}
		try { due_date = this.date_util.getDateTime(due_date_str); }catch(Exception e){}
		try { due_date_after = this.date_util.getDateTime(due_date_after_str); }catch(Exception e){}
		try { created_before = this.date_util.getDateTime(created_before_str); }catch(Exception e){}
		try { created = this.date_util.getDateTime(created_str); }catch(Exception e){}
		try { created_after = this.date_util.getDateTime(created_after_str); }catch(Exception e){}
		try { reminder_id = Integer.parseInt((String) json.get("reminder_id")); }catch(Exception e){}
		try { complete = Integer.parseInt(complete_str) == 1; }catch(Exception e){}

		request.setRequestType(request_type);
		request.setTags(tags);
		request.setDueDateBefore(due_date_before);
		request.setDueDate(due_date);
		request.setDueDateAfter(due_date_after);
		request.setCreatedDateBefore(created_before);
		request.setCreatedDate(created);
		request.setCreatedDateAfter(created_after);
		request.setReminderId(reminder_id);
		request.setComplete(complete);
	}
	
	private void buildUpdateReminderRequest(Request request, JSONObject json){
		RequestType request_type = RequestType.update_reminder;
		String reminder_id_str = (String) json.get("reminder_id");
		String reminder = (String) json.get("reminder");
		String due_date_str = (String) json.get("due_date");
		String complete_str = (String) json.get("complete");
		String deleted_str = (String) json.get("deleted");

		Integer reminder_id = null;
		DateTime due_date = null;
		Boolean complete = null;
		Boolean deleted = null;
		
		try{ reminder_id = Integer.parseInt(reminder_id_str); }catch(Exception e){}
		if(due_date_str.equals("-1"))
			request.setRemoveDueDate();
		else
			try{ due_date = this.date_util.getDateTime(due_date_str); }catch(Exception e){}
		try{ complete = complete_str.charAt(0) == '1'; }catch(Exception e){}
		try{ deleted = deleted_str.charAt(0) == '1'; }catch(Exception e){}
		
		request.setRequestType(request_type);
		request.setReminderId(reminder_id);
		request.setReminder(reminder);		
		request.setDueDate(due_date);
		request.setComplete(complete);
		request.setDeleted(deleted);
	}
	
	private void buildRegisterUserRequest(Request request, JSONObject json){
		RequestType request_type = RequestType.register_user;
		String username = (String) json.get("username");
		String password1 = (String) json.get("password");
		String password2 = (String) json.get("password2");
		String first_name = (String) json.get("first_name");
		String last_name = (String) json.get("last_name");
		String email = (String) json.get("email");
			
		request.setRequestType(request_type);
		request.setUsername(username);
		request.setPassword(password1);
		request.setPassword2(password2);
		request.setFirstName(first_name);
		request.setLastName(last_name);
		request.setEmail(email);
	}
	
	private void buildUpdateUserRequest(Request request, JSONObject json) {
		RequestType request_type = RequestType.update_user;
		String username_new = (String) json.get("username_new");
		String password1_new = (String) json.get("password1_new");
		String password2_new = (String) json.get("password2_new");
		String first_name_new = (String) json.get("first_name_new");
		String last_name_new = (String) json.get("last_name_new");
		String email_new = (String) json.get("email_new");
			
		request.setRequestType(request_type);
		request.setNewUsername(username_new);
		request.setNewPassword1(password1_new);
		request.setNewPassword2(password2_new);
		request.setNewFirstName(first_name_new);
		request.setNewLastName(last_name_new);
		request.setNewEmail(email_new);
	}
	
	
	
	public static void main(String[] args) {
		String response = "{ \"request\": { \"type\": \"add\", \"username\": \"user1\", \"password\": \"pass1\", \"reminder\": \"this is my first reminder\", \"due\": \"11/04/2016 22:40:40\",\"current\": \"21/7/2016 12:00:30\" } }";
		RequestBuilder request_builder = new RequestBuilder();
		request_builder.buildRequest(response);
	}

}
