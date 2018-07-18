package com.capgemini.paytm.beans;

import java.util.Date;

public class Transaction {
	
	@Override
	public String toString() {
		return "Transaction [mobileNo=" + mobileNo + ", transactionDate=" + transactionDate + ", transaction_type="
				+ transaction_type + ", transaction_amount=" + transaction_amount + ", transaction_status="
				+ transaction_status + "]";
	}
	private String mobileNo;
	private String transactionDate;
	private String transaction_type;
	private Float transaction_amount;
	private String transaction_status;
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	
	public Float getTransaction_amount() {
		return transaction_amount;
	}
	public void setTransaction_amount(Float transaction_amount) {
		this.transaction_amount = transaction_amount;
	}
	public String getTransaction_status() {
		return transaction_status;
	}
	public void setTransaction_status(String transaction_status) {
		this.transaction_status = transaction_status;
	}
	
	
	public Transaction(String mobileNo, String transactionDate, String transaction_type, Float transaction_amount,
			String transaction_status) {
		super();
		this.mobileNo = mobileNo;
		this.transactionDate = transactionDate;
		this.transaction_type = transaction_type;
		this.transaction_amount = transaction_amount;
		this.transaction_status = transaction_status;
	}
	public Transaction() {
		// TODO Auto-generated constructor stub
	}
	
	

}
