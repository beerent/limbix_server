package com.remindme.server.request;

import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.remindme.reminder.ReminderManager;
import com.remindme.user.User;

public class Request {
	private RequestType request_type;
	private String username;
	private String password;
	private User user;
	private String reminder;
	private Integer reminder_id;
	private DateTime due_date;
	private DateTime due_date_before;
	private DateTime due_date_after;
	private DateTime created_date;
	private DateTime created_date_before;
	private DateTime created_date_after;
	private Boolean complete;
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
	
	public void setReminderId(Integer reminder_id) { this.reminder_id = reminder_id; }
	public Integer getReminderId() { return this.reminder_id; }
	
	public void setDueDate(DateTime due_date) { this.due_date = due_date; }
	public DateTime getDueDate() { return this.due_date; }
	
	public void setCreatedDate(DateTime created_date) { this.created_date = created_date; }
	public DateTime getCreatedDate() { return this.created_date; }
	
	public void setComplete(Boolean complete){ this.complete = complete; }
	public Boolean getComplete() { return this.complete; }

	public void setUser(User user) { this.user = user; }
	public User getUser(){ return this.user; }

	public DateTime getDueDateBefore() { return due_date_before; }
	public void setDueDateBefore(DateTime due_date_before) { this.due_date_before = due_date_before; }

	public DateTime getDueDateAfter() { return due_date_after; }
	public void setDueDateAfter(DateTime due_date_after) { this.due_date_after = due_date_after; }

	public DateTime getCreatedDateBefore() { return created_date_before; }
	public void setCreatedDateBefore(DateTime created_date_before) { this.created_date_before = created_date_before; }

	public DateTime getCreatedDateAfter() { return created_date_after; }
	public void setCreatedDateAfter(DateTime created_date_after) { this.created_date_after = created_date_after; }
	
	public void setTags(ArrayList<String> tags){
		if(tags == null)
			return;

		if(tags.size() == 0)
			return;
		
		this.tags = tags;
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
		retstr += "\ndue date before: " + due_date_before;
		retstr += "\ndue date: " + due_date;
		retstr += "\ndue date after: " + due_date_after;
		retstr += "\ncreated date before: " + created_date_before;
		retstr += "\ncreated date: " + created_date;
		retstr += "\ncreated date after: " + created_date_after;
		retstr += "\nis complete: " + complete;
		retstr += "\ntags: " + tags;
		return retstr;
	}
	
}
