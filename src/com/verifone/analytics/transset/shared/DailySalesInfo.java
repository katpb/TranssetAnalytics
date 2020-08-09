package com.verifone.analytics.transset.shared;

public class DailySalesInfo extends TransactionData {
	
	private int count = 0;
	private double avgAmount = 0;
	private String type = null;
	
	public DailySalesInfo(String date, int count) {
		super(date);
		this.count = count;
	}
	
	public DailySalesInfo(String date, double avgAmount) {
		super(date);
		this.avgAmount = avgAmount;
	}
	
	public DailySalesInfo(String date, String type, int count) {
		super(date);
		this.count = count;
		this.type = type;
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the avgAmount
	 */
	public double getAvgAmount() {
		return avgAmount;
	}

	/**
	 * @return the mopName
	 */
	public String getType() {
		return type;
	}

}
