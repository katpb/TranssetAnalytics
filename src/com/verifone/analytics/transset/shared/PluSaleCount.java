package com.verifone.analytics.transset.shared;

public class PluSaleCount extends TransactionData {
	
	private MerchandiseInfo plu = null;
	private double count = 0;
	
	public PluSaleCount() {
		super();
	}

	public PluSaleCount(String date, String upcNum, String pluDesc, double count) {
		super(date);
		this.plu = new MerchandiseInfo(upcNum, pluDesc);
		this.count = count;
	}

	/**
	 * @return the UPCNum
	 */
	public String getUPCNum() {
		return plu.getCode();
	}

	/**
	 * @return the UPCDesc
	 */
	public String getUPCDesc() {
		return plu.getDescription();
	}
	

	/**
	 * @return the count
	 */
	public double getCount() {
		return count;
	}

}
