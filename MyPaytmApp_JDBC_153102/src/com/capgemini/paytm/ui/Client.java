package com.capgemini.paytm.ui;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.service.WalletService;
import com.capgemini.paytm.service.WalletServiceImpl;

public class Client {

	private WalletService walletService;
	public Client()
	{
		walletService=new WalletServiceImpl();
	}
	public void menu() {
		String ans="";	
		Scanner sc=new Scanner(System.in);
		System.out.println("****Paytm Application****");
		Customer customer=new Customer();
		do
		{
			System.out.println("1. Create Account ");
			System.out.println("2. Show Balance");
			System.out.println("3. Fund Transfer");
			System.out.println("4. Deposit amount");
			System.out.println("5. Withdraw amount");
			System.out.println("6. Print Transaction");	
			System.out.println("7. Exit");
			System.out.println("\nPlease Select an option:");
			int choice=sc.nextInt();
			
			switch (choice) {
			case 1:
				System.out.println("Enter Name: ");
				String name=sc.next();
				System.out.println("Enter Mobile Number: ");
				String mobileNo=sc.next();
				System.out.println("Enter Balance");
				BigDecimal amount=sc.nextBigDecimal();				
				try{customer=walletService.createAccount(name, mobileNo, amount);
				if(customer!=null)
				{
				System.out.println("Your account is created!");
				System.out.println(customer);
				}
				else
					System.out.println("Account already exists");
				}
				catch(Exception e)
				{
					System.out.println("Something wrong "+e);
				}
				break;				
			case 2:				
				System.out.println("Enter the mobile number to view balance: ");
				String mobileNo2=sc.next();
				try
				{
					
				customer=walletService.showBalance(mobileNo2);
				if(customer!=null)
				System.out.println("Your balance for mobile number: "+mobileNo2+" is "+customer.getWallet());
				}
				catch(Exception e)
				{
					System.out.println("Something wrong "+e);
				}
				break;
			case 3:
				System.out.println("Enter Source Mobile Number: ");
				String sourceMobileNo=sc.next();
				System.out.println("Enter Target Mobile Number: ");
				String targetMobileNo=sc.next();
				try
				{
				System.out.println("Enter amount to transfer");
				BigDecimal amount1=sc.nextBigDecimal();				
				customer=walletService.fundTransfer(sourceMobileNo, targetMobileNo, amount1);
				if(customer!=null)
				System.out.println(customer);
				}
				catch(Exception e)
				{
					System.out.println("Something wrong "+e);
				}
				break;
			case 4:
				try{System.out.println("Enter Mobile Number: ");
				String mobileNo3=sc.next();
				System.out.println("Enter amount to deposit");
				BigDecimal amount3=sc.nextBigDecimal();				
				customer=walletService.depositAmount(mobileNo3, amount3);
				if(customer!=null)
				System.out.println(customer);
				}
				catch(Exception e)
				{
					System.out.println("Something wrong "+e);
				}
				break;
			case 5:
				System.out.println("Enter Mobile Number: ");
				String mobileNo4=sc.next();
				try {
				System.out.println("Enter amount to withdraw");
				BigDecimal amount4=sc.nextBigDecimal();				
				customer=walletService.withdrawAmount(mobileNo4, amount4);
				if(customer!=null)
				System.out.println(customer);
				}
				catch(Exception e)
				{
					System.out.println("Something wrong "+e);
				}
				break;
				
			case 6:
				System.out.println("Enter Mobile Number: ");
				String mobileNo5=sc.next();
				
				try
				{
					
					List<Transaction> tl=walletService.printTransaction(mobileNo5);
					
					Iterator<Transaction> it=tl.iterator();
					if(tl!=null)
						System.out.println("Mobile no \t\tTransaction Type\t\t Transaction Date\t\tTransaction Amount\t\tTransaction Status");
					while(it.hasNext())
					{
						Transaction t=it.next();
						System.out.println(t.getMobileNo()+"\t\t"+t.getTransaction_type()+"\t\t\t"+t.getTransactionDate()+"\t\t\t"+t.getTransaction_amount()+"\t\t\t"+t.getTransaction_status());
					}
					}
					
					
				catch(Exception e)
				{
					System.out.println("Something wrong "+e);
				}
				break;
			case 7:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid options");
				break;
			}			
			System.out.println("\nDo you want to continue: Y/N ");
			ans=sc.next();
		}while(ans.equalsIgnoreCase("y"));
		System.out.println("\nThank you for using this service!");	
	}
	public static void main(String[] args) {
		Client client=new Client();
		client.menu();

	}

}
