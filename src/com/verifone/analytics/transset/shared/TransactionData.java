package com.verifone.analytics.transset.shared;

public class TransactionData {
	
	private String date;
	private String siteId;
	private String transactionId;

	public TransactionData(String date) {
		this.date = date;
	}
	
	public TransactionData(String date, String siteId, String transactionId) {
		this.date = date;
		this.siteId = siteId;
		this.transactionId = transactionId;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
