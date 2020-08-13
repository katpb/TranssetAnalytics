package com.verifone.analytics.transset.shared;

public class CashierTrackingData extends TransactionData {
	
	private String cashierName;
	private Double avgCustomerWaitTime;
	
	public CashierTrackingData() {
		super();
	}

	public CashierTrackingData(String date, String cashierName, Double avgCustomerWaitTime) {
		super(date);
		this.cashierName = cashierName;
		this.avgCustomerWaitTime = avgCustomerWaitTime;
	}

	/**
	 * @return the cashierName
	 */
	public String getCashierName() {
		return cashierName;
	}

	/**
	 * @return the avgCustomerWaitTime
	 */
	public Double getAvgCustomerWaitTime() {
		return avgCustomerWaitTime;
	}

}
