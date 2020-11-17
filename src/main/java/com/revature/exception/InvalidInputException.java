package com.revature.exception;

public class InvalidInputException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Invalid input";
	}
}
