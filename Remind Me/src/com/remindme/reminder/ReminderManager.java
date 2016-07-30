package com.remindme.reminder;

import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.remindme.database.QueryResult;
import com.remindme.user.User;
import com.remindme.user.UserManager;
import com.remindme.util.DateUtil;

public class ReminderManager {
	private static final int REMINDER_MAX_LENGTH = 300;
	private ReminderDAO reminder_dao;

	public ReminderManager(){
		this.reminder_dao = new ReminderDAO();
	}
	
	/*
	 * scans through a reminder string, storing all tags into an arraylist,
	 * and return the arraylist.
	 */
	public ArrayList<String> getTags(String reminder){
		ArrayList<String> tags = new ArrayList<String>();
		Scanner sc = new Scanner(reminder);
		
		String token;
		while(sc.hasNext()){
			token = sc.next();
			if(token.charAt(0) == '#' && token.substring(0).matches("^.*[^a-zA-Z0-9 ].*$"))
				tags.add(token);
		}
		sc.close();
		
		return tags;
	}
	
	/*
	 * returns true if the reminder is valid
	 */
	public boolean validateReminder(String reminder){
		if(reminder == null) return false;
		if(reminder.length() > REMINDER_MAX_LENGTH) return false; 
		if(reminder.length() < 1) return false;
		
		return true;
	}

	/*
	 * adds a reminder without a date
	 */
	public Reminder addReminder(User user, String reminder, String current_time) {
		int reminder_id = this.reminder_dao.addReminder(user, reminder, current_time);
		if(reminder_id == -1)
			return null;
		
		return getReminder(reminder_id);		
	}
	
	/*
	 * Adds a reminder that has a due_date set
	 */
	public Reminder addReminder(User user, String reminder, String current_time, String due_date) {
		if(due_date == null)
			return addReminder(user, reminder, current_time);

		int reminder_id = this.reminder_dao.addReminder(user, reminder, current_time, due_date);
		if(reminder_id == -1)
			return null;
		
		return getReminder(reminder_id);	
	}
	
	/*
	 * returns a Reminder object
	 */
	public Reminder getReminder(int reminder_id){
		DateUtil date_util = new DateUtil();
			
		QueryResult result = this.reminder_dao.getReminder(reminder_id);
		//reminder_id, user_id, reminder, created, due_date, complete, deleted
		reminder_id = Integer.parseInt(result.getElement(0, "reminder_id"));
		int user_id = Integer.parseInt(result.getElement(0, "user_id"));
		User user = new UserManager().getUser(user_id);
		String reminder = result.getElement(0, "reminder");
		DateTime created = date_util.getDateTime(result.getElement(0, "created"));
		DateTime due_date = date_util.getDateTime(result.getElement(0, "due_date"));
		int complete_int = Integer.parseInt(result.getElement(0, "complete"));
		boolean complete = complete_int == 1;
		int deleted_int = Integer.parseInt(result.getElement(0, "deleted"));
		boolean deleted = deleted_int == 1;
		
		return new Reminder(reminder_id, user, reminder, created, due_date, complete, deleted);
	}

	/*
	 * adds tags to reminders
	 */
	public void map_tags(Reminder reminder, ArrayList<String> tags) {
		TagManager tag_manager = new TagManager();
		Tag tag;
		for(String tag_str : tags){
			tag = tag_manager.getTag(tag_str);
			if(tag == null){
				tag_manager.addTag(tag_str);
				System.out.println("found tag: " + tag);
				tag = tag_manager.getTag(tag_str);
			}
			tag_manager.mapTag(tag, reminder);
		}
	}
}
