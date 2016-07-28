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
}