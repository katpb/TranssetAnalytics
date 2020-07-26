package com.verifone.analytics.transset.shared;

public class PluDescriptionDateCount {
	
	private String decription;
	private String date;
	private int count;
	
	/**
	 * @param decription
	 * @param count
	 */
	public PluDescriptionDateCount(String decription, String date, int count) {
		super();
		this.decription = decription;
		this.date = date;
		this.count = count;
	}
	
	/**
	 * @return the decription
	 */
	public String getDecription() {
		return decription;
	}
	/**
	 * @param decription the decription to set
	 */
	public void setDecription(String decription) {
		this.decription = decription;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
