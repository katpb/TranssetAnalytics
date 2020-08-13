package com.verifone.analytics.transset.client;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.gwt.charts.client.ChartLoader;

public class RunChart {
	private List<ChartObject> chartList = new ArrayList<ChartObject>();
	private static final RunChart INSTANCE = new RunChart();
	private boolean startNext;
	
	public static final RunChart getInstance () {
		return INSTANCE;
	}
	
	public void run() {
		if (!chartList.isEmpty()) {
		ChartObject obj = chartList.remove(0);
		ChartLoader chartLoader = new ChartLoader(obj.getCorechart());
		chartLoader.loadApi(obj.getRunnable());
		startNext = false;
		}
	}
	
	public List getChartList() {		
		return chartList;
	}



	public void setChartList(List chartList) {
		this.chartList = chartList;
	}
	
	
	// Notification - if done check remove the chart object from list
	// And start the next one.
	public void notifyDone() {
		startNext = true;
		run();
	}
}
