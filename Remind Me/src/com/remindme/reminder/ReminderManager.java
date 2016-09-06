package com.remindme.reminder;

import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.remindme.database.QueryResult;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;
import com.remindme.user.User;
import com.remindme.user.UserManager;
import com.remindme.util.DateUtil;

public class ReminderManager {
	private static final int REMINDER_MAX_LENGTH = 300;
	
	private ReminderDAO reminder_dao;
	private ResponseManager response_manager;
	private DateUtil date_util;

	public ReminderManager(){
		this.reminder_dao = new ReminderDAO();
		this.response_manager = new ResponseManager();
		this.date_util = new DateUtil();
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
	
	public ArrayList<String> getStringsFromCommaDeliminatedString(String tags){
		if(tags.equals("")) return new ArrayList<String>();
		
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
			tags_string += ", " + tags.get(i);
		}
		
		return tags_string;
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
		QueryResult result = this.reminder_dao.getReminder(reminder_id);
		
		//reminder_id, user_id, reminder, created, due_date, complete, deleted
		
		//can be null
		String due_date_str = result.getElement(0, "due_date");
		DateTime due_date = null;
		try{ due_date = date_util.getDateTime(due_date_str); }catch(Exception e){};
		
		//cant be null
		reminder_id = Integer.parseInt(result.getElement(0, "reminder_id"));
		int user_id = Integer.parseInt(result.getElement(0, "user_id"));
		User user = new UserManager().getUser(user_id);
		String reminder = result.getElement(0, "reminder");
		DateTime created = date_util.getDateTime(result.getElement(0, "created"));
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
			Integer reminder_id, String complete_str) {
		
		String tags_str = null;
		String due_date_before_str = null;
		String due_date_str = null;
		String due_date_after_str = null;
		String created_date_before_str = null;
		String created_date_str = null;
		String created_date_after_str = null;
		Integer reminder_id_str = null;
		
		//optional fields
		try{ tags_str = getTagsCommaDelimited(tags); }catch(Exception e){}
		try{ due_date_before_str = this.date_util.JodaDateTimeToSQLDateTime(due_date_before); }catch(Exception e){}
		try{ due_date_str = this.date_util.JodaDateTimeToSQLDateTime(due_date); }catch(Exception e){}
		try{ due_date_after_str = this.date_util.JodaDateTimeToSQLDateTime(due_date_after); }catch(Exception e){}
		try{ created_date_before_str = this.date_util.JodaDateTimeToSQLDateTime(created_date_before); }catch(Exception e){}
		try{ created_date_str = this.date_util.JodaDateTimeToSQLDateTime(created_date); }catch(Exception e){}
		try{ created_date_after_str = this.date_util.JodaDateTimeToSQLDateTime(created_date_after); }catch(Exception e){}
		QueryResult results = this.reminder_dao.getReminders(
				user.getUserId(),
				tags_str, 
				due_date_before_str,
				due_date_str,
				due_date_after_str,
				created_date_before_str,
				created_date_str,
				created_date_after_str,
				reminder_id_str, 
				complete_str
			);
		
		//int reminder_id, User user, String reminder, DateTime created, DateTime due_date,
		//boolean complete, boolean deleted
		
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		Boolean complete = false;
		Boolean deleted = false;

		String reminder;
		for(int i = 0; i < results.numRows(); i++){
			reminder_id = null;
			due_date_str = null;
			due_date = null;
			created_date_str = null;
			created_date = null;
			reminder = null;
			complete = null;
			deleted = null;
			
			reminder = results.getElement(i, "reminder");
			created_date_str = results.getElement(i, "created");
			due_date_str = results.getElement(i, "due_date");
			try { reminder_id = Integer.parseInt(results.getElement(i, "reminder_id")); }catch(Exception e){e.printStackTrace();}
			try { due_date = this.date_util.getDateTime(due_date_str); }catch(Exception e){}
			try { created_date = this.date_util.getDateTime(created_date_str); }catch(Exception e){}
			try { complete = results.getElement(i, "complete").equals("1"); }catch(Exception e){}
			try { deleted = results.getElement(i, "deleted").equals("1"); }catch(Exception e){}
			
			reminders.add(new Reminder(reminder_id, user, reminder, created_date, due_date, complete, deleted));
		}
		return reminders;
	}

	public Reminder updateReminder(int reminder_id, String reminder, DateTime due_date, Boolean remove_due_date,
			Boolean complete, Boolean deleted) {
		String complete_str = null;
		if(complete != null){
			complete_str = "1";
			if(!complete)
				complete_str = "0";
		}
		String deleted_str = null;
		if(deleted != null){
			deleted_str = "1";
			if(!deleted)
				deleted_str = "0";
		}
		
		String due_date_str = null;
		try{ due_date_str = this.date_util.JodaDateTimeToSQLDateTime(due_date); }catch(Exception e){}
		
		boolean result = this.reminder_dao.updateReminder(
				reminder_id, 
				reminder,
				due_date_str,
				remove_due_date,
				complete_str,
				deleted_str);
		//if this worked and reminder not null aka tags potentially exist
		if(result && reminder != null)
			this.reminder_dao.updateTags(reminder_id, getTags(reminder));
		
		return getReminder(reminder_id);
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
