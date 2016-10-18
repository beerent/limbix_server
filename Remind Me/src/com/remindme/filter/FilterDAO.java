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
	
	
	public int addFilter(Integer user_id, String filter_name, String filter_tag_list, String due_date_before, String due_date,
			String due_date_after, String created_date_before, String created_date, String created_date_after,
			Integer reminder_id, String complete, String deleted) {

		String sql = "insert into filters (user_id, filter_name";
		
		if(filter_tag_list != null)
			sql += ", tag_id_list";
		
		if(due_date_before != null)
			sql += ", due_before";
		
		if(due_date != null)
			sql += ", due";
		
		if(due_date_after != null)
			sql += ", due_after";
		
		if(created_date_before != null)
			sql += ", created_before";
		
		if(created_date != null)
			sql += ", created";
		
		if(created_date_after != null)
			sql += ", created_after";
		
		if(reminder_id != null)
			sql += ", reminder_id";
		
		if(complete != null)
			sql += ", completed";
		
		if(deleted != null)
			sql += ", deleted";
		
		sql += ") ";
		sql += "values (?, ?";
		
		if(filter_tag_list != null)
			sql += ", ?";
		if(due_date_before != null)
			sql += ", ?";
		if(due_date != null)
			sql += ", ?";
		if(due_date_after != null)
			sql += ", ?";
		if(created_date_before != null)
			sql += ", ?";
		if(created_date != null)
			sql += ", ?";
		if(created_date_after != null)
			sql += ", ?";
		if(reminder_id != null)
			sql += ", ?";
		if(complete != null)
			sql += ", ?";
		if(deleted != null)
			sql += ", ?";
		sql += ")";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			statement.setString(2, filter_name);
			int i = 3;
			if(filter_tag_list != null){
				statement.setString(i, filter_tag_list);
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

			int filter_id = executeInsert(connection, statement);
			return filter_id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int updateCustomFilter(Integer user_id, String filter_tag_list, String due_date_before, 
			String due_date, String due_date_after, String created_date_before, String created_date, 
			String created_date_after, String complete, String deleted) {
		String sql = "update filters set user_id = ? , tag_id_list = ? , "
				+ "due_before = ? , due = ? , due_after = ? , "
				+ "created_before = ? , created = ? , created_after = ? , "
				+ "completed = ? , deleted = ? "
				+ "where user_id = ? and filter_name = ?";

		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			int i = 2;
			
			if(filter_tag_list != null) statement.setString(i, filter_tag_list);
			else statement.setString(i, null);
			i++;
			
			if(due_date_before != null) statement.setString(i, due_date_before);
			else statement.setString(i, null);
			i++;
			
			if(due_date != null) statement.setString(i, due_date);
			else statement.setString(i, null);
			i++;
			
			if(due_date_after != null) statement.setString(i, due_date_after);
			else statement.setString(i, null);
			i++;
			
			if(created_date_before != null) statement.setString(i, created_date_before);
			else statement.setString(i, null);
			i++;
			
			if(created_date != null) statement.setString(i, created_date);
			else statement.setString(i, null);
			i++;
			
			if(created_date_after != null) statement.setString(i, created_date_after);
			else statement.setString(i, null);
			i++;
			
			if(complete != null) statement.setString(i, complete);	
			else statement.setString(i, null);
			i++;		
			
			if(deleted != null) statement.setString(i, deleted);
			else statement.setString(i, null);
			i++;
			
			statement.setInt(i++, user_id);
			statement.setString(i, "[custom]");
			
			executeUpdate(connection, statement);
			
			sql = "select filter_id from filters where user_id = ? and filter_name = ?";
			connection = getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setString(2, "[custom]");
			QueryResult result = executeQuery(connection, statement);
			if(result.containsData())
				return Integer.parseInt(result.getElement(0, 0));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}

	public void setCurrentFilter(int user_id, int filter_id) {
		String sql = "update filters set current = '0' where user_id = ? and current = '1'";
		Connection connection = super.getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			executeUpdate(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		
		sql = "update filters set current = '1' where user_id = ? and filter_id = ?";
		connection = super.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setInt(2, filter_id);
			System.out.println(statement);
			executeUpdate(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public QueryResult getFiltersMeta(int user_id) {
		String sql = "select filter_name, filter_id from filters where user_id = ? and filter_deleted = '0' order by filter_name asc";
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			return executeQuery(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public QueryResult getFilter(int user_id, Integer filter_id) {
		String sql = "select "
						+ "filter_name, "
						+ "filter_id, "
						+ "tag_id_list, "
						+ "created_before, "
						+ "created, "
						+ "created_after, "
						+ "due_before, "
						+ "due, "
						+ "due_after, "
						+ "completed, "
						+ "deleted "
					+ "from filters where user_id = ? and filter_id = ? and filter_deleted = '0'";
		
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setInt(2, filter_id);
			
			System.out.println(statement);
			return executeQuery(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void deleteFilter(int user_id, Integer filter_id) {
		String sql = "update filters set filter_deleted = '1' where user_id = ? and filter_id = ?";
		Connection connection = super.getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			statement.setInt(2, filter_id);
			System.out.println(statement);
			executeUpdate(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public QueryResult getCustomFilterId(int user_id) {
		String sql = "select filter_id from filters where user_id = ? and filter_name = '[custom]'";
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			return executeQuery(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
