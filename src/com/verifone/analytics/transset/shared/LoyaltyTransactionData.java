package com.verifone.analytics.transset.shared;

public class LoyaltyTransactionData extends TransactionData {
	
	private String loyaltyProgram = null;
	private Double discAmount = null;

	public LoyaltyTransactionData(String date, String loyaltyProgram, Double discAmount) {
		super(date);
		this.loyaltyProgram = loyaltyProgram;
		this.discAmount = discAmount;
	}

	/**
	 * @return the loyaltyProgram
	 */
	public String getLoyaltyProgram() {
		return loyaltyProgram;
	}

	/**
	 * @return the discAmount
	 */
	public Double getDiscAmount() {
		return discAmount;
	}

}
