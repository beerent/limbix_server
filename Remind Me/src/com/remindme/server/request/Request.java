package com.remindme.server.request;

import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.remindme.user.User;

public class Request {
	private RequestType request_type;
	private String username;
	private String password;
	private User user;
	private String reminder;
	private int reminder_id;
	private DateTime due_date;
	private DateTime created_date;
	private boolean complete;
	private ArrayList<String> tags;
	
	private boolean contains_required_fields;
	private boolean confirmed_fields;
	
	public Request(){
		this.tags = new ArrayList<String>();
		this.contains_required_fields = false;
		this.confirmed_fields = false;
	}
	
	public boolean containsRequiredFields(){
		return this.contains_required_fields;
	}
	
	public boolean confirmedFields(){
		return this.confirmed_fields;
	}
	
	public void setContainsRequiredFields(boolean b){
		this.contains_required_fields = b;
	}
	
	public void setConfirmedFields(boolean b){
		this.confirmed_fields = b;
	}
	
	public void setRequestType(RequestType request_type) { this.request_type = request_type; }
	public RequestType getRequestType() { return this.request_type; }
	
	public void setUsername(String username){ this.username = username; }
	public String getUsername(){ return this.username; }
	
	public void setPassword(String password){ this.password = password; }
	public String getPassword(){ return this.password; }
	
	public void setReminder(String reminder) { this.reminder = reminder; }
	public String getReminder() { return this.reminder; }
	
	public void setReminderId(int reminder_id) { this.reminder_id = reminder_id; }
	public int getReminderId() { return this.reminder_id; }
	
	public void setDueDate(DateTime due_date) { this.due_date = due_date; }
	public DateTime getDueDate() { return this.due_date; }
	
	public void setCreatedDate(DateTime created_date) { this.created_date = created_date; }
	public DateTime getCreatedDate() { return this.created_date; }
	
	public void setComplete(boolean complete){ this.complete = complete; }
	public boolean getComplete() { return this.complete; }
	
	public void setTags(String reminder){
		if(reminder == null)
			return;
		
		Scanner sc = new Scanner(reminder);
		
		String token;
		while(sc.hasNext()){
			token = sc.next();
			if(token.charAt(0) == '#')
				addTag(token);
		}
	}
	
	public void addTag(String tag){
		if(!this.tags.contains(tag))
			this.tags.add(tag);
	}
	
	public ArrayList<String> getTags(){
		return this.tags;
	}
	
	public String toString(){
		String retstr =  "type: " + request_type;
		retstr += "\nusername: " + username;
		retstr += "\npassword" + password;
		retstr += "\nreminder : " + reminder;
		retstr += "\nreminder_id: " + reminder_id;
		retstr += "\ndue date: " + due_date;
		retstr += "\ncreated date: " + created_date;
		retstr += "\nis complete: " + complete;
		retstr += "\ntags: " + tags;
		return retstr;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser(){
		return this.user;
	}
	
}
