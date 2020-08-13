package com.verifone.analytics.transset.client;

import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.corechart.PieChartOptions;
import com.googlecode.gwt.charts.client.options.TextStyle;

public class VFIPieChart {
	
	private PieChart mopChart;
	private HTMLPanel mainPanel = new HTMLPanel("");
	
	private ColumnType columnType1;
	private ColumnType columnType2;
	
	private String columnName1;
	private String columnName2;
	private Map dataMap;
	private String title;
	
	
	private void drawPieChart() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(columnType1, columnName1);
		data.addColumn(columnType2, columnName2);
		
		for (Object entry : dataMap.keySet()) {
			data.addRow(entry, dataMap.get(entry));
		}
		PieChartOptions options = PieChartOptions.create();
		options.setTitle(title);
		TextStyle textStyle = TextStyle.create();
		textStyle.setFontName("");
		textStyle.setBold(false);
		textStyle.setFontSize(15);
		options.setTitleTextStyle(textStyle);
		mopChart.draw(data, options);
	}	
	
	public void setTitle (String title ) {
		this.title = title;
	}
	public void setDataColumnType1 (ColumnType type, String name) {
		columnType1 = type;
		columnName1 = name;
	}
	
	public void setDataColumnType2 (ColumnType type, String name) {
		columnType2 = type;
		columnName2 = name;
	}
	
	public void setData(Map data) {
		this.dataMap = data;
	}
	
	public HTMLPanel getPanel() {
//		HTMLPanel titlePanel = new HTMLPanel(title);
//		titlePanel.setStyleName("titlePanel");
//		mainPanel.add(titlePanel);
		mainPanel.setStyleName("chartContainer");
		
		ChartObject obj = new ChartObject();
		obj.setCorechart(ChartPackage.CORECHART);
		obj.setRunnable(new Runnable() {
			public void run() {
				// Create and attach the chart
				mopChart = new PieChart();
				mopChart.clearChart();
				drawPieChart();
				
				mainPanel.add(mopChart);
				RunChart.getInstance().notifyDone();
			}

		});
		RunChart.getInstance().getChartList().add(obj);

		return mainPanel;
	}
}
