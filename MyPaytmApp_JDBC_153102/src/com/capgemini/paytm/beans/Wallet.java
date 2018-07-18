package com.capgemini.paytm.beans;

import java.math.BigDecimal;

public class Wallet {

	private BigDecimal balance;

	public Wallet(BigDecimal balance) {
		super();
		this.balance = balance;
	}

	public Wallet() {
		super();
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return  balance + "\n";
	}
	
}
