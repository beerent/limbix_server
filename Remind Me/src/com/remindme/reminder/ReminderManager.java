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
		if(reminder == null)
			return null;
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
	
	public ArrayList<String> getTagsFromRequest(String tags){
		if(tags == null)
			return null;
		String [] tags_array = tags.split(",");
		ArrayList<String> ret_array = new ArrayList<String>();
		for(String s : tags_array){
			ret_array.add(s.trim());
		}
		return ret_array;
	}
	
	public String getTagsCommaDelimited(ArrayList<String> tags){
		if(tags.size() == 0)
			return null;
		
		if(tags.size() == 1)
			return tags.get(0);
		
		String tags_string = tags.get(0);
		for(int i = 1; i < tags.size(); i++){
			tags_string += ", " + tags.get(i) ;
		}
		
		return tags_string;
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
		int reminder_id = this.reminder_dao.addReminder(user.getUserId(), reminder, current_time);
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

		int reminder_id = this.reminder_dao.addReminder(user.getUserId(), reminder, current_time, due_date);
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

	public boolean tagExists(String tag) {
		QueryResult results = this.reminder_dao.getTag(tag);
		return results.containsData();
	}

	public ArrayList<Reminder> getReminders(User user, ArrayList<String> tags, DateTime due_date_before, DateTime due_date,
			DateTime due_date_after, DateTime created_date_before, DateTime created_date, DateTime created_date_after,
			Integer reminder_id, String complete) {
		ReminderManager reminder_manager = new ReminderManager();
		DateUtil date_util = new DateUtil();
		QueryResult results = this.reminder_dao.getReminders(user.getUserId(),
				reminder_manager.getTagsCommaDelimited(tags), 
				date_util.JodaDateTimeToSQLDateTime(due_date_before),
				date_util.JodaDateTimeToSQLDateTime(due_date),
				date_util.JodaDateTimeToSQLDateTime(due_date_after),
				date_util.JodaDateTimeToSQLDateTime(created_date_before),
				date_util.JodaDateTimeToSQLDateTime(created_date),
				date_util.JodaDateTimeToSQLDateTime(created_date_after),
				reminder_id, 
				complete);
		
		//int reminder_id, User user, String reminder, DateTime created, DateTime due_date,
		//boolean complete, boolean deleted
		
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		boolean complete_bool = false;
		boolean deleted = false;

		for(int i = 0; i < results.numRows(); i++){
			complete_bool = results.getElement(i, "complete").equals("1");
		
			reminder_id = Integer.parseInt(results.getElement(i, "reminder_id"));
			//user placeholder
			String reminder = results.getElement(i, "reminder");
			created_date = date_util.getDateTime(results.getElement(i, "created"));
			due_date = date_util.getDateTime(results.getElement(i, "due_date"));
			//complete_bool place holder
			//deleted place holder
			
			reminders.add(new Reminder(reminder_id, user, reminder, created_date, due_date, complete_bool, deleted));
		}
		return reminders;
	}
}
