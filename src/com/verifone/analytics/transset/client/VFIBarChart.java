package com.verifone.analytics.transset.client;

import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.ComboChart;
import com.googlecode.gwt.charts.client.corechart.ComboChartOptions;
import com.googlecode.gwt.charts.client.options.Bar;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendAlignment;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.SeriesType;
import com.googlecode.gwt.charts.client.options.VAxis;

public class VFIBarChart {

	private ComboChart chart;
	private HTMLPanel mainPanel = new HTMLPanel("");
	
	private ColumnType columnType1;
	private ColumnType columnType2;
	
	private String columnName1;
	private String columnName2;
	private Map dataMap;
	private String title;
	private String hAxis;
	private String vAxis;
	private SeriesType seriesType = SeriesType.BARS;
	
	
	private void drawBarChart() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(columnType1, columnName1);
		data.addColumn(columnType2, columnName2);
		
		for (Object entry : dataMap.keySet()) {
			data.addRow(entry, dataMap.get(entry));
		}
		ComboChartOptions options = ComboChartOptions.create();		
		options.setHAxis(HAxis.create(hAxis));
		options.setVAxis(VAxis.create(vAxis));
		options.setSeriesType(seriesType);
		Legend legend = Legend.create();
		legend.setPosition(LegendPosition.NONE);
		options.setLegend(legend);
		chart.draw(data, options);
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
	
	public String gethAxis() {
		return hAxis;
	}

	public void sethAxis(String hAxis) {
		this.hAxis = hAxis;
	}

	public String getvAxis() {
		return vAxis;
	}

	public void setvAxis(String vAxis) {
		this.vAxis = vAxis;
	}
	
	public SeriesType getSeriesType() {
		return seriesType;
	}

	public void setSeriesType(SeriesType seriesType) {
		this.seriesType = seriesType;
	}

	public HTMLPanel getPanel() {
		HTMLPanel titlePanel = new HTMLPanel(title);
		titlePanel.setStyleName("titlePanel");
		mainPanel.setStyleName("chartContainer");
		mainPanel.add(titlePanel);
		ChartObject obj = new ChartObject();
		obj.setCorechart(ChartPackage.CORECHART);
		obj.setRunnable(new Runnable() {
			public void run() {
				// Create and attach the chart
				chart = new ComboChart();
				chart.clearChart();
				drawBarChart();
				
				mainPanel.add(chart);
				RunChart.getInstance().notifyDone();
			}

		});
		RunChart.getInstance().getChartList().add(obj);

		return mainPanel;
	}
}
