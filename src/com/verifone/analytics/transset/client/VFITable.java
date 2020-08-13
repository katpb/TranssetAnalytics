package com.verifone.analytics.transset.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.table.Table;
import com.googlecode.gwt.charts.client.table.TableOptions;
import com.verifone.analytics.transset.shared.FuelProductSale;

public class VFITable {
	
	private Table table;

	private HTMLPanel mainPanel = new HTMLPanel("");
	
	private List<FuelProductSale> dataList;

	private String title;
	
	
	private void drawPieChart() {
		// Prepare the data
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Date");
		data.addColumn(ColumnType.STRING, "Fuel Product");
		data.addColumn(ColumnType.NUMBER, "Volume");
		data.addColumn(ColumnType.NUMBER, "Amount");
		
		int row = 0;
		int col = 0;
		data.addRows(dataList.size());
		for (int index = 0; index < dataList.size(); index++) {			
			FuelProductSale fuelSale = dataList.get(index);
			data.setCell(row,col++, fuelSale .getDate());
			data.setCell(row,col++, fuelSale.getDescription());
			data.setCell(row,col++, fuelSale.getVolume());
			data.setCell(row,col++, fuelSale.getAmount());
			row++;
			col = 0;
		}
		
		TableOptions options = TableOptions.create();
		options.setAlternatingRowStyle(true);
		options.setShowRowNumber(true);
		options.setPageSize(20);
		
		table.draw(data, options);
	}	
	
	public void setTitle (String title ) {
		this.title = title;
	}
	public void setDataColumnType1 (ColumnType type, String name) {
	}
	
	public void setDataColumnType2 (ColumnType type, String name) {
	}
	
	public List<FuelProductSale> getDataList() {
		return dataList;
	}

	public void setDataList(List<FuelProductSale> dataList) {
		this.dataList = dataList;
	}
	
	public HTMLPanel getPanel() {
		HTMLPanel titlePanel = new HTMLPanel(title);
		titlePanel.setStyleName("titlePanel");
		mainPanel.add(titlePanel);
		
		ChartObject obj = new ChartObject();
		obj.setCorechart(ChartPackage.TABLE);
		obj.setRunnable(new Runnable() {
			public void run() {
				// Create and attach the chart
				table = new Table();
				table.clearChart();
				drawPieChart();
				
				mainPanel.add(table);
				RunChart.getInstance().notifyDone();
			}

		});
		RunChart.getInstance().getChartList().add(obj);

		return mainPanel;
	}
}
