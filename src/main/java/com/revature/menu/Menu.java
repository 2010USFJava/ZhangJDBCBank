package com.revature.menu;

import java.util.ArrayList;
import java.util.Scanner;

import com.revature.beans.*;
import com.revature.dao.AdminDao;
import com.revature.dao.BankAccountDao;
import com.revature.dao.CustomerDao;
import com.revature.daoimpl.AdminDaoImpl;
import com.revature.daoimpl.BankAccountDaoImpl;
import com.revature.daoimpl.CustomerDaoImpl;
import com.revature.exception.AccountDeletionException;
import com.revature.exception.IncorrectPasswordException;
import com.revature.exception.InvalidInputException;
import com.revature.exception.OverdraftException;
import com.revature.exception.UsernameException;
import com.revature.exception.UsernameNotFoundException;
import com.revature.util.LogThis;

public class Menu {
	static Scanner scan = new Scanner(System.in);
	static User user = null;
	
	public static void startMenu() {
		user = null;
		System.out.println("~Welcome to Blueberry Bank~");
		System.out.println("Please enter a choice:");
		System.out.println("\t[L]ogin");
		System.out.println("\t[S]etup a new account");
		System.out.println("\t[A]dmin Options");
		System.out.println("\t[Q]uit");
		
		String choice=scan.nextLine();
		
		switch(choice.toLowerCase()) {
			case "l":
				try {
					loginMenu();
				} catch (IncorrectPasswordException e) {
					System.out.println(e.getMessage());
					tryLoginAgainMenu();
				} catch (UsernameNotFoundException e) {
					System.out.println(e.getMessage());
					tryLoginAgainMenu();
				}
				break;
			case "s":
				try {
					newCustomerMenu();
				} catch (UsernameException e) {
					System.out.println(e.getMessage());
					tryLoginAgainMenu();
				}
				break;
			case "a":
			try {
				adminLoginMenu();
			} catch (UsernameNotFoundException | IncorrectPasswordException e) {
				System.out.println(e.getMessage());
				tryLoginAgainMenu();
			}
				break;
			case "q":
				LogThis.LogIt("info", "User " + user + " has left the bank.");
				System.out.println("Thank you for visiting Blueberry Bank. Have a berry good day!");
				break;
			default:
				System.out.println("Please try again");
				startMenu();
				break;			
		}
		return;
	}
	
	public static void tryLoginAgainMenu() {
		System.out.println("\t[M]ain menu");
		System.out.println("\t[Q]uit");
		String choice2=scan.nextLine();
		switch(choice2.toLowerCase()) {
			case "m":
				startMenu();
				break;
			case "q":
				LogThis.LogIt("info", user + " has left the bank.");
				System.out.println("Thank you for visiting Blueberry Bank. Have a berry good day!");
				break;
			default:
				System.out.println("Please try again.");
				tryLoginAgainMenu();
				break;	
		}		
		return;
	}
	
	public static void loginMenu() throws IncorrectPasswordException, UsernameNotFoundException {
		System.out.println("~Login~");
		System.out.println("Username:");
		String username=scan.nextLine();
		CustomerDao cd = new CustomerDaoImpl();
		if (!cd.checkExistingUsername(username))
			throw new UsernameNotFoundException();
		user = cd.retrieveCustomerByUsername(username);
		
		System.out.println("Password:");
		String password=scan.nextLine();
		if (!password.equals(user.getPassword()))
			throw new IncorrectPasswordException();
		System.out.println("Login successful.");
		customerMenu((Customer) user);
		return;
	}
	
	public static void customerMenu(Customer c) {
		System.out.println("What would you like to do next, " + c.getFirstName() + "?");
		CustomerDao cd = new CustomerDaoImpl();
		ArrayList<BankAccount> accts = cd.getCustomerAccounts(c);

		if (c.getAccts().isEmpty()) {
			System.out.println("\t[A]pply for a new account");
			System.out.println("\t[M]ain menu");
		}
		else {
			System.out.println("\t[S]elect an account");
			System.out.println("\t[A]pply for a new account");
			System.out.println("\t[D]elete an account");
			System.out.println("\t[M]ain menu");
		}
	
		String choice=scan.nextLine();
		switch(choice.toLowerCase()) {
		case "s": 
			selectAccountMenu(c);
			break;
		case "a":
			newAccountMenu(c);
			break;
		case "d":
			deleteAccountMenu(c);
			break;
		case "m":
			startMenu();
			break;
		default:
			System.out.println("Please try again");
			customerMenu(c);
			break;	
		}		
		return;	
		

	}
	
	private static void newAccountMenu(Customer c) {
		BankAccountDao bd = new BankAccountDaoImpl();
		System.out.println("Initial Deposit:");	
		double deposit = 0.0;
		try {
			deposit = Double.parseDouble(scan.nextLine());	
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			customerMenu(c);
		}
		if (deposit < 0) {
			System.out.println("Invalid input.");
			customerMenu(c);
			return;
		}
		bd.createAccount(c, deposit);
		System.out.println("Account creation successful. Returning to menu");
		customerMenu(c);
		return;	
	}

	public static void deleteAccountMenu(Customer c) {
		BankAccountDao bd = new BankAccountDaoImpl();
		System.out.println("Here are your accounts:");
		c.printAccounts();
		System.out.println("Which one would you like to delete?");
		int choice = 0;
		try {
			choice=Integer.parseInt(scan.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			selectAccountMenu(c);
		}
		if (choice > c.getAccts().size() || choice < 1) {
			System.out.println("Invalid input.");
			System.out.println("\t[T]ry again");
			System.out.println("\t[M]ain menu");
			String choice2 =scan.nextLine();
			switch(choice2.toLowerCase()) {	
				case "t": 
					selectAccountMenu(c);
					break;
				case "m":
					customerMenu(c);
					break;
				default:
					System.out.println("Please try again.");
					selectAccountMenu(c);
					break;	
			}
		}
		else {
			BankAccount a = c.getAccts().get(choice-1);
			try {
				bd.deleteAccount(c, a);
			} catch (AccountDeletionException e) {
				System.out.println(e.getMessage());
				customerMenu(c);
				return;
			}
		}
		System.out.println("Account deletion successful.");
		customerMenu(c);
		return;
	}
	
	
	public static void selectAccountMenu(Customer c) {
		System.out.println("Here are your accounts:");
		c.printAccounts();
		System.out.println("Which one would you like to use?");
	//	System.out.println("\t[1] for the first account, [2] for the second..");
		int choice = 0;
		try {
			choice=Integer.parseInt(scan.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			selectAccountMenu(c);
		}
		if (choice > c.getAccts().size() || choice < 1) {
			System.out.println("Invalid input.");
			System.out.println("\t[T]ry again");
			System.out.println("\t[M]ain menu");
			String choice2 =scan.nextLine();
			switch(choice2.toLowerCase()) {	
				case "t": 
					selectAccountMenu(c);
					break;
				case "m":
					customerMenu(c);
					break;
				default:
					System.out.println("Please try again.");
					selectAccountMenu(c);
					break;	
			}
		}
		else {
			BankAccount a = c.getAccts().get(choice-1);
			accountMenu(c, a);
		}
		return;
		
	}
	
	public static void accountMenu(Customer c, BankAccount a) {
		System.out.println("For the " + a.getBasicInfo() + ", please select from the following options:");
		System.out.println("\t[W]ithdraw");
		System.out.println("\t[D]eposit");
		System.out.println("\t[M]enu");
		String choice=scan.nextLine();
		switch(choice.toLowerCase()) {
		case "w": 
			withdrawMenu(c, a);
			break;
		case "d":
			depositMenu(c, a);
			break;
		case "m":
			customerMenu(c);
			break;
		default:
			System.out.println("Please try again, (" + choice +") is not valid input.");
			customerMenu(c);
			break;	
		}		
		return;
	}
	


	public static void depositMenu(Customer c, BankAccount a) {
		BankAccountDao bd = new BankAccountDaoImpl();
		System.out.println("How much would you like to deposit?");
		double choice = 0.0;
		try {
			choice = Double.parseDouble(scan.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			accountMenu(c, a);
		}
		try {
			bd.deposit(a, choice);
			System.out.println("Your new balance is " +  a.getFormattedBalance());
			customerMenu(c);
			
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());                               
			accountMenu(c, a);
		}
		return;
	}
	
	public static void withdrawMenu(Customer c, BankAccount a) {
		BankAccountDao bd = new BankAccountDaoImpl();
		System.out.println("How much would you like to withdraw?");
		double choice = 0.0;
		try {
			choice = Double.parseDouble(scan.nextLine());   
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			accountMenu(c, a);
		}
		try {
			bd.withdraw(a, choice);
			System.out.println("Your new balance is " + a.getFormattedBalance());
			customerMenu(c);
			
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());                               
			accountMenu(c, a);
		} catch (OverdraftException e) {
			System.out.println(e.getMessage());                               
			accountMenu(c, a);
		}   
 		return;
	}
	
	public static void newCustomerMenu() throws UsernameException {
		CustomerDao cd = new CustomerDaoImpl();
		System.out.println("~New Customer Menu~");
		System.out.println("First Name:");
		String firstName = scan.nextLine();
		System.out.println("Last Name:");
		String lastName = scan.nextLine();
		System.out.println("Please enter a username:");
		String username = scan.nextLine();
		System.out.println("Please enter a password:");
		String password = scan.nextLine();
		Customer c = cd.createCustomer(firstName, lastName, username, password);
		user = c;
		System.out.println("Account setup successful. Logging in...");
		customerMenu(c);
		return;
		
	}
	
	public static void adminLoginMenu() throws UsernameNotFoundException, IncorrectPasswordException {
		System.out.println("~Admin Menu~");
		System.out.println("Username:");
		String username=scan.nextLine();
		AdminDao ad = new AdminDaoImpl();
		if (!ad.checkAdminExists(username))
			throw new UsernameNotFoundException();
		user = ad.retrieveAdminByUsername(username);
		System.out.println("Password:");
		String password=scan.nextLine();
		if (!password.equals(user.getPassword()))
			throw new IncorrectPasswordException();
		System.out.println("Admin login successful.");
		adminMenu();
		
	}
	
	public static void adminMenu() {
		AdminDao ad = new AdminDaoImpl();
		
		System.out.println("Admin options:");
		System.out.println("\t[V]iew customers");
		System.out.println("\t[C]reate a new customer account");
		System.out.println("\t[U]pdate a customer account");
		System.out.println("\t[D]elete a customer account");
		System.out.println("\t[M]ain menu");
		String choice=scan.nextLine();
		
		switch(choice.toLowerCase()) {
			case "v":
				System.out.println("~Bank Customers~");
				ad.viewCustomers((Admin)user);
				adminMenu();
				break;
			case "c":
				try {
					newAdminCustomerMenu();
				} catch (UsernameException e) {
					System.out.println(e.getMessage());
					adminMenu();
				}
				break;
			case "u":
				selectCustomersMenu();
				break;
			case "d":
				//deleteCustomersMenu();
				break;
			case "m":
				startMenu();
				break;
			default:
				System.out.println("Please try again");
				startMenu();
				break;			
		}
	}
	
	public static void newAdminCustomerMenu() throws UsernameException {
		CustomerDao cd = new CustomerDaoImpl();
		System.out.println("~Admin's New Customer Menu~");
		System.out.println("First Name:");
		String firstName = scan.nextLine();
		System.out.println("Last Name:");
		String lastName = scan.nextLine();
		System.out.println("Please enter a username:");
		String username = scan.nextLine();
		System.out.println("Please enter a password:");
		String password = scan.nextLine();
		Customer c = cd.createCustomer(firstName, lastName, username, password);
		System.out.println("Customer successfully created.");
		adminMenu();
		return;
		
	}
	
	public static void selectCustomersMenu() {
		AdminDao ad = new AdminDaoImpl();
		System.out.println("Here are the customers:");
		ad.viewCustomers((Admin)user);
		System.out.println("Which one would you like to update?");
		int choice = 0;
		try {
			choice=Integer.parseInt(scan.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input.");
			selectCustomersMenu();
		}
		if (choice > ad.getNumCustomers() || choice < 1) {
			System.out.println("Invalid input.");
			System.out.println("\t[T]ry again");
			System.out.println("\t[M]ain menu");
			String choice2 =scan.nextLine();
			switch(choice2.toLowerCase()) {	
				case "t": 
					selectCustomersMenu();
					break;
				case "m":
					adminMenu();
					break;
				default:
					System.out.println("Please try again.");
					adminMenu();
					break;	
			}
		}
		else {
			//TODO: Update customers menu
		}
		return;
		
	}
}
