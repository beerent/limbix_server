package com.remindme.reminder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.joda.time.DateTime;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;
import com.remindme.user.User;

public class ReminderDAO extends DAO{
	
	public ReminderDAO(){}
	
	public int insertReminder(User user, String reminder) {

		String sql = "insert into reminders (user_id, reminder) values (?, ?)";

		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user.getUserId());
			statement.setString(2, reminder);
			return executeInsert(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
