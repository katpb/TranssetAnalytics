package com.verifone.analytics.transset.client;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.options.SeriesType;
import com.verifone.analytics.transset.shared.CashierTrackingData;
import com.verifone.analytics.transset.shared.CategorySaleCount;
import com.verifone.analytics.transset.shared.CustomerCount;
import com.verifone.analytics.transset.shared.DailySalesInfo;
import com.verifone.analytics.transset.shared.FuelProductSale;
import com.verifone.analytics.transset.shared.PluSaleCount;
import com.verifone.analytics.transset.shared.TransactionData;

public final class WorkingArea {

	private static final WorkingArea INST = new WorkingArea();
	private RootPanel root = RootPanel.get();
	private HTMLPanel mainBoard = new HTMLPanel("");
	private HTMLPanel menubar = new HTMLPanel("");
	private HTMLPanel dataPanel = new HTMLPanel("");
	private ListBox siteList = new ListBox();
	private static final TranssetAnalyserServiceAsync dbService = GWT.create(TranssetAnalyserService.class);
	
	private WorkingArea() {

	}

	public static final WorkingArea getInstance() {
		return INST;
	}

	public void drawUI() {
		// Add style to the panel
		menubar.setStyleName("menubar");
		dataPanel.setStyleName("dataPanel");
		mainBoard.setStyleName("dashboard");
		siteList.setStyleName("listBox");

		siteList.addChangeHandler(new ChangeSiteNameHandler());
		
		// fill data to List box  
		addSiteNames();

		menubar.add(siteList);

		mainBoard.add(menubar);
		mainBoard.add(dataPanel);
		root.add(mainBoard);
	}

	private void addSiteNames() {
		dbService.getSiteName(new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				siteList.addItem("");
				for (String siteName : result) {
					siteList.addItem(siteName);
				}
				siteList.setItemSelected(0, true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Unable to fetch : ", caught);
				
			}
		});		
	}

	public void addToDataPanel(Widget widget) {
		dataPanel.clear();
		dataPanel.add(widget);
	}

	public TranssetAnalyserServiceAsync getDbService() {
		return dbService;
	}

	public void setData(Map<String, List<? extends TransactionData>> result) {
		dataPanel.clear();
		// Get the result and draw chart
		for (String entry :result.keySet()) {
			switch (entry) {
			case "salesByUPCTop":
				loadSalesByUPCTop(entry, result.get(entry));
				break;

			case "salesByUPCBottom":
				loadSalesByUPBottom(entry, result.get(entry));
				break;

			case "salesByCategoryTop":
				loadSalesByCategoryTop(entry, result.get(entry));
				break;

			case "salesByCategoryBottom":
				loadSalesByCategoryBottom(entry, result.get(entry));
				break;

			case "salesByDepartmentTop":
				loadSalesByDepartmentTop(entry, result.get(entry));
				break;

			case "salesByDepartmentBottom":
				loadSalesByDepartmentBottom(entry, result.get(entry));
				break;

			case "salesByFuelProduct":
				loadSalesByFuelProduct(entry, result.get(entry));
				break;

			case "averageWaitTimeByCashier":
				loadCustomerWaitTimeByCashier(entry, result.get(entry));
				break;

			case "dailySalesCount":
				loadDailySalesCount(entry, result.get(entry));
				break;

			case "averageTransactionAmount":
				loadAverageTransactionAmount(entry, result.get(entry));
				break;

			case "salesByMOP":
				loadSalesByMOP(entry, result.get(entry));
				break;

			case "salesByCardType":
				loadSalesByCardType(entry, result.get(entry));
				break;

			case "salesByEntryMethod":
				loadSalesByEntryMethod(entry, result.get(entry));
				break;

			case "discountsByLoyaltyProgram":
				loadDiscountsByLoyaltyProgram(entry, result.get(entry));
				break;

			case "recurringCustomerCount":
				loadRecurringCustomerCount(entry, result.get(entry));
				break;
			}			
		}		
		
		RunChart.getInstance().run();
	}

	private void loadSalesByCardType(String entry, List<? extends TransactionData> list) {
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle("Sales By Item Type");
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DailySalesInfo sinfo = (DailySalesInfo)dailySaleInfo;
			pluCountMap.put(sinfo.getType(), sinfo.getCount());
		}
		pieChart.setData(pluCountMap);
		dataPanel.add(pieChart.getPanel());
	}

	private void loadRecurringCustomerCount(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Entry-Method description");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number Time use");
		Map <String, Integer> customerCountMap = new HashMap<String, Integer>();
		for (TransactionData txnData : list) {
			CustomerCount customerCount = (CustomerCount) txnData;
			customerCountMap.put(customerCount.getCustomerCardNumber(), customerCount.getCount());
		}
		pieChart.setData(customerCountMap);
		pieChart.sethAxis("Entry-Method");
		pieChart.sethAxis("Count");
		dataPanel.add(pieChart.getPanel());
	}

	private void loadDiscountsByLoyaltyProgram(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByEntryMethod(String entry, List<? extends TransactionData> list) {
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle("Sales By MOP Entry Method");
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Integer> entryCountMap = new HashMap<String, Integer>();
		for (TransactionData txnData : list) {
			DailySalesInfo entryCount = (DailySalesInfo) txnData;
			entryCountMap.put(entryCount.getType(), entryCount.getCount());
		}
		pieChart.setData(entryCountMap);
		dataPanel.add(pieChart.getPanel());
	}

	private void loadSalesByMOP(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadAverageTransactionAmount(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setTitle("Average Transaction Amount");
		lineChart.setDataColumnType1(ColumnType.STRING, "");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Double> custWaitTimeMap = new HashMap<String, Double>();
		for (TransactionData txnData : list) {
			DailySalesInfo avgTxnTime = (DailySalesInfo) txnData;
			custWaitTimeMap.put(avgTxnTime.getDate(), avgTxnTime.getAvgAmount());
		}
		lineChart.setData(custWaitTimeMap);
		lineChart.sethAxis("Date");
		lineChart.setvAxis("Average Amount");
		dataPanel.add(lineChart.getPanel());
	}

	private void loadDailySalesCount(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadCustomerWaitTimeByCashier(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setTitle("Customer Waiting Time By Cashier");
		lineChart.setDataColumnType1(ColumnType.STRING, "");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Double> custWaitTimeMap = new HashMap<String, Double>();
		for (TransactionData txnData : list) {
			CashierTrackingData custWaitTime = (CashierTrackingData) txnData;
			custWaitTimeMap.put(custWaitTime.getCashierName(), custWaitTime.getAvgCustomerWaitTime());
		}
		lineChart.setData(custWaitTimeMap);
		lineChart.sethAxis("Cashier Name");
		lineChart.setvAxis("Customer Wait Time");
		dataPanel.add(lineChart.getPanel());
	}

	private void loadSalesByFuelProduct(String entry, List<? extends TransactionData> list) {
		VFIBarChart barChart = new VFIBarChart();
		barChart.setTitle("Sales By Fuel Products");
		barChart.setDataColumnType1(ColumnType.STRING, "");
		barChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Double> fuelSalesMap = new HashMap<String, Double>();
		for (TransactionData fuelSalesData : list) {
			FuelProductSale fuelSale = (FuelProductSale) fuelSalesData;
			fuelSalesMap.put(fuelSale.getDescription(), fuelSale.getAmount());
		}
		barChart.setData(fuelSalesMap);
		barChart.sethAxis("Amount");
		barChart.setvAxis("Fuel Product");
		dataPanel.add(barChart.getPanel());
	}

	private void loadSalesByDepartmentBottom(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByDepartmentTop(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByCategoryBottom(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByCategoryTop(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Category description");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number of Sales");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			CategorySaleCount catSalesCount = (CategorySaleCount)dailySaleInfo;
			pluCountMap.put(catSalesCount.getCategoryDesc(), catSalesCount.getCount());
		}
		pieChart.setData(pluCountMap);
		pieChart.sethAxis("Category");
		pieChart.sethAxis("Count");
		dataPanel.add(pieChart.getPanel());
		
	}

	private void loadSalesByUPBottom(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByCategory(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByUPC(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByUPCTop(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setTitle(entry);
		lineChart.setDataColumnType1(ColumnType.STRING, "Plu description");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "Number Plu Sales");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			PluSaleCount pluSalesCount = (PluSaleCount)dailySaleInfo;
			pluCountMap.put(pluSalesCount.getUPCDesc(), pluSalesCount.getCount());
		}
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setData(pluCountMap);
		lineChart.sethAxis("Plu");
		lineChart.sethAxis("Count");
		dataPanel.add(lineChart.getPanel());
		
	}
	
}