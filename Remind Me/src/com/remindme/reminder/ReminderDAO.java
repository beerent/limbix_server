package com.remindme.reminder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;
import com.remindme.user.User;

public class ReminderDAO extends DAO{
	
	public ReminderDAO(){}
	
	public int addReminder(User user, String reminder, String current_time) {
		String sql = "insert into reminders (user_id, reminder, created) values (?, ?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user.getUserId());
			statement.setString(2, reminder);
			statement.setString(3, current_time);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int addReminder(User user, String reminder, String current_time, String due_date) {
		String sql = "insert into reminders (user_id, reminder, created, due_date) values (?, ?, ?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user.getUserId());
			statement.setString(2, reminder);
			statement.setString(3, current_time);
			statement.setString(4, due_date);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public QueryResult getReminder(int reminder_id) {
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

	public int mapTag(Tag tag, Reminder reminder) {
		String sql = "insert into reminder_tag_map (reminder_id, tag_id) values (?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, reminder.getReminderId());
			statement.setInt(2, tag.getTagId());
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;		
	}
}
