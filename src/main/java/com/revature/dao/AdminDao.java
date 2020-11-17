package com.revature.dao;

import com.revature.beans.Admin;

public interface AdminDao {

	Admin retrieveAdminByUsername(String username);
	boolean checkAdminExists(String username);
	void viewCustomers(Admin a);
	int getNumCustomers();
}
