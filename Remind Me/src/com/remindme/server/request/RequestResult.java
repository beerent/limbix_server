package com.remindme.server.request;

import java.util.ArrayList;
import java.util.Date;

import com.remindme.user.User;

public class RequestResult {
	private User user;
	private boolean valid_reminder;

	public void setUser(User auth_user) {
		this.user = user;
	}

	public void setReminder(boolean valid_reminder) {
		this.valid_reminder = valid_reminder;
	}

	public void setDueDate(Date valid_due_date) {
		// TODO Auto-generated method stub
		
	}

	public void setTags(ArrayList<String> tags) {
		// TODO Auto-generated method stub
		
	}

}
