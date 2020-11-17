package com.revature.dao;

import java.util.ArrayList;

import com.revature.beans.Admin;
import com.revature.beans.Customer;

public interface AdminDao {

	Admin retrieveAdminByUsername(String username);
	boolean checkAdminExists(String username);
	void viewCustomers(Admin a);
	int getNumCustomers();
	ArrayList<Customer> getAllCustomers();
}
