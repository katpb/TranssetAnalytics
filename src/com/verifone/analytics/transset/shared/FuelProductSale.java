package com.verifone.analytics.transset.shared;

public class FuelProductSale extends TransactionData {

	public FuelProductSale(String date, String description, Double volume, Double amount) {
		super(date);
		this.description = description;
		this.volume = volume;
		this.amount = amount;
	}
	
	private String description = null;
	private Double volume = null;
	private Double amount =  null;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the volume
	 */
	public Double getVolume() {
		return volume;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
			
	
}
