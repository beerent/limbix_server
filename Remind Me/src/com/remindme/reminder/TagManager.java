package com.remindme.reminder;

import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.remindme.database.QueryResult;
import com.remindme.server.response.RequestResponse;
import com.remindme.user.User;
import com.remindme.util.DateUtil;

public class TagManager {
	private ReminderDAO reminder_dao;
	
	public TagManager(){
		this.reminder_dao = new ReminderDAO();
	}

	public Tag getTag(String tag_str) {
		QueryResult result = reminder_dao.getTag(tag_str);
		if(!result.containsData())
			return null;
		
		return new Tag(Integer.parseInt(result.getElement(0, "tag_id")), result.getElement(0, "tag"));
	}

	public int addTag(String tag_str) {
		return reminder_dao.addTag(tag_str);
	}

	public void mapTag(Tag tag, Reminder reminder) {
		this.reminder_dao.mapTag(tag.getTagId(), reminder.getReminderId());	
	}
	
	public ArrayList<Tag> getTags(User user, DateTime due_date_before, DateTime due_date, DateTime due_date_after,
			DateTime created_date_before, DateTime created_date, DateTime created_date_after, String complete,
			String deleted) {
		
		DateUtil date_util = new DateUtil();
		
		String due_date_before_str = null;
		String due_date_str = null;
		String due_date_after_str = null;
		String created_date_before_str = null;
		String created_date_str = null;
		String created_date_after_str = null;
		
		if(due_date_before != null)
			due_date_before_str = date_util.JodaDateTimeToSQLDateTime(due_date_before);
		if(due_date != null)
			due_date_str = date_util.JodaDateTimeToSQLDateTime(due_date);
		if(due_date_after != null)
			due_date_after_str = date_util.JodaDateTimeToSQLDateTime(due_date_after);
		if(created_date_before != null)
			created_date_before_str = date_util.JodaDateTimeToSQLDateTime(created_date_before);
		if(created_date != null)
			created_date_str = date_util.JodaDateTimeToSQLDateTime(created_date);
		if(created_date_after != null)
			created_date_after_str = date_util.JodaDateTimeToSQLDateTime(created_date_after);
		
		ArrayList<Tag> tags = new ArrayList<Tag>();
		
		QueryResult results = this.reminder_dao.getTags(
				user.getUserId(),
				due_date_before_str,
				due_date_str,
				due_date_after_str,
				created_date_before_str,
				created_date_str,
				created_date_after_str, 
				complete,
				deleted
			);
		
		for(int i = 0; i < results.numRows(); i++)
			tags.add(new Tag(Integer.parseInt(results.getElement(i, 0)), results.getElement(i, 1)));
		
		return tags;
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
		if(tags == null || tags.equals("")) return null;
		
		String [] tags_array = tags.split(",");
		ArrayList<String> ret_array = new ArrayList<String>();
		for(String s : tags_array){
			ret_array.add(s.trim());
		}
		System.out.println("returning: " + ret_array);
		return ret_array;
	}

}
