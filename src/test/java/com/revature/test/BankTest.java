package com.revature.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.beans.*;
import com.revature.dao.BankAccountDao;
import com.revature.dao.CustomerDao;
import com.revature.daoimpl.BankAccountDaoImpl;
import com.revature.daoimpl.CustomerDaoImpl;
import com.revature.exception.AccountDeletionException;
import com.revature.exception.InvalidInputException;
import com.revature.exception.OverdraftException;
import com.revature.exception.UsernameException;
import com.revature.util.LogThis;


public class BankTest {
	
	@BeforeClass
	public static void initialSetUp() {
		System.out.println("I run once before everything");
	}
	
	@Test
	public void test() {
		assertTrue(true);
	}
	
	@Test
	public void logTest() {
		LogThis.LogIt("info", "logger works!");
	}

	
	@Test
	public void retrieveCustomerByIdTest() {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		assertNotNull(c);
	}
	
	@Test
	public void getCustomerAccountsTest() {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		assertNotNull(cd.getCustomerAccounts(c));
	}
	
	
	@Test
	public void retrieveCustomerByUsernameTest() {
		String name = "blue";
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveCustomerByUsername(name);
		assertNotNull(c);
	}
	
	@Test
	public void withdrawTest() throws OverdraftException, InvalidInputException {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		ArrayList<BankAccount> accts = cd.getCustomerAccounts(c);
		BankAccount a = accts.get(0);
		double previousBalance = a.getBalance();
		a.withdraw(5.0);
		assertEquals(previousBalance-5, a.getBalance(), .001);
	}
	
	@Test(expected = OverdraftException.class)
	public void overdraftTest() throws OverdraftException, InvalidInputException {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		ArrayList<BankAccount> accts = cd.getCustomerAccounts(c);
		BankAccount a = accts.get(0);
		a.withdraw(10.0);
	}
	
	@Test
	public void depositTest() throws InvalidInputException {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		BankAccountDao bad = new BankAccountDaoImpl();
		ArrayList<BankAccount> accts = cd.getCustomerAccounts(c);
		BankAccount a = accts.get(0);
		double previousBalance = a.getBalance();
		a.deposit(10.0);
		assertEquals(previousBalance+10, a.getBalance(), .001);
	}
	
	@Test(expected = AccountDeletionException.class)
	public void deletionExceptionTest() throws AccountDeletionException {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		BankAccountDao bad = new BankAccountDaoImpl();
		bad.createAccount(c, 555);
		ArrayList<BankAccount> accts = cd.getCustomerAccounts(c);
		BankAccount newAcct = accts.get(accts.size()-1);
		bad.deleteAccount(c, newAcct);
	}
	
	@Test
	public void accountCreationandDeletionTest() throws AccountDeletionException, OverdraftException, InvalidInputException {
		CustomerDao cd = new CustomerDaoImpl();
		Customer c = cd.retrieveById(1);
		BankAccountDao good = new BankAccountDaoImpl();
		good.createAccount(c, 66.60);
		ArrayList<BankAccount> accts = cd.getCustomerAccounts(c);
		BankAccount newAcct = accts.get(accts.size()-1);
		newAcct.setBalance(0.0);
		good.setBalance(newAcct, 0.0);
		good.deleteAccount(c, newAcct);
	}
	
	@Test
	public void uniqueUsernameTest() {
		CustomerDao cd = new CustomerDaoImpl();
		assertTrue(cd.checkUniqueUsername("blue"));
	}
	
	@Test
	public void existingUsernameTest() {
		CustomerDao cd = new CustomerDaoImpl();
		assertTrue(cd.checkExistingUsername("blue"));
		assertFalse(cd.checkExistingUsername("crayon"));
	}

	@Test
	public void customerCreationTest() {
		CustomerDao cd = new CustomerDaoImpl();
		try {
			cd.createCustomer("harvey", "birdman", "attorney", "atlaw");
		} catch (UsernameException e) {
			System.out.println(e.getMessage());
		}
	}

}
