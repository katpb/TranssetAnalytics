package com.verifone.analytics.transset.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.options.SeriesType;
import com.verifone.analytics.transset.shared.CashierTrackingData;
import com.verifone.analytics.transset.shared.CategorySaleCount;
import com.verifone.analytics.transset.shared.CustomerCount;
import com.verifone.analytics.transset.shared.DailySalesInfo;
import com.verifone.analytics.transset.shared.DepartmentSaleCount;
import com.verifone.analytics.transset.shared.FuelProductSale;
import com.verifone.analytics.transset.shared.LoyaltyTransactionData;
import com.verifone.analytics.transset.shared.PluSaleCount;
import com.verifone.analytics.transset.shared.TransactionData;

public final class WorkingArea {

	private static final WorkingArea INST = new WorkingArea();
	private RootPanel root = RootPanel.get();
	private HTMLPanel mainBoard = new HTMLPanel("");
	private HTMLPanel menubar = new HTMLPanel("");
	private HTMLPanel dataPanel = new HTMLPanel("");	
	
	// Different container 
	private HTMLPanel upcPanel = new HTMLPanel("");
	private HTMLPanel categotyPanel = new HTMLPanel("");
	private HTMLPanel departmentPanel = new HTMLPanel("");
	
	private HTMLPanel fuelPanel = new HTMLPanel("");
	
	private HTMLPanel paymentPanel = new HTMLPanel("");
	
	private HTMLPanel avgPanel = new HTMLPanel("");
	
	private HTMLPanel salesCustCountPanel = new HTMLPanel("");
	
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
		mainBoard.setStyleName("dashboard");
		siteList.setStyleName("listBox");
		dataPanel.setStyleName("dataPanel");
		
		upcPanel.setStyleName("upcpanel");
		categotyPanel.setStyleName("categoryPanel");
		departmentPanel.setStyleName("departmentPanel");
		paymentPanel.setStyleName("paymentPanel");
		
		fuelPanel.setStyleName("fuelPanel");
		
		avgPanel.setStyleName("avgPanel");
		
		siteList.addChangeHandler(new ChangeSiteNameHandler());
		
		salesCustCountPanel.setStyleName("salesCustCountPanel");
		
		// fill data to List box  
		addSiteNames();

		siteList.addChangeHandler(new ChangeSiteNameHandler());
		HTMLPanel storeSelector = new HTMLPanel("Store Locator");
		storeSelector.add(siteList);
		storeSelector.setStyleName("storeSelector");
		
		menubar.add(storeSelector);
		HTMLPanel title = new HTMLPanel("C-Store Analytics DashBoard");
		title.setStyleName("header-title");
		menubar.add(title);
		
		// Data table added element 
		dataPanel.add(upcPanel);
		dataPanel.add(categotyPanel);
		dataPanel.add(departmentPanel);
		dataPanel.add(paymentPanel);
		dataPanel.add(fuelPanel);
		dataPanel.add(avgPanel);
		dataPanel.add(salesCustCountPanel);
		
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

	public TranssetAnalyserServiceAsync getDbService() {
		return dbService;
	}

	public void setData(Map<String, List<? extends TransactionData>> result) {
		clearPanels();
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
				//loadFuelSaleTable(entry, result.get(entry));
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
		paymentPanel.add(pieChart.getPanel());
	}

	private void loadRecurringCustomerCount(String entry, List<? extends TransactionData> list) {
		VFIBarChart barChart = new VFIBarChart();
		barChart.setTitle("Recurring Customer Count");
		barChart.setDataColumnType1(ColumnType.STRING, "");
		barChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Integer> customerCountMap = new HashMap<String, Integer>();
		for (TransactionData txnData : list) {
			CustomerCount customerCount = (CustomerCount) txnData;
			String cardNumber = customerCount.getCustomerCardNumber();
			if(cardNumber != null) {
				cardNumber = cardNumber.substring(cardNumber.length() - 4);
			}
			customerCountMap.put(cardNumber, customerCount.getCount());
		}
		barChart.setData(customerCountMap);
		salesCustCountPanel.add(pieChart.getPanel());
		barChart.sethAxis("CustomerCard");
		barChart.setvAxis("Count");
		dataPanel.add(barChart.getPanel());
	}

	private void loadDiscountsByLoyaltyProgram(String entry, List<? extends TransactionData> list) {
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Double> pluCountMap = new HashMap<String, Double>();
		for (TransactionData dailySaleInfo : list) {
			LoyaltyTransactionData loyalData = (LoyaltyTransactionData)dailySaleInfo;
			pluCountMap.put(loyalData.getLoyaltyProgram(), loyalData.getDiscAmount());
		}
		pieChart.setData(pluCountMap);
		paymentPanel.add(pieChart.getPanel());

		
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
		paymentPanel.add(pieChart.getPanel());
	}

	private void loadSalesByMOP(String entry, List<? extends TransactionData> list) {
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map<String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DailySalesInfo dailySalesCount = (DailySalesInfo) dailySaleInfo;
			if (!dailySalesCount.getDate().equals("null")) {
				pluCountMap.put(dailySalesCount.getType(), dailySalesCount.getCount());
			}
		}
		GWT.log(pluCountMap.size() + "");
		pieChart.setData(pluCountMap);		
		paymentPanel.add(pieChart.getPanel());

	}

	private void loadAverageTransactionAmount(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setSeriesType(SeriesType.AREA);
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
		avgPanel.add(lineChart.getPanel());
	}

	private void loadDailySalesCount(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setTitle(entry);
		lineChart.setDataColumnType1(ColumnType.STRING, "");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DailySalesInfo dailySalesCount = (DailySalesInfo)dailySaleInfo;
			if (!dailySalesCount.getDate().equals("null")) {
				pluCountMap.put(dailySalesCount.getDate(), dailySalesCount.getCount());
			}
		}
		GWT.log(pluCountMap.size() + "");
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setData(pluCountMap);
		lineChart.sethAxis("Plu");
		lineChart.sethAxis("Count");
		salesCustCountPanel.add(lineChart.getPanel());

		
	}

	private void loadCustomerWaitTimeByCashier(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setSeriesType(SeriesType.AREA);
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
		avgPanel.add(lineChart.getPanel());
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
		fuelPanel.add(barChart.getPanel());
		
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle(entry);
		pieChart.setTitle("Volume Sold By Fuel Products");
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		fuelSalesMap = new HashMap<String, Double>();
		for (TransactionData fuelSalesData : list) {
			FuelProductSale fuelSale = (FuelProductSale) fuelSalesData;
			fuelSalesMap.put(fuelSale.getDescription(), fuelSale.getVolume());
		}
		pieChart.setData(fuelSalesMap);
		fuelPanel.add(pieChart.getPanel());
	}

	private void loadSalesByDepartmentBottom(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		pieChart.setSeriesType(SeriesType.BARS);
		Map <String, Integer> deptCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DepartmentSaleCount deptSalesCount = (DepartmentSaleCount)dailySaleInfo;
			deptCountMap.put(deptSalesCount.getDeptDesc(), deptSalesCount.getCount());
		}
		pieChart.setData(deptCountMap);
		pieChart.sethAxis("Department");
		pieChart.sethAxis("Count");
		departmentPanel.add(pieChart.getPanel());
	}

	private void loadSalesByDepartmentTop(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		pieChart.setSeriesType(SeriesType.BARS);
		Map <String, Integer> deptCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DepartmentSaleCount deptSalesCount = (DepartmentSaleCount)dailySaleInfo;
			deptCountMap.put(deptSalesCount.getDeptDesc().substring(0, 8), deptSalesCount.getCount());
		}
		pieChart.setData(deptCountMap);
		pieChart.sethAxis("Department");
		pieChart.sethAxis("Count");
		departmentPanel.add(pieChart.getPanel());
	}

	private void loadSalesByCategoryBottom(String entry, List<? extends TransactionData> list) {
		VFIBarChart barChart = new VFIBarChart();
		barChart.setTitle(entry);
		barChart.setDataColumnType1(ColumnType.STRING, "");
		barChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Double> pluCountMap = new HashMap<String, Double>();
		for (TransactionData dailySaleInfo : list) {
			CategorySaleCount catSalesCount = (CategorySaleCount)dailySaleInfo;
			pluCountMap.put(catSalesCount.getCategoryDesc(), catSalesCount.getCount());
		}
		barChart.setData(pluCountMap);
		barChart.sethAxis("Category");
		barChart.sethAxis("Count");
		categotyPanel.add(barChart.getPanel());		
	}

	private void loadSalesByCategoryTop(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Double> pluCountMap = new HashMap<String, Double>();
		for (TransactionData dailySaleInfo : list) {
			CategorySaleCount catSalesCount = (CategorySaleCount) dailySaleInfo;
			pluCountMap.put(catSalesCount.getCategoryDesc(), catSalesCount.getCount());
		}
		pieChart.setData(pluCountMap);
		pieChart.sethAxis("Category");
		pieChart.sethAxis("Count");
		categotyPanel.add(pieChart.getPanel());
	}

	private void loadSalesByUPBottom(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setTitle(entry);
		lineChart.setDataColumnType1(ColumnType.STRING, "");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			PluSaleCount pluSalesCount = (PluSaleCount)dailySaleInfo;
			pluCountMap.put(pluSalesCount.getUPCDesc(), pluSalesCount.getCount());
		}
		GWT.log(pluCountMap.size() + "");
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setData(pluCountMap);
		lineChart.sethAxis("Plu");
		lineChart.sethAxis("Count");
		upcPanel.add(lineChart.getPanel());

		
	}

	private void loadSalesByUPCTop(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setTitle(entry);
		lineChart.setDataColumnType1(ColumnType.STRING, "");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			PluSaleCount pluSalesCount = (PluSaleCount)dailySaleInfo;
			pluCountMap.put(pluSalesCount.getUPCDesc(), pluSalesCount.getCount());
		}
		GWT.log(pluCountMap.size() + "");
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setData(pluCountMap);
		lineChart.sethAxis("Plu");
		lineChart.sethAxis("Count");
		upcPanel.add(lineChart.getPanel());
		
	}
//	private void loadFuelSaleTable(String entry, List<? extends TransactionData> list) {
//		VFITable table = new VFITable();
//		table.setTitle("Fuel Product Sale Table");
//		List<FuelProductSale> fuelProductSaleList = new ArrayList<FuelProductSale>();
//		for (TransactionData trData : list) {
//			FuelProductSale fuelProdSale = (FuelProductSale)trData;
//			fuelProductSaleList.add(fuelProdSale);			
//		}
//		table.setDataList(fuelProductSaleList);
//		fuelPanel.add(table.getPanel());
//	}
	
	private void clearPanels() {
		upcPanel.clear();
		categotyPanel.clear();
		departmentPanel.clear();
		fuelPanel.clear();
		paymentPanel.clear();
		avgPanel.clear();
		salesCustCountPanel.clear();
		
	}
	
}
