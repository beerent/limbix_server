package com.remindme.filter;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.database.QueryResult;
import com.remindme.reminder.TagManager;
import com.remindme.user.User;
import com.remindme.util.DateUtil;

public class FilterManager {
	private FilterDAO filter_dao;
	
	public FilterManager(){
		this.filter_dao = new FilterDAO();
	}

	public int setCustomFilter(User user, ArrayList<String> tags, DateTime due_date_before, DateTime due_date,
			DateTime due_date_after, DateTime created_date_before, DateTime created_date, DateTime created_date_after,
			Integer reminder_id, String complete, String deleted) {
		TagManager tag_manager = new TagManager();
		DateUtil date_util = new DateUtil();
		
		String tags_str = null;
		String due_date_before_str = null;
		String due_date_str = null;
		String due_date_after_str = null;
		
		String created_date_before_str = null;
		String created_date_str = null;
		String created_date_after_str = null;
		
		if (tags != null && tags.size() > 0)
			tags_str = tag_manager.getTagsCommaDelimited(tags);
		
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

		int filter_id = filter_dao.updateCustomFilter(
				user.getUserId(), tags_str, 
				due_date_before_str, due_date_str, due_date_after_str,
				created_date_before_str, created_date_str, 
				created_date_after_str, complete, deleted
		
		);
		
		setCurrentFilter(user, filter_id);
		
		return filter_id;
		
	}

	public void setCurrentFilter(User user, int filter_id) {
		filter_dao.setCurrentFilter(user.getUserId(), filter_id);
		
	}

	public void addFilter(User user, String filter_name, ArrayList<String> tags, DateTime due_date_before, DateTime due_date,
			DateTime due_date_after, DateTime created_date_before, DateTime created_date, DateTime created_date_after,
			Integer reminder_id, String complete, String deleted) {
		TagManager tag_manager = new TagManager();
		DateUtil date_util = new DateUtil();
		
		String tags_str = null;
		String due_date_before_str = null;
		String due_date_str = null;
		String due_date_after_str = null;
		
		String created_date_before_str = null;
		String created_date_str = null;
		String created_date_after_str = null;
		
		
		if (tags != null && tags.size() > 0)
			tags_str = tag_manager.getTagsCommaDelimited(tags);
		
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

		filter_dao.addFilter(
			user.getUserId(), filter_name, tags_str, 
			due_date_before_str, due_date_str, due_date_after_str,
			created_date_before_str, created_date_str, 
			created_date_after_str, reminder_id, complete, deleted
		
		);
		
	}

	public ArrayList<Filter> getFiltersMeta(User user) {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		QueryResult results = filter_dao.getFiltersMeta(user.getUserId());
		for(int i = 0; i < results.numRows(); i++){
			filters.add(new Filter(results.getElement(i, 0), Integer.parseInt(results.getElement(i, 1))));
		}
		
		return filters;
	}

	public Filter getFilter(User user, Integer filter_id) {
		DateUtil date_util = new DateUtil();
		QueryResult result = filter_dao.getFilter(user.getUserId(), filter_id);
		String filter_name = result.getElement(0, 0);
		
		//skip, filter_id was passed in as parameter.
		//String filter_id_str = result.getElement(0, 1);
		
		String tags = result.getElement(0, 2);
		
		String created_before_str = result.getElement(0, 3);
		String created_str = result.getElement(0, 4);
		String created_after_str = result.getElement(0, 5);
		
		String due_before_str = result.getElement(0, 6);
		String due_str = result.getElement(0, 7);
		String due_after_str = result.getElement(0, 8);
		
		String completed_str = result.getElement(0, 9);
		String deleted_str = result.getElement(0, 10);
		
		DateTime created_before = null;
		DateTime created = null;
		DateTime created_after = null;
		
		DateTime due_before = null;
		DateTime due = null;
		DateTime due_after = null;
		
		Boolean completed = null;
		Boolean deleted = null;
		
		if(created_before_str != null && !created_before_str.equals("null"))
			created_before = date_util.getDateTime(created_before_str);
		if(created_str != null && !created_str.equals("null"))
			created = date_util.getDateTime(created_str);
		if(created_after_str != null && !created_after_str.equals("null"))
			created_after = date_util.getDateTime(created_after_str);
		
		if(due_before_str != null && !due_before_str.equals("null"))
			due_before = date_util.getDateTime(due_before_str);
		if(due_str != null && !due_str.equals("null"))
			due = date_util.getDateTime(due_str);
		if(due_after_str != null && !due_after_str.equals("null"))
			due_after = date_util.getDateTime(due_after_str);
		
		if(completed_str != null && !completed_str.equals("null"))
			completed = completed_str.equals("1");
		
		if(deleted_str != null && !deleted_str.equals("null"))
			deleted = deleted_str.equals("1");
		
		System.out.println("tags: " + tags);
		
		Filter filter = new Filter(
			filter_name, filter_id, tags, 
			created_before, created, created_after, 
			due_before, due, due_after, 
			completed, deleted
		);
		
		return filter;
	}

	public boolean getDeleteFilter(User user, Integer filter_id) {
		this.filter_dao.deleteFilter(user.getUserId(), filter_id);
		return true;
	}

	public Filter getCurrentUserFilter(User user) {
		QueryResult result = this.filter_dao.getCustomFilterId(user.getUserId());
		if(!result.containsData())
			return null;
		return getFilter(user, Integer.parseInt(result.getElement(0, 0)));
	}

}
