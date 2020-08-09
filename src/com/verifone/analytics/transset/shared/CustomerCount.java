package com.verifone.analytics.transset.shared;

public class CustomerCount extends TransactionData {
	
	private String customerCardNumber = null;
	private int count = 0;

	public CustomerCount(String date, String custCardCount, int count) {
		super(date);
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the customerCardNumber
	 */
	public String getCustomerCardNumber() {
		return customerCardNumber;
	}

}
