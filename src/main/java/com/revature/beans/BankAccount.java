package com.revature.beans;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.revature.exception.InvalidInputException;
import com.revature.exception.OverdraftException;
import com.revature.util.LogThis;

/**
 * @author mzhan
 *
 */
public class BankAccount {
	
	private Customer accountHolder;
	private double balance;
	private int id;
	
	public BankAccount(Customer accountHolder, double balance, int id)  {
		this.createAccount(accountHolder, balance, id);
	}
	
	public boolean isEmpty() {
		return 0 == Double.compare(this.balance, 0.0);
	}

	/*
	 * Creates an account with account holder(s) and balance
	 */
	private void createAccount(Customer accountHolder, double balance, int id) {
		if (accountHolder == null)
			System.err.println("Account must an account holder.");
		this.balance = balance;
		this.accountHolder = accountHolder;
		this.id = id;
		//LogThis.LogIt("info", "Bank account " + this.toString() + " has been created!");
		
	}
	
	/*
	 * Withdraws amount from the account.
	 */
	public boolean withdraw(double amount) throws InvalidInputException, OverdraftException {
		if (amount <= 0) {
			throw new InvalidInputException();
		}
		if (amount > this.balance)  {
			throw new OverdraftException();
		}
		else
			this.balance -= amount;
		LogThis.LogIt("info", amount + " was withdrawn from " + this.toString());
		return true;
	}
	
	/*
	 * Deposits amount to the account. Returns true if the operation is successful.
	 */
	public boolean deposit(double amount) throws InvalidInputException {
		if (amount <= 0) {
			throw new InvalidInputException();
		}
		this.balance += amount;
		LogThis.LogIt("info", amount + " was deposited to " + this.toString());
		return true;
		
	}
	
	public Customer getAccountHolder() {
		return accountHolder;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBasicInfo() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String formattedBalance = formatter.format(this.balance);
		return "Bank Account #" + id + " with Balance " + getFormattedBalance();
	}
	
	public String getFormattedBalance() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(this.balance);
	}
	
	@Override
	public String toString() {
		return "BankAccount [accountHolder=" + accountHolder.getFirstName() + " " + accountHolder.getLastName()
					+ ", balance=" + balance + ", account_id=" + id + "]";
	}


}
