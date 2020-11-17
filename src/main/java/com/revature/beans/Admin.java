package com.revature.beans;

import java.util.ArrayList;

public class Admin extends User{
	
	public Admin(String username, String password, int id) {
		super(username, password, id);
	}

	@Override
	public String toString() {
		return "Admin [username=" + username + ", password=" + password + ", id=" + id + "]";
	}
	
	public void printCustomers(ArrayList<Customer> cstmrs) {
		for (int i = 1; i <= cstmrs.size(); i++) {
			Customer c = cstmrs.get(i-1);
			System.out.println("\t[" + i + "] Customer #" + c.getId() + " " + c.getFirstName() 
					+ " " + c.getLastName() + " has username="+ c.getUsername() + ", "
					+ "password=" + c.getPassword());
		}
	}
	
}
