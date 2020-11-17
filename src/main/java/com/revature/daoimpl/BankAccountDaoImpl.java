package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.beans.BankAccount;
import com.revature.beans.Customer;
import com.revature.dao.BankAccountDao;
import com.revature.exception.AccountDeletionException;
import com.revature.exception.InvalidInputException;
import com.revature.exception.OverdraftException;
import com.revature.util.ConnFactory;
import com.revature.util.LogThis;

public class BankAccountDaoImpl implements BankAccountDao {
	
	public static ConnFactory cf = ConnFactory.getInstance();
	@Override
	public void createAccount(Customer c, double balance) {
		BankAccount a = null;
		try {
			Connection conn = cf.getConnection();
			String sql = "insert into account (c_id, balance) values (?,?)";
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, c.getId());
			ps.setDouble(2, balance);
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next())
				a = new BankAccount(c, balance, keys.getInt(1));
			c.applyForAccount(a);
			LogThis.LogIt("info", "Added a new account: " + a);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void deleteAccount(Customer c, BankAccount a) throws AccountDeletionException {
		if (!a.isEmpty())
			throw new AccountDeletionException();
		try {
			Connection conn = cf.getConnection();
			String sql = "delete from account where account_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getId());
			ps.executeUpdate();
			c.removeAcct(a);
			LogThis.LogIt("info", "Deleted account " + a);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void withdraw(BankAccount a, double amount) throws OverdraftException, InvalidInputException {
		a.withdraw(amount);
		setBalance(a, a.getBalance());
	}
	
	@Override
	public void deposit(BankAccount a, double amount) throws InvalidInputException {
		a.deposit(amount);
		setBalance(a, a.getBalance());
	}
	
	@Override
	public void setBalance(BankAccount a, double amount) {
		try {
			Connection conn = cf.getConnection();
			String sql = "update account set balance=? where account_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, amount);
			ps.setInt(2, a.getId());
			ps.executeUpdate();
			LogThis.LogIt("info", "Account " + a + " updating balance to " + a.getFormattedBalance());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	
}
