package com.remindme.server.request;

import org.joda.time.DateTime;

import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;

public class RequestValidator {
	private ResponseManager response_manager;
	private static final int REMINDER_MAX_LENGTH = 300;
	
	public RequestValidator(){
		this.response_manager = new ResponseManager();
	}

	public RequestResponse validateReminderString(String reminder) {
		if(reminder == null) return this.response_manager.missingReminder();
		if(reminder.length() > REMINDER_MAX_LENGTH) this.response_manager.reminderTooLong();
		if(reminder.length() < 1) return this.response_manager.reminderTooShort();
		return null;
	}

	public RequestResponse validateDueDate(DateTime due_date) {
		return null;
	}

	public RequestResponse validateComplete(boolean complete) {
		return null;
	}

	public RequestResponse validateDeleted(boolean deleted) {
		return null;
	}

}
