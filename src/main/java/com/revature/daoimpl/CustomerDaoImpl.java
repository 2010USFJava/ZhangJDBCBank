package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.BankAccount;
import com.revature.beans.Customer;
import com.revature.dao.CustomerDao;
import com.revature.exception.AccountDeletionException;
import com.revature.exception.UsernameException;
import com.revature.util.ConnFactory;
import com.revature.util.LogThis;

public class CustomerDaoImpl implements CustomerDao {

	public static ConnFactory cf = ConnFactory.getInstance();
	
	@Override
	public ArrayList<BankAccount> getCustomerAccounts(Customer c) {
		try {
			Connection conn = cf.getConnection();
			String sql = "select * from account where c_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, c.getId());
			ResultSet rs = ps.executeQuery();
			ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
			while (rs.next()) {
				BankAccount a = new BankAccount(c, rs.getDouble("balance"), rs.getInt("account_id"));
				accounts.add(a);
				LogThis.LogIt("info", "Retrieved account : " + a);
//				System.out.println(rs.getString("account_id")+ "\t"
//	                    + rs.getString("c_id") + "\t"
//	                    + rs.getString("balance"));
			}
			c.setAccts(accounts);
			return accounts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Customer retrieveById(int id) {
		Customer c = null;
		try {
			Connection conn = cf.getConnection();
			String sql = "select * from bank_customer where c_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, 1);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				c = new Customer(rs.getString("username"), rs.getString("password"), 
						   rs.getString("first_name"),
						   rs.getString("last_name"), id);
				LogThis.LogIt("info", "Retrieved customer : " + c);
//				System.out.println(rs.getString("c_id")+ "\t"
//	                    + rs.getString("first_name") + "\t"
//	                    + rs.getString("last_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	@Override
	public Customer retrieveCustomerByUsername(String username) {
		Customer c = null;
		try {
			Connection conn = cf.getConnection();
			String sql = "select * from bank_customer where username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				c = new Customer(username, rs.getString("password"), 
										   rs.getString("first_name"),
										   rs.getString("last_name"),
										   rs.getInt("c_id"));
				LogThis.LogIt("info", "Retrieved customer : " + c);
//				System.out.println(rs.getString("c_id")+ "\t"
//	                    + rs.getString("first_name") + "\t"
//	                    + rs.getString("last_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}



	/*
	 * Return true if the username is unique.
	 */
	public boolean checkUniqueUsername(String username) {
		try {
			Connection conn = cf.getConnection();
			String sql = "select count(*) from bank_customer where username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				//System.out.println("COUNT IS " + rs.getInt("count"));
				if (rs.getInt("count") <= 1) 
					return true;
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Return true if the username exists.
	 */
	public boolean checkExistingUsername(String username) {
		try {
			Connection conn = cf.getConnection();
			String sql = "select count(*) from bank_customer where username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) 
				return (rs.getInt("count") >= 1);
		} catch (SQLException e)  {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Customer createCustomer(String firstName, String lastName, String username, String password) throws UsernameException {
		if (checkExistingUsername(username)) {
			throw new UsernameException();
		}
		Customer c = null;
		try {
			Connection conn = cf.getConnection();
			String sql = "insert into bank_customer (first_name, last_name, username, password) values (?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, username);
			ps.setString(4, password);

			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next())
				c = new Customer(username, password, firstName, lastName, keys.getInt(1));
			LogThis.LogIt("info", "Created " + c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return c;
	}
	
	@Override
	public void deleteCustomer(Customer c) {
		try {
			Connection conn = cf.getConnection();
			String sql = "delete from bank_customer where c_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, c.getId());
			ps.executeUpdate();
			LogThis.LogIt("info", "Deleted customer " + c);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateCustomerName(Customer c, String firstName, String lastName) {
		try {
			Connection conn = cf.getConnection();
			String sql = "update bank_customer set first_name=?, last_name=? where c_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,firstName);
			ps.setString(2,lastName);
			ps.setInt(3,c.getId());
			ps.executeUpdate();
			c.setFirstName(firstName);
			c.setLastName(lastName);
			LogThis.LogIt("info", "Customer " + c + " updating name to " + firstName + " " + lastName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void updateCustomerLogin(Customer c, String username, String password) {
		try {
			Connection conn = cf.getConnection();
			String sql = "update bank_customer set username=?, password=? where c_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,username);
			ps.setString(2,password);
			ps.setInt(3,c.getId());
			ps.executeUpdate();
			c.setUsername(username);
			c.setPassword(password);
			LogThis.LogIt("info", "Customer " + c + " updating login info to username=" + username + ", password=" + password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

}
