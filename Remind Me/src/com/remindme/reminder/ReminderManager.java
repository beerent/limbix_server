package com.remindme.reminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.remindme.server.request.RequestResult;
import com.remindme.user.User;
import com.remindme.user.UserManager;

public class ReminderManager {
	private static final int REMINDER_MAX_LENGTH = 300;
	private ReminderDAO reminder_dao;

	public ReminderManager(){
		this.reminder_dao = new ReminderDAO();
	}
	
	public ArrayList<String> getTags(String reminder){
		ArrayList<String> tags = new ArrayList<String>();
		Scanner sc = new Scanner(reminder);
		
		String token;
		while(sc.hasNext()){
			token = sc.next();
			if(token.charAt(0) == '#' && token.substring(0).matches("^.*[^a-zA-Z0-9 ].*$"))
				tags.add(token);
		}
		
		return tags;
	}
	
	public boolean validateReminder(String reminder){
		if(reminder == null) return false;
		if(reminder.length() > REMINDER_MAX_LENGTH) return false; 
		if(reminder.length() < 1) return false;
		
		return true;
	}

	public void addTags(int reminder_id, ArrayList<String> tags) {
		
	}

	public int addReminder(User user, String reminder) {
		return this.reminder_dao.insertReminder(user, reminder);
	}

	public int addReminder(User user, String reminder, DateTime due_date) {
		if(due_date == null)
			return addReminder(user, reminder);
		return -1;
	}
	
}
