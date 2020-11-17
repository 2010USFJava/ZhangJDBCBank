package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.beans.BankAccount;
import com.revature.beans.Customer;
import com.revature.exception.AccountDeletionException;
import com.revature.exception.InvalidInputException;
import com.revature.exception.OverdraftException;


public interface BankAccountDao {

	public void createAccount(Customer c, double balance);
	public void deleteAccount(Customer c, BankAccount a) throws AccountDeletionException;
	public void setBalance(BankAccount a, double amount);
	public void withdraw(BankAccount a, double amount) throws OverdraftException, InvalidInputException;
	public void deposit(BankAccount a, double amount) throws InvalidInputException;
}
