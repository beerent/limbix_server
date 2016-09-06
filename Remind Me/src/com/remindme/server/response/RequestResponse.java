package com.remindme.server.response;

import org.json.simple.JSONObject;

public class RequestResponse {
	private JSONObject response_json;
	
	public RequestResponse(JSONObject json){
		this.response_json = json;
	}

	public RequestResponse() {
		// TODO Auto-generated constructor stub
	}

	public JSONObject getResponseJson() {
		return response_json;
	}

	public void setResponseJson(JSONObject response_json) {
		this.response_json = response_json;
	}
	
	@Override
	public String toString(){
		return this.response_json.toJSONString();
	}
}
