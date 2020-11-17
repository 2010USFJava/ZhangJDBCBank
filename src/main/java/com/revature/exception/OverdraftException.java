package com.revature.exception;

public class OverdraftException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Insufficient balance.";
	}
}
