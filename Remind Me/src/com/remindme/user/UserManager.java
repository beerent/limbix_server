package com.remindme.user;

import com.remindme.database.QueryResult;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;

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

	public boolean  registerUser(String username, String email, String first, String last, String password) {
		return this.user_dao.registerUser(username, email, first, last, password) > 0;
	}

	public RequestResponse confirmUsername(String username) {
		ResponseManager response_manager = new ResponseManager();
		if(!usernameLongEnough(username))
			return response_manager.usernameTooShort();
		if(!usernameShortEnough(username))
			return response_manager.usernameTooLong();
		if(!usernameValidCharacters(username))
			return response_manager.invalidUsername();	
		if(!containsAlpha(username))
			return response_manager.usernameMissingAlphaCharacter();
		if(usernameExists(username))
			return response_manager.usernameAlreadyExists();
		return null;
	}

	public RequestResponse confirmPasswords(String password, String password2) {
		ResponseManager response_manager = new ResponseManager();
		if(!passwordsMatch(password, password2))
			return response_manager.passwordsDoNotMatch();
		if(!passwordLongEnough(password))
			return response_manager.passwordTooShort();
		if(!passwordShortEnough(password))
			return response_manager.passwordTooLong();
		if(!containsAlpha(password))
			return response_manager.passwordMissingAlphaCharacter();
		if(!containsNumeric(password))
			return response_manager.passwordMissingNumericCharacter();
		return null;
	}

	public RequestResponse verifyFirstAndLastName(String first_name, String last_name) {
		ResponseManager response_manager = new ResponseManager();
		if(!firstLastNameLongEnough(first_name))
			return response_manager.firstNameTooShort();
		if(!firstLastNameLongEnough(last_name))
			return response_manager.lastNameTooShort();
		if(!firstLastNameShortEnough(first_name))
			return response_manager.firstNameTooLong();
		if(!firstLastNameShortEnough(last_name))
			return response_manager.lastNameTooLong();
		if(!validFirstOrLastName(first_name))
			return response_manager.invalidFirstName();
		if(!validFirstOrLastName(last_name))
			return response_manager.invalidLastName();	
		return null;
	}
}
