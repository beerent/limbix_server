package com.remindme.filter;

import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.database.QueryResult;
import com.remindme.reminder.TagManager;
import com.remindme.user.User;
import com.remindme.util.DateUtil;

public class FilterManager {
	private DateUtil date_util;
	private FilterDAO filter_dao;
	
	public FilterManager(){
		this.date_util = new DateUtil();
		this.filter_dao = new FilterDAO();
	}

	public void addFilter(User user, ArrayList<String> tags, DateTime due_date_before, DateTime due_date,
			DateTime due_date_after, DateTime created_date_before, DateTime created_date, DateTime created_date_after,
			Integer reminder_id, String complete_str, String deleted_str) {

		String tags_str = null;
		String due_date_before_str = null;
		String due_date_str = null;
		String due_date_after_str = null;
		String created_date_before_str = null;
		String created_date_str = null;
		String created_date_after_str = null;
		Integer reminder_id_str = null;
		
		//optional fields
		try{ tags_str = new TagManager().getTagsCommaDelimited(tags); }catch(Exception e){}
		try{ due_date_before_str = this.date_util.JodaDateTimeToSQLDateTime(due_date_before); }catch(Exception e){}
		try{ due_date_str = this.date_util.JodaDateTimeToSQLDateTime(due_date); }catch(Exception e){}
		try{ due_date_after_str = this.date_util.JodaDateTimeToSQLDateTime(due_date_after); }catch(Exception e){}
		try{ created_date_before_str = this.date_util.JodaDateTimeToSQLDateTime(created_date_before); }catch(Exception e){}
		try{ created_date_str = this.date_util.JodaDateTimeToSQLDateTime(created_date); }catch(Exception e){}
		try{ created_date_after_str = this.date_util.JodaDateTimeToSQLDateTime(created_date_after); }catch(Exception e){}
		/*QueryResult results = this.filter_dao.addFilter(
				user.getUserId(),
				tags_str, 
				due_date_before_str,
				due_date_str,
				due_date_after_str,
				created_date_before_str,
				created_date_str,
				created_date_after_str,
				reminder_id_str, 
				complete_str,
				deleted_str
			);
			*/
		
	}

}
