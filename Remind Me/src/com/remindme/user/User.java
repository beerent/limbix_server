package com.remindme.user;

public class User {
	private String username;
	private int user_id;
	private String password;
	private String email;
	private String first_name;
	private String last_name;
	
	public User(int user_id, String username, String password, String email, String first, String last){
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.first_name = first;
		this.last_name = last;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUserId() {
		return user_id;
	}
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	
	public String toString(){
		return "" + getUserId() + " | " 
				+ getUsername() + " | " 
				+ getEmail() 
				+ " | " 
				+ getFirstName() + " " 
				+ getLastName();
	}
	
}
