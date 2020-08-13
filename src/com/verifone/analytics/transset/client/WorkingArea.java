package com.verifone.analytics.transset.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.options.SeriesType;
import com.verifone.analytics.transset.shared.CategorySaleCount;
import com.verifone.analytics.transset.shared.DailySalesInfo;
import com.verifone.analytics.transset.shared.DepartmentSaleCount;
import com.verifone.analytics.transset.shared.LoyaltyTransactionData;
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

			case "customerWaitTimeByCashier":
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
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Plu description");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number Plu Sales");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DailySalesInfo sinfo = (DailySalesInfo)dailySaleInfo;
			pluCountMap.put(sinfo.getType(), sinfo.getCount());
		}
		pieChart.setData(pluCountMap);
		dataPanel.add(pieChart.getPanel());
	}

	private void loadRecurringCustomerCount(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadDiscountsByLoyaltyProgram(String entry, List<? extends TransactionData> list) {
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "LoyalityPrograms");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Discount Amount");
		Map <String, Double> pluCountMap = new HashMap<String, Double>();
		for (TransactionData dailySaleInfo : list) {
			LoyaltyTransactionData loyalData = (LoyaltyTransactionData)dailySaleInfo;
			pluCountMap.put(loyalData.getLoyaltyProgram(), loyalData.getDiscAmount());
		}
		pieChart.setData(pluCountMap);
		dataPanel.add(pieChart.getPanel());

		
	}

	private void loadSalesByEntryMethod(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Entry-Method description");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number Time use");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DailySalesInfo pluSalesCount = (DailySalesInfo)dailySaleInfo;
			pluCountMap.put(pluSalesCount.getType(), pluSalesCount.getCount());
		}
		pieChart.setData(pluCountMap);
		pieChart.sethAxis("Entry-Method");
		pieChart.sethAxis("Count");
		dataPanel.add(pieChart.getPanel());
		
	}

	private void loadSalesByMOP(String entry, List<? extends TransactionData> list) {
		VFIPieChart pieChart = new VFIPieChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Date");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number of Sales");
		Map<String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DailySalesInfo dailySalesCount = (DailySalesInfo) dailySaleInfo;
			if (!dailySalesCount.getDate().equals("null")) {
				pluCountMap.put(dailySalesCount.getType(), dailySalesCount.getCount());
			}
		}
		GWT.log(pluCountMap.size() + "");
		pieChart.setData(pluCountMap);		
		dataPanel.add(pieChart.getPanel());

	}

	private void loadAverageTransactionAmount(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadDailySalesCount(String entry, List<? extends TransactionData> list) {
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setTitle(entry);
		lineChart.setDataColumnType1(ColumnType.STRING, "Date");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "Number of Sales");
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
		dataPanel.add(lineChart.getPanel());

		
	}

	private void loadCustomerWaitTimeByCashier(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByFuelProduct(String entry, List<? extends TransactionData> list) {
		// TODO Auto-generated method stub
		
	}

	private void loadSalesByDepartmentBottom(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Department description");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number of Sales");
		pieChart.setSeriesType(SeriesType.BARS);
		Map <String, Integer> deptCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DepartmentSaleCount deptSalesCount = (DepartmentSaleCount)dailySaleInfo;
			deptCountMap.put(deptSalesCount.getDeptDesc(), deptSalesCount.getCount());
		}
		pieChart.setData(deptCountMap);
		pieChart.sethAxis("Department");
		pieChart.sethAxis("Count");
		dataPanel.add(pieChart.getPanel());
	}

	private void loadSalesByDepartmentTop(String entry, List<? extends TransactionData> list) {
		VFIBarChart pieChart = new VFIBarChart();
		pieChart.setTitle(entry);
		pieChart.setDataColumnType1(ColumnType.STRING, "Department description");
		pieChart.setDataColumnType2(ColumnType.NUMBER, "Number of Sales");
		pieChart.setSeriesType(SeriesType.AREA);
		Map <String, Integer> deptCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			DepartmentSaleCount deptSalesCount = (DepartmentSaleCount)dailySaleInfo;
			deptCountMap.put(deptSalesCount.getDeptDesc(), deptSalesCount.getCount());
		}
		pieChart.setData(deptCountMap);
		pieChart.sethAxis("Department");
		pieChart.sethAxis("Count");
		dataPanel.add(pieChart.getPanel());
	}

	private void loadSalesByCategoryBottom(String entry, List<? extends TransactionData> list) {
		VFIBarChart barChart = new VFIBarChart();
		barChart.setTitle(entry);
		barChart.setDataColumnType1(ColumnType.STRING, "Category description");
		barChart.setDataColumnType2(ColumnType.NUMBER, "Number of Sales");
		Map <String, Integer> pluCountMap = new HashMap<String, Integer>();
		for (TransactionData dailySaleInfo : list) {
			CategorySaleCount catSalesCount = (CategorySaleCount)dailySaleInfo;
			pluCountMap.put(catSalesCount.getCategoryDesc(), catSalesCount.getCount());
		}
		barChart.setData(pluCountMap);
		barChart.sethAxis("Category");
		barChart.sethAxis("Count");
		dataPanel.add(barChart.getPanel());
		
		
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
		VFIBarChart lineChart = new VFIBarChart();
		lineChart.setTitle(entry);
		lineChart.setDataColumnType1(ColumnType.STRING, "Plu description");
		lineChart.setDataColumnType2(ColumnType.NUMBER, "Number Plu Sales");
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
		dataPanel.add(lineChart.getPanel());

		
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
		GWT.log(pluCountMap.size() + "");
		lineChart.setSeriesType(SeriesType.LINE);
		lineChart.setData(pluCountMap);
		lineChart.sethAxis("Plu");
		lineChart.sethAxis("Count");
		dataPanel.add(lineChart.getPanel());
		
	}

}
