package com.capgemini.paytm.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InsufficientBalanceException;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.repo.WalletRepo;
import com.capgemini.paytm.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {


public WalletRepo repo;
WalletRepoImpl obj=new WalletRepoImpl();
	public WalletServiceImpl(){
		repo= new WalletRepoImpl();
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}
	
	
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		
		Customer cust=new Customer();
		 validate( name, mobileNo,cust);
		 validate(amount);	//amount should be positive
		 cust.setWallet(new Wallet(amount));
		boolean result=repo.save(cust);
		if(result==true)
			return cust;
		else
			return null;
				//create object of customer, and call dao save layer
		}

	public Customer showBalance(String mobileNo) {
		
		Customer customer=repo.findOne(mobileNo);		
		if(customer!=null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {	
		
		 validate(amount);
		Customer scust=new Customer();
		Customer tcust=new Customer();
		Transaction strans=new Transaction();
		Transaction ttrans=new Transaction();
		
		Wallet sw=new Wallet();
		Wallet tw=new Wallet();
		scust=repo.findOne(sourceMobileNo);
		tcust=repo.findOne(targetMobileNo);
		strans.setMobileNo(sourceMobileNo);
		ttrans.setMobileNo(targetMobileNo);
		strans.setTransaction_amount(amount.floatValue());
		ttrans.setTransaction_amount(amount.floatValue());
		strans.setTransactionDate(new Date().toString());
		ttrans.setTransactionDate(new Date().toString());
		strans.setTransaction_type("Fund Transfer");
		ttrans.setTransaction_type("Fund Transfer");
		
		if(sourceMobileNo.equals(targetMobileNo))
		{
			throw new InvalidInputException("Source and target mobile numbers cannot be same");
		}
		else
		if(scust!=null && tcust!=null )
		{	
			if(scust.getWallet().getBalance().compareTo(amount)>=0)
			{
			BigDecimal amtSub=scust.getWallet().getBalance();
			BigDecimal diff=amtSub.subtract(amount);
			sw.setBalance(diff);
			scust.setWallet(sw);
			
			BigDecimal amtAdd=tcust.getWallet().getBalance();
			BigDecimal sum=amtAdd.add(amount);			
			tw.setBalance(sum);
			tcust.setWallet(tw);
			
		
			
			strans.setTransaction_status("successfull");
			ttrans.setTransaction_status("successfull");
			repo.saveTransaction(strans);
			repo.saveTransaction(ttrans);
			repo.Update(targetMobileNo, tcust);
			repo.Update(sourceMobileNo, scust);
			//obj.getData().put(targetMobileNo, tcust);
			//obj.getData().put(sourceMobileNo, scust);
			}
			else
				{
				
				strans.setTransaction_status("failed");
				ttrans.setTransaction_status("failed");
				repo.saveTransaction(strans);
				repo.saveTransaction(ttrans);
				throw new InsufficientBalanceException("Amount is more than available balance");
				
				}
		}
		else
		{
			throw new InvalidInputException("Account does not exists");
		}
		
		return tcust;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		validate(amount);
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		Transaction strans=new Transaction();
		
		cust=repo.findOne(mobileNo);
		
		
		strans.setMobileNo(mobileNo);
		strans.setTransaction_amount(amount.floatValue());
		strans.setTransactionDate(new Date().toString());
		strans.setTransaction_type("Deposit");
		
		
		
		if(cust!=null)
		{
			strans.setTransaction_amount(amount.floatValue());
			
			
			BigDecimal amtAdd=cust.getWallet().getBalance().add(amount);
			wallet.setBalance(amtAdd);
			cust.setWallet(wallet);
			obj.Update(mobileNo, cust);
			
			strans.setTransaction_status("successfull");
			//obj.getData().put(mobileNo, cust);
			 repo.saveTransaction(strans);
		}
		else
		
			
			throw new InvalidInputException("Account does not exists");
		
		
		return cust;
		
	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		
		validate(amount);
		Customer cust=new Customer();
		Wallet wallet=new Wallet();
		
		Transaction strans=new Transaction();
		
		strans.setMobileNo(mobileNo);
		strans.setTransaction_amount(amount.floatValue());
		strans.setTransactionDate(new Date().toString());
		strans.setTransaction_type("Withdraw");
		
		cust=repo.findOne(mobileNo);
		if(cust!=null)
		{
			if(cust.getWallet().getBalance().compareTo(amount)>=0)
			{
			BigDecimal amtSub=cust.getWallet().getBalance().subtract(amount);
			wallet.setBalance(amtSub);
			cust.setWallet(wallet);
			obj.Update(mobileNo, cust);
			strans.setTransaction_status("successfull");
			repo.saveTransaction(strans);
			//obj.getData().put(mobileNo, cust);
			}
			else
				{
				strans.setTransaction_status("failed");
				repo.saveTransaction(strans);
				throw new InsufficientBalanceException("Sorry cannot withdraw,amount to be withdrawn is more than available balance");
				}
		}
		else
			throw new InvalidInputException("Account does not exists");
		
		
		return cust;
	}
	
	
	public List<Transaction> printTransaction(String mobileNo)
	{
		
		
		return repo.findTransaction(mobileNo);
		
	}
	
	public boolean validate(String name,String phoneno,Customer cust)  {
		Scanner sc=new Scanner(System.in);
		while(true)
		{Pattern pa=Pattern.compile("[a-zA-Z]+\\.?");
		Matcher ma=pa.matcher(name);
		if(ma.matches())
		{
			break;
		}
		else
		{
			System.err.println("Enter valid name: ");
			name=sc.next();
		}
			
		}
		
		 //check if phone no is valid
		while(true)
		{Pattern p=Pattern.compile("(0/91)?[7-9][0-9]{9}");
		Matcher m=p.matcher(phoneno);
		if(m.matches())
		{
			break;
		}
		else
		{
			System.err.println("Enter valid 10 digit phone no: ");
			phoneno=sc.next();
		}
			
		}
		cust.setMobileNo(phoneno);
		cust.setName(name);
		return true;
	}
	public boolean validate(BigDecimal amount)
	{
			if(Math.abs(amount.floatValue())==amount.floatValue())
			{
				
			}
			else
			{
				throw new InvalidInputException("Enter positive amount");
				
			}
				
		return true;
		
	}
	

}