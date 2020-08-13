package com.verifone.analytics.transset.shared;

import java.io.Serializable;

public class MerchandiseInfo implements Serializable {
	private String code;
	private String description;

	public MerchandiseInfo() {
		super();
	}
	
	public MerchandiseInfo(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
