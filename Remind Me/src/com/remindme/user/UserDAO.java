package com.remindme.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.remindme.database.DAO;
import com.remindme.database.QueryResult;

public class UserDAO extends DAO{

	public QueryResult selectUser(String username, String password) {
		String sql = "select user_id, username, email, first_name, last_name, pw_hash from users where username = ? and pw_hash = ?";
		try {
			PreparedStatement statement = super.getConnection().prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);
			return executeQuery(statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}

}
