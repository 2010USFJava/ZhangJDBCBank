package com.revature.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.BankAccount;
import com.revature.beans.Customer;
import com.revature.exception.UsernameException;


public interface CustomerDao {
	public Customer retrieveById(int id);
	public ArrayList<BankAccount> getCustomerAccounts(Customer c);
	Customer retrieveCustomerByUsername(String username);
	public boolean checkUniqueUsername(String username);
	public boolean checkExistingUsername(String username);
	Customer createCustomer(String firstName, String lastName, String username, String password) throws UsernameException; 
}
