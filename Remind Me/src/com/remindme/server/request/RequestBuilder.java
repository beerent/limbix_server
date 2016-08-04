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

		/**********************************************************
		 *       IDENTIFY THE REQUEST AND RETURN THE OBJECT       *     
		 **********************************************************/
		
		// ADD REMINDER REQUEST
		if(type.equals("add")) buildAddReminderRequest(request, json);
		
		// GET REMINDER REQUEST
		else if(type.equals("get")) buildGetRemindersRequest(request, json);
		
		// UPDATE REMINDER REQUEST
		else if(type.equals("update_reminder")) buildUpdateReminderRequest(request, json);
		
		// REGISTER USER REQUEST
		else if(type.equals("register_user")) buildRegisterUserRequest(request, json);
		
		return request;
	}
	
	/*
	 * Adds the appropriate fields for an ADD REMINDER request to 
	 * the request object.
	 */
	private void buildAddReminderRequest(Request request, JSONObject json){
		RequestType request_type = RequestType.add;
		String reminder = (String) json.get("reminder");
		ArrayList<String> tags = this.reminder_manager.getTags((String) json.get("reminder"));
		DateTime due_date = this.date_util.getDateTime((String) json.get("due_date"));
		DateTime created = this.date_util.getDateTime((String) json.get("current"));
		
		request.setRequestType(request_type);
		request.setReminder(reminder);
		request.setTags(tags);
		request.setDueDate(due_date);
		request.setCreatedDate(created);
	}
	
	/*
	 *  Adds the appropriate fields for a GET REMINDER request to
	 *  the request object.
	 */
	private void buildGetRemindersRequest(Request request, JSONObject json) {
		RequestType request_type = RequestType.get;
		ArrayList<String> tags = this.reminder_manager.getTagsFromRequest((String) json.get("tags"));
		DateTime due_date_before = this.date_util.getDateTime((String) json.get("due_date_before"));
		DateTime due_date = this.date_util.getDateTime((String) json.get("due_date"));
		DateTime due_date_after = this.date_util.getDateTime((String) json.get("due_date_after"));
		DateTime created_before = this.date_util.getDateTime((String) json.get("created_before"));
		DateTime created = this.date_util.getDateTime((String) json.get("created"));
		DateTime created_after = this.date_util.getDateTime((String) json.get("created_after"));
		Integer reminder_id;
		try{
			reminder_id = Integer.parseInt((String) json.get("reminder_id"));
		}catch(Exception e){
			reminder_id = null;
		}
		Boolean complete = Boolean.valueOf((String) json.get("complete"));

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
				
		request.setRequestType(request_type);
		
		Integer reminder_id;
		try{
			reminder_id = Integer.parseInt(reminder_id_str);
		}catch(Exception e){
			reminder_id = null;
		}
		
		request.setReminderId(reminder_id);
		request.setReminder(reminder);
		if(due_date_str != null) request.setDueDate(this.date_util.getDateTime(due_date_str));
		if(complete_str != null) request.setComplete(complete_str.charAt(0) == '1');
		if(deleted_str != null) request.setDeleted(deleted_str.charAt(0) == '1');
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
	
	
	
	public static void main(String[] args) {
		String response = "{ \"request\": { \"type\": \"add\", \"username\": \"user1\", \"password\": \"pass1\", \"reminder\": \"this is my first reminder\", \"due\": \"11/04/2016 22:40:40\",\"current\": \"21/7/2016 12:00:30\" } }";
		RequestBuilder request_builder = new RequestBuilder();
		request_builder.buildRequest(response);
	}

}
