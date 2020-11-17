package com.revature.exception;

public class UsernameException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Username already exists.";
	}
}
