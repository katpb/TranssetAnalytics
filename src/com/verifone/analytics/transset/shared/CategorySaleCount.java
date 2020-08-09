package com.verifone.analytics.transset.shared;

public class CategorySaleCount extends TransactionData {
	
	private MerchandiseInfo cat = null;
	private int count = 0;

	public CategorySaleCount(String date, String catNum, String catDesc, int count) {
		super(date);
		this.cat = new MerchandiseInfo(catNum, catDesc);
		this.count = count;
	}

	/**
	 * @return the CategoryNum
	 */
	public String getCategoryNum() {
		return cat.getCode();
	}

	/**
	 * @return the CategoryDesc
	 */
	public String getCategoryDesc() {
		return cat.getDescription();
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

}
