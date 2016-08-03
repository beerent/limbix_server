package com.remindme.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;

public class UserDAO extends DAO{

	public QueryResult getUser(String username, String password) {
		String sql = "select user_id, username, email, first_name, last_name, pw_hash from users where username = ? and pw_hash = ?";
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}

	public QueryResult getUser(int user_id) {
		String sql = "select user_id, username, email, first_name, last_name, pw_hash from users where user_id = ?";
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}

}
