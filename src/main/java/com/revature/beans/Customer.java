package com.revature.beans;

import java.text.NumberFormat;
import java.util.ArrayList;

import com.revature.util.LogThis;

public class Customer extends User {

	private ArrayList<BankAccount> accts;
	private String firstName;
	private String lastName;
	
	public Customer(String username, String password, String firstName, String lastName, int id) {
		super(username, password, id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.accts = new ArrayList<BankAccount>();
	}
	
	public void applyForAccount(BankAccount newAcct) {
		if (newAcct == null)
			System.err.println("Cannot apply for null account.");
		this.accts.add(newAcct);
	}

	public ArrayList<BankAccount> getAccts() {
		return accts;
	}
	
	public void removeAcct(BankAccount acct) {
		this.accts.remove(acct);
	}

	public void setAccts(ArrayList<BankAccount> accts) {
		this.accts = accts;
	}
	
	public void printAccounts() {
		for (int i = 1; i <= accts.size(); i++) {
			BankAccount a = accts.get(i-1);
			System.out.println("\t[" + i + "] Bank Account #" + a.getId() + " with Balance " + a.getFormattedBalance());
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Customer [username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	

}
