package com.revature.exception;

public class UsernameNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Username not found.";
	}
}
