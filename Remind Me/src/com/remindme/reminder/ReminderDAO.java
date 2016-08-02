package com.remindme.reminder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;
import com.remindme.user.User;
import com.remindme.util.DateUtil;

public class ReminderDAO extends DAO{
	
	public ReminderDAO(){}
	
	public int addReminder(Integer user_id, String reminder, String current_time) {
		String sql = "insert into reminders (user_id, reminder, created) values (?, ?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			statement.setString(2, reminder);
			statement.setString(3, current_time);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int addReminder(Integer user_id, String reminder, String current_time, String due_date) {
		String sql = "insert into reminders (user_id, reminder, created, due_date) values (?, ?, ?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			statement.setString(2, reminder);
			statement.setString(3, current_time);
			statement.setString(4, due_date);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public QueryResult getReminder(Integer reminder_id) {
		String sql = "select reminder_id, user_id, reminder, created, due_date, complete, deleted from reminders where reminder_id = ?";
		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql);
			statement.setInt(1, reminder_id);
			return executeQuery(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}
	
	public QueryResult getTag(String tag) {
		String sql = "select tag_id, tag from tags where tag = ?";
		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql);
			statement.setString(1, tag);
			return executeQuery(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}

	public int addTag(String tag_str) {
		String sql = "insert into tags (tag) values (?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, tag_str);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int mapTag(Integer tag_id, Reminder reminder) {
		String sql = "insert into reminder_tag_map (reminder_id, tag_id) values (?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, reminder.getReminderId());
			statement.setInt(2, tag_id);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;		
	}

	public QueryResult getReminders(Integer user_id, String tags, String due_date_before, String due_date,
			String due_date_after, String created_date_before, String created_date, String created_date_after,
			Integer reminder_id, String complete) {
		
		ReminderManager reminder_manager = new ReminderManager();
		ArrayList<String> tags_as_arraylist = null;

		String sql = "select distinct reminders.reminder_id, user_id, reminder, created, due_date, complete, deleted ";
		sql += "from reminders ";
		sql += "join reminder_tag_map on reminders.reminder_id = reminder_tag_map.reminder_id ";
		sql += "join tags on tags.tag_id = reminder_tag_map.tag_id ";
		sql += "where user_id = ? ";
		if(tags != null){
			tags_as_arraylist = reminder_manager.getTagsFromRequest(tags);
			sql += "and tags.tag in (?";
			for(int i = 1; i < tags_as_arraylist.size(); i++)
				sql += ", ?";
			sql += ") ";
		}
		if(due_date_before != null)
			sql += "and due_date < ? ";
		if(due_date != null)
			sql += "and due_date = ? ";
		if(due_date_after != null)
			sql += "and due_date > ? ";
		if(created_date_before != null)
			sql += "and created < ? ";
		if(created_date != null)
			sql += "and created = ? ";
		if(created_date_after != null)
			sql += "and created > ? ";
		if(reminder_id != null)
			sql += "and reminders.reminder_id = ? ";
		if(complete != null)
			sql += "and complete = ?";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			int i = 2;
			if(tags != null){
				for(int j = 0; j < tags_as_arraylist.size(); j++){
					statement.setString(i, tags_as_arraylist.get(j));
					i++;
				}
			}
			
			if(due_date_before != null){
				statement.setString(i, due_date_before);
				i++;
			}
			
			if(due_date != null){
				statement.setString(i, due_date);;
				i++;
			}
			
			if(due_date_after != null){
				statement.setString(i, due_date_after);
				i++;
			}
			
			if(created_date_before != null){
				statement.setString(i, created_date_before);
				i++;
			}
			
			if(created_date != null){
				statement.setString(i, created_date);
				i++;
			}
			
			if(created_date_after != null){
				statement.setString(i, created_date_after);
				i++;
			}
			
			if(reminder_id != null){
				statement.setInt(i, reminder_id);
				i++;
			}

			if(complete != null){
				statement.setString(i, complete);	
			}
			
			System.out.println(statement);
			return executeQuery(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
