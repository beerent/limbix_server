package com.remindme.reminder;

import java.util.ArrayList;
import java.util.Date;

import com.remindme.user.User;

public class Reminder {
	private String reminder;
	private User user;
	private int reminder_id;
	private boolean complete;
	private Date due_date;
	private Date created;
	
	private ArrayList<String> tags;
	
	public Reminder(){}
	
	public Reminder(String reminder, User user, int reminder_id, boolean complete, Date due_date, Date created){
		
	}
	
	public Reminder(String reminder, User user, int reminder_id, boolean complete, String due_date, String created){
		
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getReminderId() {
		return reminder_id;
	}

	public void setReminderId(int reminder_id) {
		this.reminder_id = reminder_id;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Date getDueDate() {
		return due_date;
	}
	
	public String getDueDateString() {
		return due_date.toString();
	}

	public void setDueDate(Date due_date) {
		this.due_date = due_date;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public void addTag(String tag){
		this.tags.add(tag);
	}
}
