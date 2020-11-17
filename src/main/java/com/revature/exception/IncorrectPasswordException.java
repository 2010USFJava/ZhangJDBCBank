package com.revature.exception;

public class IncorrectPasswordException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Incorrect Password.";
	}
}
