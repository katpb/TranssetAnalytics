package com.verifone.analytics.transset.client;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.PieChart;

public class VFIPieChart {
	
	private PieChart mopChart;
	private HTMLPanel mainPanel = new HTMLPanel("");
	private void drawMopPieChart() {
		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartLoader.loadApi(new Runnable() {
			public void run() {
				// Create and attach the chart
				mopChart = new PieChart();
				mopChart.clearChart();
				
				drawPieChart();
				mainPanel.add(mopChart);
			}

		});	
		
	}
	private void drawPieChart() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Mop");
		data.addColumn(ColumnType.NUMBER, "Amount");
		
		data.addRow("Cash", 100);
		data.addRow("Credit", 200);
		data.addRow("Debit", 300);
		data.addRow("Cupon", 500);
		data.addRow("XYZ", 600);
		data.addRow("abc", 700);
		mopChart.draw(data);
	}
	
	public HTMLPanel getPanel() {
		drawMopPieChart();
		return mainPanel;
	}

	public void setData() {
		
	}
}
