package com.remindme.util;

import java.io.StringReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONUtil {
	
	public JSONObject getJSONObect(String s){
		JSONParser parser = new JSONParser();
		try {
			JSONObject json_obj = (JSONObject) parser.parse(new StringReader(s));
			return json_obj;
		} catch (Exception e) {
			return null;
		}
	}	
	
	public static void main(String[] args) {
		JSONUtil ju = new JSONUtil();
		JSONObject jo = ju.getJSONObect("{\"response\":{\"op\":0,\"error\":\"Invalid username password combo.\"}}");
		System.out.println(jo != null);
		System.out.println(jo.get("response"));
	}
}