package com.remindme.reminder;

import com.remindme.database.QueryResult;

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

}
