package com.verifone.analytics.transset.client;

import com.googlecode.gwt.charts.client.ChartPackage;

public class ChartObject {
	private ChartPackage corechart;
	private Runnable runnable;
	
	public ChartPackage getCorechart() {
		return corechart;
	}
	public void setCorechart(ChartPackage corechart) {
		this.corechart = corechart;
	}
	public Runnable getRunnable() {
		return runnable;
	}
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	} 
	

}
