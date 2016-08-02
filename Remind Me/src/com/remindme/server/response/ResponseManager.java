package com.remindme.server.response;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.remindme.reminder.Reminder;
import com.remindme.user.User;

public class ResponseManager {

	public RequestResponse invalidCredientials() {
		return createErrorResponse("Invalid username and/or password.");
	}

	public RequestResponse missingUsername() {
		return createErrorResponse("Request is missing username.");
	}
	
	public RequestResponse missingPassword() {
		return createErrorResponse("Request is missing password.");
	}
	
	public RequestResponse missingRequestType() {
		return createErrorResponse("Request is missing request type.");
	}
	
	public RequestResponse invalidRequestType() {
		return createErrorResponse("Request contains an invalid request type.");
	}
	
	public RequestResponse missingReminder() {
		return createErrorResponse("Request is missing reminder.");
	}
	
	public RequestResponse invalidReminder() {
		return createErrorResponse("Request contains an invalid reminder.");
	}
	
	public RequestResponse missingRequiredField() {
		return createErrorResponse("Request is missing one or more required fields.");
	}

	public RequestResponse invalidTag(String tag) {
		return createErrorResponse("Tag '" + tag + "' doesn't exist.");
	}
	
	public RequestResponse multipleDueDateConstraints() {
		return createErrorResponse("Too many Due Date constraints set. Only one allowed.");
	}
	
	public RequestResponse multipleCreatedDateConstraints() {
		return createErrorResponse("Too many Created Date constraints set. Only one allowed.");
	}
	
	public RequestResponse unknownError() {
		return createSuccessResponse("Request contains an unknown error. The error has been recorded.");
	}
	
	public RequestResponse addReminderSuccess() {
		return createErrorResponse("Reminder is successful");
	}
	
	public RequestResponse getRemindersSuccess(ArrayList<Reminder> reminders){
		return new RequestResponse(getRemindersJSON(reminders));
	}
	
	private JSONObject getRemindersJSON(ArrayList<Reminder> reminders){
		JSONObject reminders_json = new JSONObject();
		JSONObject return_json = new JSONObject();
		JSONArray reminders_array = new JSONArray();
		for(Reminder reminder : reminders){
			reminders_array.add(getReminderJSON(reminder));
		}
		reminders_json.put("reminder", reminders_array);
		return_json.put("reminders", reminders_json);
		return return_json;
	}
	
	private JSONObject getReminderJSON(Reminder reminder){
		JSONObject reminder_json = new JSONObject();
		JSONObject return_json = new JSONObject();
		
		reminder_json.put("reminder_id", "" + reminder.getReminderId());
		reminder_json.put("reminder", "" + reminder.getReminder());
		reminder_json.put("complete", "" + reminder.isComplete());
		reminder_json.put("due_date", "" + reminder.getDueDate());
		reminder_json.put("created_date", "" + reminder.getCreated());
		return reminder_json;
	}

	private RequestResponse createSuccessResponse(String message){
		JSONObject response = new JSONObject();
		response.put("success", message);
		JSONObject jo = new JSONObject();
		jo.put("response", response);
		return new RequestResponse(jo);
	}
	
	@SuppressWarnings("unchecked")
	private RequestResponse createErrorResponse(String error){
		JSONObject response = new JSONObject();
		response.put("error", error);
		JSONObject jo = new JSONObject();
		jo.put("response", response);
		return new RequestResponse(jo);
	}
}
