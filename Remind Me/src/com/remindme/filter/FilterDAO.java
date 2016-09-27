package com.remindme.filter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;
import com.remindme.reminder.ReminderManager;

public class FilterDAO extends DAO{
	
	
	public QueryResult getReminders(Integer user_id, String filter_tag_set_id, String due_date_before, String due_date,
			String due_date_after, String created_date_before, String created_date, String created_date_after,
			Integer reminder_id, String complete, String deleted) {
		System.out.println(deleted);
		
		ReminderManager reminder_manager = new ReminderManager();
		ArrayList<String> tags_as_arraylist = null;

		String sql = "insert into filters (user_id";
		
		if(filter_tag_set_id != null)
			sql += ", filter_tag_set_id";
		
		if(due_date_before != null)
			sql += ", due_date_before";
		
		if(due_date != null)
			sql += ", due_date";
		
		if(due_date_after != null)
			sql += ", due_date_after";
		
		if(created_date_before != null)
			sql += ", created_date_before";
		
		if(created_date != null)
			sql += ", created_date";
		
		if(created_date_after != null)
			sql += ", created_date_after";
		
		if(reminder_id != null)
			sql += ", reminder_id";
		
		if(complete != null)
			sql += ", complete";
		
		/* SKIP DELETED FOR NOW */
		sql += ") ";
		sql += "values (" + user_id;
		
		return null;
			
		
		
		
		
		
		
		
	/*	
		
		
		
		//		String sql = "select distinct reminders.reminder_id, user_id, reminder, created, due_date, complete, deleted ";
		sql += "from reminders ";
		if(tags != null){
			sql += "join reminder_tag_map on reminders.reminder_id = reminder_tag_map.reminder_id ";
			sql += "join tags on tags.tag_id = reminder_tag_map.tag_id ";
		}
		sql += "where user_id = ? ";
		if(tags != null){
			tags_as_arraylist = reminder_manager.getStringsFromCommaDeliminatedString(tags);
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

			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
		*/
	}
}
