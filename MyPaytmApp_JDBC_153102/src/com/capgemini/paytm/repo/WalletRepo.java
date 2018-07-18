package com.capgemini.paytm.repo;

import java.util.List;

import com.capgemini.paytm.beans.Customer;
import com.capgemini.paytm.beans.Transaction;

public interface WalletRepo {

	public boolean save(Customer customer);	
	public Customer findOne(String mobileNo);
	public Customer Update(String mobileNo,Customer custm) ;
	public boolean saveTransaction(Transaction transaction);	
	public List<Transaction> findTransaction(String mobileNo);
}
