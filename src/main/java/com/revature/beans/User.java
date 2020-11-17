package com.revature.beans;

import java.io.Serializable;

public class User {

	protected String username;
	protected String password;
	protected int id;
	
	public User(String username, String password, int id) {
		this.username = username;
		this.password = password;
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", id=" + id + "]";
	}

	
	
	
	
}
