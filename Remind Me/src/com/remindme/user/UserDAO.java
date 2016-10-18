package com.remindme.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

	public QueryResult getUser(String username) {
		String sql = "select user_id, username, email, first_name, last_name, pw_hash from users where username = ?";
		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			return executeQuery(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new QueryResult(null, null);
	}

	public int registerUser(String username, String email, String first, String last, String password) {
		String sql = "insert into users (username, email, first_name, last_name, pw_hash) values (?, ?, ?, ?, ?)";

		try {
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, username);
			statement.setString(2, email);
			statement.setString(3, first);
			statement.setString(4, last);
			statement.setString(5, password);
			return executeInsert(connection, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean updateUser(int user_id, String new_username, String new_email, String new_first, String new_last,
			String new_password) {
		String sql = "update users set user_id = ? ";
		if(new_username != null) sql += ", username = ? ";
		if(new_email != null) sql += ", email = ? ";
		if(new_first != null) sql += ", first_name = ? ";
		if(new_last != null) sql += ", last_name = ? ";
		if(new_password != null)  sql += ", pw_hash = ? ";
		sql += "where user_id = ?";
		
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user_id);
			int i = 2;
			
			if(new_username != null){
				statement.setString(i, new_username);
				i++;
			}
			
			if(new_email != null){
				statement.setString(i, new_email);
				i++;
			}
			
			if(new_first != null){
				statement.setString(i, new_first);
				i++;
			}
			
			if(new_last != null){
				statement.setString(i, new_last);
				i++;
			}
			
			if(new_password != null){
				statement.setString(i, new_password);
				i++;
			}
			statement.setInt(i, user_id);
			System.out.println(statement);
			return executeUpdate(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateGCMToken(int user_id, String token) {
		String sql = "insert into gcm_tokens (user_id, gcm_token) values (?, ?) on duplicate key update gcm_token = values (gcm_token)";
		try{
			Connection connection = super.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, user_id);
			statement.setString(2, token);
			executeInsert(connection, statement);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
