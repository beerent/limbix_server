package com.remindme.user;

import com.remindme.database.QueryResult;

public class UserManager{
	private UserDAO user_dao;
	

	public UserManager(){
		this.user_dao = new UserDAO();
	}
	
	//user_id, username, email, first_name, last_name, pw_hash
	public User authenticateUser(String username, String password){
		QueryResult result = user_dao.selectUser(username, password);
		
		if(!result.containsData())
			return null;

		int user_id = Integer.parseInt(result.getElement(0, result.getColumnIndex("user_id")));
		username = result.getElement(0, result.getColumnIndex("username"));
		String email = result.getElement(0, result.getColumnIndex("email"));
		password = result.getElement(0, result.getColumnIndex("pw_hash"));
		String first = result.getElement(0, result.getColumnIndex("first_name"));
		String last = result.getElement(0, result.getColumnIndex("last_name"));
		
		User user = new User(user_id, username, password, email, first, last);
		return user;
	}
}
