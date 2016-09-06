package com.remindme.reminder;

import java.util.ArrayList;
import org.joda.time.DateTime;

import com.remindme.user.User;

public class Reminder {
	private String reminder;
	private User user;
	private int reminder_id;
	private boolean complete;
	private DateTime due_date;
	private DateTime created;
	private boolean deleted;
	
	private ArrayList<String> tags;

	public Reminder(int reminder_id, User user, String reminder, DateTime created, DateTime due_date,
			boolean complete, boolean deleted) {
		this.reminder_id = reminder_id;
		this.user = user;
		this.reminder = reminder;
		this.created = created;
		this.due_date = due_date;
		this.complete = complete;
		this.setDeleted(deleted);
	}

	public Reminder(String element, User user2, String element2, String element3, String element4, String complete2,
			String string) {
		// TODO Auto-generated constructor stub
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

	public DateTime getDueDate() {
		return due_date;
	}
	
	public String getDueDateString() {
		return due_date.toString();
	}

	public void setDueDate(DateTime due_date) {
		this.due_date = due_date;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public String toString(){
		return reminder + " | " +
		user + " | " +
		reminder_id + " | " +
		due_date + " | " +
		created + " | " +
		complete + " | " +
		deleted;
	}
}
