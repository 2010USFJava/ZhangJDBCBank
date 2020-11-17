package com.revature.exception;

public class AccountDeletionException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Cannot delete account with a remaining balance.";
	}
}
