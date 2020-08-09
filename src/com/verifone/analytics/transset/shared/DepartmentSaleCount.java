package com.verifone.analytics.transset.shared;

public class DepartmentSaleCount extends TransactionData {
	
	private MerchandiseInfo dept = null;
	private int count = 0;

	public DepartmentSaleCount(String date, String deptNum, String deptDesc, int count) {
		super(date);
		this.dept = new MerchandiseInfo(deptNum, deptDesc);
		this.count = count;
	}

	/**
	 * @return the DeptNum
	 */
	public String getDeptNum() {
		return dept.getCode();
	}

	/**
	 * @return the DeptDesc
	 */
	public String getDeptDesc() {
		return dept.getDescription();
	}
	

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

}
