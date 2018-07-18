package com.capgemini.paytm.junittest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InsufficientBalanceException;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.repo.WalletRepoImpl;
import com.capgemini.paytm.service.WalletService;
import com.capgemini.paytm.service.WalletServiceImpl;
import com.capgemini.paytm.util.DBUtil;

import static org.junit.Assert.*;

public class WalletTest {

	WalletService service;
	WalletRepoImpl repo;
	Customer cust1, cust2, cust3, cust4;

	@Before
	public void initData() {

		service = new WalletServiceImpl();
		cust1 = service.createAccount("Purva", "9900112212", new BigDecimal(9000));
		cust2 = service.createAccount("Abhilasha", "9963242422", new BigDecimal(6000000));
		cust3 = service.createAccount("Sejal", "9922950519", new BigDecimal(7000));
		cust4 = service.createAccount("Abha", "9922950518", new BigDecimal(7000));

	}

	// Test Create Account
	@Test(expected = NullPointerException.class)
	public void testCreateAccount() {

		service.createAccount(null, null, null);

	}

	@Test
	public void testCreateAccount1() {

		Customer c = new Customer();
		Customer cust = new Customer();
		cust = service.createAccount("Purva", "9900112213", new BigDecimal(7000));
		c.setMobileNo("9900112213");
		c.setName("Purva");
		c.setWallet(new Wallet(new BigDecimal(7000)));
		Customer actual = c;
		Customer expected = cust;
		assertEquals(expected, actual);
	}

	@Test
	public void testCreateAccount2() {

		Customer cust = new Customer();
		cust = service.createAccount("Purva", "9900112214", new BigDecimal(7000));
		assertEquals("Purva", cust.getName());

	}

	// Test Show Balance

	@Test(expected = InvalidInputException.class)
	public void testShowBalance() {
		service.showBalance("9579405722");

	}

	@Test
	public void testShowBalance2() {

		Customer cust = new Customer();

		cust = service.showBalance("9922950519");
		assertEquals(cust, cust3);

	}

	@Test(expected = InvalidInputException.class)
	public void testShowBalance4() {

		service.showBalance("992251");
	}

	
	// Test Fund Transfer
	@Test(expected = NullPointerException.class)
	public void testFundTransfer() {
		service.fundTransfer(null, null, new BigDecimal(7000));
	}

	@Test(expected = NullPointerException.class)
	public void testFundTransfer2() {
		service.fundTransfer(null, "95679405744", new BigDecimal(7000));
	}

	@Test(expected = Exception.class)
	public void testFundTransfer4() {
		service.fundTransfer("99001", "99632", new BigDecimal(2000));

	}

	@Test(expected = InvalidInputException.class)
	public void testFundTransfer5() {
		service.fundTransfer("9579405744", "9579405744", new BigDecimal(2000));

	}

	@Test(expected = InsufficientBalanceException.class)
	public void testFundTransfer6() {
		service.fundTransfer("9900112212", "9963242422", new BigDecimal(10000));

	}

	@Test
	public void testFundTransfer7() {
		Customer cust=service.showBalance("9963242422");
		Customer c=service.fundTransfer("9900112212", "9963242422", new BigDecimal(100));
		BigDecimal actual=c.getWallet().getBalance();
		BigDecimal expected=cust.getWallet().getBalance().add(new BigDecimal(100));
		
		assertEquals(expected, actual);
	}
	@Test(expected = InvalidInputException.class)
	public void testFundTransfer8() {
		service.fundTransfer("9900112212", "95679405744", new BigDecimal(-7000));
	}

	
	
	//Test Deposit
	@Test(expected = Exception.class)
	public void testDeposit() {
		
		service.depositAmount("900000000", new BigDecimal(2000));
	}
	
	@Test(expected =InvalidInputException.class)
	public void testDeposit2() {
		service.depositAmount(null, new BigDecimal(2000));
	}
	

	@Test(expected = Exception.class)
	public void testDeposit4() {
		cust1 = service.depositAmount("996324", new BigDecimal(2000));

	}
	

	public void testDeposit5() {
		Customer cus=service.showBalance("9922950518");
		Customer c=service.depositAmount("9922950518", new BigDecimal(100));
		BigDecimal actual=c.getWallet().getBalance();
		BigDecimal expected=cus.getWallet().getBalance().add(new BigDecimal(100));
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = InvalidInputException.class)
	public void testDeposit6() {
		cust1 = service.depositAmount("9922950518", new BigDecimal(-2000));

	}
	

	//Test Withdraw
	
	@Test(expected = Exception.class)
	public void testWithdraw3() {
		service.withdrawAmount("99632", new BigDecimal(2000));

	}

	@Test(expected = InvalidInputException.class)
	public void testWithdraw4() {
		service.withdrawAmount("9900112212", new BigDecimal(-7000));
	}
	
	
	//Test Validation
	@Test
	public void TestValidate() {
		Customer c = new Customer();
		boolean actual = service.validate("Vaishnavi", "9123456789", c);
		assertEquals(true, actual);
	}

	@AfterClass
	public static void testAfter() {
		
		Connection con;
		try {
			con = DBUtil.getConnection();
			Statement stm=con.createStatement();
			PreparedStatement pstm=con.prepareStatement("delete from customer");
			pstm.executeQuery();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@After
	public  void After() {
		service=null;
	}
	}
	

