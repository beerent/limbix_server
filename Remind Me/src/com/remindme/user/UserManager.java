package com.remindme.user;

import com.remindme.database.QueryResult;

public class UserManager{
	private UserDAO user_dao;
	

	public UserManager(){
		this.user_dao = new UserDAO();
	}
	
	//user_id, username, email, first_name, last_name, pw_hash
	public User authenticateUser(String username, String password){
		QueryResult result = user_dao.getUser(username, password);	
		return getUser(result);
	}

	public User getUser(int user_id) {
		QueryResult result = user_dao.getUser(user_id);
		return getUser(result);
	}
	
	public User getUser(String username){
		QueryResult result = user_dao.getUser(username);
		return getUser(result);
	}
	
	private User getUser(QueryResult result){
		if(!result.containsData())
			return null;

		int user_id = Integer.parseInt(result.getElement(0, result.getColumnIndex("user_id")));
		String username = result.getElement(0, result.getColumnIndex("username"));
		String email = result.getElement(0, result.getColumnIndex("email"));
		String password = result.getElement(0, result.getColumnIndex("pw_hash"));
		String first = result.getElement(0, result.getColumnIndex("first_name"));
		String last = result.getElement(0, result.getColumnIndex("last_name"));
		
		User user = new User(user_id, username, password, email, first, last);
		return user;
	}

	public boolean usernameLongEnough(String username) {
		return username.length() >= 3;
	}
	
	public boolean usernameShortEnough(String username) {
		return username.length() <= 20;
	}
	
	public boolean usernameValidCharacters(String username) {
		char c;
		for(int i = 0; i < username.length(); i++){
			c = username.charAt(i);
			
			//numeric
			if(c >= 48 && c <= 57)
				continue;
			//alpha
			if(c >= 65 && c <= 90 || c >= 97 && c <= 122)
				continue;
			if(c == '_')
				continue;
			return false;
		}
		return true;
	}

	public boolean usernameExists(String username) {
		User user = getUser(username);
		return user != null;
	}

	public boolean passwordLongEnough(String password) {
		return password.length() >= 8;
	}

	public boolean passwordShortEnough(String password) {
		return password.length() <= 25;
	}

	public boolean passwordsMatch(String password, String password2) {
		return password.equals(password2);
	}

	public boolean containsAlpha(String str){
		char c;
		for(int i = 0; i < str.length(); i++){
			c = str.charAt(i);
			if(c >= 65 && c <= 90 || c >= 97 && c <= 122)
				return true;
		}
		return false;
	}
	
	public boolean containsNumeric(String str) {
		char c;
		for(int i = 0; i < str.length(); i++){
			c = str.charAt(i);
			if(c >= 48 && c <= 57)
				return true;
		}
		return false;
	}

	public boolean firstLastNameLongEnough(String name) {
		return name.length() > 0;
	}

	public boolean firstLastNameShortEnough(String name) {
		return name.length() <= 50;
	}

	public boolean validFirstOrLastName(String name) {
		char c;
		for(int i = 0; i < name.length(); i++){
			c = name.charAt(i);
			
			//numeric
			if(c >= 48 && c <= 57)
				continue;
			//alpha
			if(c >= 65 && c <= 90 || c >= 97 && c <= 122)
				continue;
			if(c == '_' || c == '-')
				continue;
			return false;
		}
		return true;
	}

	public boolean validEmail(String email) {
		if(!email.contains("@"))
			return false;
		if(!email.contains("."))
			return false;
		
		if(email.length() < 5 || email.length() >= 256)
			return false;
		return true;
	}

	public void registerUser(String username, String email, String first, String last, String password) {
		this.user_dao.registerUser(username, email, first, last, email);
		
	}
}
