package com.remindme.server.request;

import java.util.Date;

import org.json.simple.JSONObject;

import com.remindme.util.DateUtil;
import com.remindme.util.JSONUtil;

public class RequestBuilder {

	/*TYPES SUPPORTED:
	 * 
	 * TYPES IN PROGRESS:
	 *   - add
	 *   
	 * TYPES TO DO:
	 *   - get
	 *   - update
	 */
	public Request buildRequest(String response) {
		JSONUtil json_util = new JSONUtil();

		JSONObject json = json_util.getJSONObect(response);
		json = (JSONObject) json.get("request");
		
		String username = (String) json.get("username");
		String password = (String) json.get("password");
		String type = (String) json.get("type");
		
		Request request = new Request();
		
		if(username == null){
			request.setUsername(null);
			return request;
		}
		request.setUsername(username);
		
		if(password == null){
			request.setPassword(null);
			return request;
		}
		request.setPassword(password);
		
		if(type == null){
			request.setRequestType(null);
			return request;
		}

		if(type.equals("add")){
			buildAddRequest(request, json);
		}
		return request;
	}
	
	private void buildAddRequest(Request request, JSONObject json){
		DateUtil date_util = new DateUtil();

		request.setRequestType(RequestType.add);
		request.setReminder((String) json.get("reminder"));
		request.setTags((String) json.get("reminder"));
		request.setDueDate(date_util.getDateTime((String) json.get("due")));
		request.setCreatedDate(date_util.getDateTime((String) json.get("current")));
	}
	
	public static void main(String[] args) {
		String response = "{ \"request\": { \"type\": \"add\", \"username\": \"user1\", \"password\": \"pass1\", \"reminder\": \"this is my first reminder\", \"due\": \"11/04/2016 22:40:40\",\"current\": \"21/7/2016 12:00:30\" } }";
		RequestBuilder request_builder = new RequestBuilder();
		request_builder.buildRequest(response);
	}

}
