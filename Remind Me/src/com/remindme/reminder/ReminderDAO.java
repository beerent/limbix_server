package com.remindme.reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;
import com.remindme.user.User;

public class ReminderDAO extends DAO{
	
	public ReminderDAO(){}
	
	public int addReminder(Integer user_id, String reminder, String current_time) {
		String sql = "insert into reminders (user_id, reminder, created) values (?, ?, ?)";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			statement.setString(2, reminder);
			statement.setString(3, current_time);
			return executeInsert(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int addReminder(Integer user_id, String reminder, String current_time, String due_date) {
		String sql = "insert into reminders (user_id, reminder, created, due_date) values (?, ?, ?, ?)";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			statement.setString(2, reminder);
			statement.setString(3, current_time);
			statement.setString(4, due_date);
			return executeInsert(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public QueryResult getReminder(Integer reminder_id) {
		String sql = "select reminder_id, user_id, reminder, created, due_date, complete, deleted from reminders where reminder_id = ?";
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, reminder_id);
			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}
	
	public QueryResult getTag(String tag) {
		String sql = "select tag_id, tag from tags where tag = ?";
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, tag);
			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}

	public int addTag(String tag_str) {
		String sql = "insert into tags (tag) values (?)";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, tag_str);
			return executeInsert(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int mapTag(Integer tag_id, Integer reminder_id) {
		String sql = "insert into reminder_tag_map (reminder_id, tag_id) values (?, ?)";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, reminder_id);
			statement.setInt(2, tag_id);
			return executeInsert(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;		
	}

	public QueryResult getReminders(Integer user_id, String tags, String due_date_before, String due_date,
			String due_date_after, String created_date_before, String created_date, String created_date_after,
			Integer reminder_id, String complete, String deleted) {
		
		TagManager tag_manager = new TagManager();
		ArrayList<String> tags_as_arraylist = null;

		String sql = "select distinct reminders.reminder_id, user_id, reminder, created, due_date, complete, deleted ";
		sql += "from reminders ";
		if(tags != null){
			sql += "join reminder_tag_map on reminders.reminder_id = reminder_tag_map.reminder_id ";
			sql += "join tags on tags.tag_id = reminder_tag_map.tag_id ";
		}
		sql += "where user_id = ? ";
		if(tags != null){
			tags_as_arraylist = tag_manager.getStringsFromCommaDeliminatedString(tags);
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
			sql += "and complete = ? ";
		if(deleted != null)
			sql += "and deleted = ?";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
				statement.setString(i, due_date);
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
				i++;
			}
			
			if(deleted != null){
				statement.setString(i, deleted);
			}

			System.out.println("STATEMENT: " + statement);
			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public boolean updateReminder(int reminder_id, String reminder, String due_date, Boolean remove_due_date,
			String complete_str, String deleted_str) {
		String sql = "update reminders set reminder_id = ? ";
		if(reminder != null)
			sql += ", reminder = ? ";
		if(due_date != null || (remove_due_date != null && remove_due_date))
			sql += ", due_date = ? ";
		if(complete_str != null)
			sql += ", complete = ? ";
		if(deleted_str != null)
			sql += ", deleted = ? ";
		sql += "where reminder_id = ?";
		
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, reminder_id);
			int i = 2;
			
			if(reminder != null){
				statement.setString(i, reminder);
				i++;
			}
			
			if(due_date != null){
				statement.setString(i, due_date);
				i++;
			}else if(remove_due_date != null && remove_due_date){
				statement.setNull(i, java.sql.Types.DATE);
				i++;
			}
			
			if(complete_str != null){
				statement.setString(i, complete_str);
				i++;
			}
			
			if(deleted_str != null){
				statement.setString(i, deleted_str);
				i++;
			}
			statement.setInt(i, reminder_id);
			return executeUpdate(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateTags(int reminder_id, ArrayList<String> tags) {
		String sql = "delete from reminder_tag_map where reminder_id = ?";
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, reminder_id);
			executeUpdate(connection, statement);
			
			QueryResult result = null;
			for(String tag : tags){
				result = getTag(tag);
				int tag_id;
				if(result.containsData())
					tag_id = Integer.parseInt(result.getElement(0, "tag_id"));
				else
					tag_id = addTag(tag);
				mapTag(tag_id, reminder_id);
			}
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	public QueryResult getTags(int user_id, String due_date_before, String due_date, String due_date_after,
			String created_date_before, String created_date, String created_date_after, String complete,
			String deleted) {
		
		String sql = "select distinct tags.tag_id, tags.tag from tags " +
						"join reminder_tag_map on tags.tag_id = reminder_tag_map.tag_id " +
						"join reminders on reminder_tag_map.reminder_id = reminders.reminder_id " +
						"where user_id = ? and deleted = ? ";
		
		if(complete != null)
			sql += "and complete = ? ";
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
		
		
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setString(2, deleted);
			
			int i = 3;

			if(complete != null){
				statement.setString(i, complete);
				i++;
			}
			
			if(due_date_before != null){
				statement.setString(i, due_date_before);
				i++;
			}
			
			if(due_date != null){
				statement.setString(i, due_date);
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
			
			if(created_date_after != null)
				statement.setString(i, created_date_after);
			
			return executeQuery(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public QueryResult limbExistsInFilter(Integer user_id, String created_before, String created, String created_after,
			String due_before, String due, String due_after, String tags, String completed, String deleted,
			int reminder_id) {
		return getReminders(user_id, tags, due_before, due,
				due_after, created_before, created, created_after,
				reminder_id, completed, deleted);
	}
	
}
