package com.capgemini.paytm.repo;






import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;
import com.capgemini.paytm.beans.Wallet;
import com.capgemini.paytm.exception.InvalidInputException;
import com.capgemini.paytm.util.DBUtil;

public class WalletRepoImpl implements WalletRepo{

	
public WalletRepoImpl() {
		
	}
	
	Customer cust=new Customer();
	
	
	@Override
	public boolean save(Customer customer) {
		
		try(Connection con=DBUtil.getConnection())
		{
			Statement stm=con.createStatement();
			PreparedStatement pstm=con.prepareStatement("Insert into Customer values(?,?,?)");
			
			pstm.setString(2, customer.getName());
		pstm.setString(1, customer.getMobileNo());
		float balance=customer.getWallet().getBalance().floatValue();
		
		pstm.setFloat(3, balance);
		pstm.execute();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
		return true;
	}

	@Override
	public Customer findOne(String mobileNo)  {	
		Customer customer = new Customer();
		try(Connection con=DBUtil.getConnection())
		{
		PreparedStatement preparedStatement=con.prepareStatement("select *from Customer where mobileNo=?");
		
		preparedStatement.setString(1,mobileNo );
		
		ResultSet res=preparedStatement.executeQuery();
		
		if(res.next()==false)
		{
			customer=null;
			
		}
		else
		{
		customer.setName(res.getString(2));
		customer.setMobileNo(res.getString(1));
		BigDecimal balance=new BigDecimal(res.getFloat(3));
		Wallet wallet=new Wallet();
		wallet.setBalance(balance);
		customer.setWallet(wallet);
		return customer;
		}
		}
		 catch (Exception e) {
			
				// TODO Auto-generated catch block
				System.out.println("Something went wrong "+e);
			}
			
		return customer; 
	}

	@Override
	public Customer Update(String mobileNo,Customer custm)  {	
		
		try(Connection con=DBUtil.getConnection())
		{
		PreparedStatement preparedStatement=con.prepareStatement("update Customer set wallet=? where mobileNo=?");
		
		preparedStatement.setString(2,mobileNo );
		
		float bal=custm.getWallet().getBalance().floatValue();
		
		preparedStatement.setFloat(1, bal);
		
		ResultSet res=preparedStatement.executeQuery();
		
		
		}
		 catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
			 System.out.println("Something went wrong "+e);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Something went wrong "+e);

			}
			
		return custm; 
	}

	@Override
	public boolean saveTransaction(Transaction transaction) {
		try(Connection con=DBUtil.getConnection())
		{
			Statement stm=con.createStatement();
			PreparedStatement pstm=con.prepareStatement("Insert into Transaction values(?,?,?,?,?)");
			
			pstm.setString(1, transaction.getMobileNo());
		pstm.setString(2, transaction.getTransaction_type());

		pstm.setString(3, transaction.getTransactionDate().toString());
	pstm.setFloat(4, transaction.getTransaction_amount());
	pstm.setString(5, transaction.getTransaction_status());
		
		
		pstm.execute();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
		return true;
	}

	@Override
	public List<Transaction>  findTransaction(String mobileNo) {
		List<Transaction> list=new ArrayList<Transaction>();
		try(Connection con=DBUtil.getConnection())
		{
		PreparedStatement preparedStatement=con.prepareStatement("select *from Transaction where mobileNo=?");
		
		preparedStatement.setString(1,mobileNo );
		
		
		ResultSet res=preparedStatement.executeQuery();
		
		if(res.next()==false)
		{ list=null;
			throw new InvalidInputException("No account found with mobile no "+mobileNo);
			
		}
		else {
			list.add(new Transaction(res.getString(1),res.getString(3),res.getString(2),res.getFloat(4),res.getString(5)));
			while(res.next())
			{
			
		list.add(new Transaction(res.getString(1),res.getString(3),res.getString(2),res.getFloat(4),res.getString(5)));
			}
		}
		}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Something went wrong "+e);
			}
			
		return list; 
	}
}
