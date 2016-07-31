package com.remindme.server.response;

import org.json.simple.JSONObject;

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
	
	public RequestResponse addReminderSuccess() {
		return createErrorResponse("Reminder is successful");
	}
	
	public RequestResponse unknownError() {
		return createSuccessResponse("Request contains an unknown error. The error has been recorded.");
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
