package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.beans.Admin;
import com.revature.beans.BankAccount;
import com.revature.beans.Customer;
import com.revature.dao.AdminDao;
import com.revature.util.ConnFactory;
import com.revature.util.LogThis;

public class AdminDaoImpl implements AdminDao {

	public static ConnFactory cf = ConnFactory.getInstance();
	

	@Override
	public ArrayList<Customer> getAllCustomers() {
		ArrayList<Customer> cs = new ArrayList<Customer>();
		try {
			Connection conn = cf.getConnection();
			String sql = "select * from bank_customer";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Customer c = new Customer(rs.getString("username"), rs.getString("password"),
									rs.getString("first_name"),rs.getString("last_name"), 
									rs.getInt("c_id"));
				cs.add(c);
				
			}	
		
			LogThis.LogIt("info", "Retrieved all customers");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cs;
	}
	
	@Override
	public void viewCustomers(Admin a) {
		
		try {
			Connection conn = cf.getConnection();
			String sql = "select * from bank_customer";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Customer> customers = new ArrayList<Customer>();
			while (rs.next()) {
				Customer c = new Customer(rs.getString("username"), rs.getString("password"),
									rs.getString("first_name"),rs.getString("last_name"), 
									rs.getInt("c_id"));
				customers.add(c);
				
			}	
			a.printCustomers(customers);
			LogThis.LogIt("info", "Viewed all customers");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public Admin retrieveAdminByUsername(String username) {
		Admin ad = null;
		try {
			Connection conn = cf.getConnection();
			String sql = "select * from admin where username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				ad = new Admin(username, rs.getString("password"), rs.getInt("admin_id"));
				LogThis.LogIt("info", "Retrieved admin : " + ad);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ad;
	}
	
	/*
	 * Return true if the admin exists.
	 */
	@Override
	public boolean checkAdminExists(String username) {
		try {
			Connection conn = cf.getConnection();
			String sql = "select count(*) from admin where username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt("count") >= 1) 
					return true;
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public int getNumCustomers() {
		try {
			Connection conn = cf.getConnection();
			String sql = "select count_customers()";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt("count_customers");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	

}
