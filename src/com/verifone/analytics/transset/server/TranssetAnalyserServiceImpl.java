package com.verifone.analytics.transset.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.verifone.analytics.transset.client.TranssetAnalyserService;
import com.verifone.analytics.transset.shared.CashierTrackingData;
import com.verifone.analytics.transset.shared.CategorySaleCount;
import com.verifone.analytics.transset.shared.CustomerCount;
import com.verifone.analytics.transset.shared.DailySalesInfo;
import com.verifone.analytics.transset.shared.DepartmentSaleCount;
import com.verifone.analytics.transset.shared.FuelProductSale;
import com.verifone.analytics.transset.shared.LoyaltyTransactionData;
import com.verifone.analytics.transset.shared.PluSaleCount;
import com.verifone.analytics.transset.shared.TransactionData;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TranssetAnalyserServiceImpl extends RemoteServiceServlet implements TranssetAnalyserService {
	
	private static Map<String, List<TransactionData>> collectionsMap = null;
	private static Map<String, Map<String, List<TransactionData>>> siteMap = null;
	
	public TranssetAnalyserServiceImpl() {
		super();
		loadResults();
	}
	
	/**
	 * Loads all the resultSetDB collections to the map. This pre-processing will
	 * reduce the user response time greatly.
	 */
	private static void loadResults() {
		int index = 5;
		siteMap = new HashMap<String, Map<String, List<TransactionData>>>();
		try(MongoClient connection = new MongoClient("localhost", 27017);){
			while(index >= 5 && index <= 9) {
				String dbName = "site" + index + "ResultsDB";
				MongoDatabase database = connection.getDatabase(dbName);
				collectionsMap = new HashMap<String, List<TransactionData>>();
				for (String collection : database.listCollectionNames()) {
					try(MongoCursor<Document> iterator = database.getCollection(collection).find().iterator();) {
						loadCollectionData(iterator, collection);
					}
				}
				siteMap.put("site" + index, (Map) collectionsMap);
				index++;
			}
		}
	}

	private static void loadCollectionData(MongoCursor<Document> iterator, String collection) {
		switch (collection) {
		case "salesByUPCTop":
			loadSalesByUPC(iterator, true);
			break;

		case "salesByUPCBottom":
			loadSalesByUPC(iterator, false);
			break;

		case "salesByCategoryTop":
			loadSalesByCategory(iterator, true);
			break;

		case "salesByCategoryBottom":
			loadSalesByCategory(iterator, false);
			break;

		case "salesByDepartmentTop":
			loadSalesByDepartment(iterator, true);
			break;

		case "salesByDepartmentBottom":
			loadSalesByDepartment(iterator, false);
			break;

		case "salesByFuelProduct":
			loadSalesByFuelProduct(iterator);
			break;

		case "customerWaitTimeByCashier":
			loadCustomerWaitTimeByCashier(iterator);
			break;

		case "dailySalesCount":
			loadDailySalesCount(iterator);
			break;

		case "averageTransactionAmount":
			loadAverageTransactionAmount(iterator);
			break;

		case "salesByMOP":
			loadSalesByMOP(iterator);
			break;

		case "salesByCardType":
			loadSalesByCardType(iterator);
			break;

		case "salesByEntryMethod":
			loadSalesByEntryMethod(iterator);
			break;

		case "discountsByLoyaltyProgram":
			loadDiscountsByLoyaltyProgram(iterator);
			break;

		case "recurringCustomerCount":
			loadRecurringCustomerCount(iterator);
			break;

		default:
			break;
		}
	}

	private static void loadSalesByUPC(MongoCursor<Document> iterator, boolean isTopList) {
		if(iterator != null) {
			List<PluSaleCount> pluSaleCount = new ArrayList<PluSaleCount>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String num = String.valueOf(doc.get("UPCNum"));
				String desc = String.valueOf(doc.get("UPCDesc"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				pluSaleCount.add(new PluSaleCount(date, num, desc, count));
			}
			
			// store the data in the collection map based on the flag 'isTopList'
			if(!pluSaleCount.isEmpty()) {
				collectionsMap.put(isTopList ? "salesByUPCTop" : "salesByUPCBottom", (List) pluSaleCount);
			}
		}
	}
	
	private static void loadSalesByCategory(MongoCursor<Document> iterator, boolean isTopList) {
		if(iterator != null) {
			List<CategorySaleCount> catSaleCount = new ArrayList<CategorySaleCount>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String num = String.valueOf(doc.get("CategoryNum"));
				String desc = String.valueOf(doc.get("CategoryDesc"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				catSaleCount.add(new CategorySaleCount(date, num, desc, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!catSaleCount.isEmpty()) {
				collectionsMap.put(isTopList ? "salesByCategoryTop" : "salesByCategoryBottom", (List) catSaleCount);
			}
		}
	}
	
	private static void loadSalesByDepartment(MongoCursor<Document> iterator, boolean isTopList) {
		if(iterator != null) {
			List<DepartmentSaleCount> deptSaleCount = new ArrayList<DepartmentSaleCount>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String num = String.valueOf(doc.get("DeptNum"));
				String desc = String.valueOf(doc.get("DeptDesc"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				deptSaleCount.add(new DepartmentSaleCount(date, num, desc, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!deptSaleCount.isEmpty()) {
				collectionsMap.put(isTopList ? "salesByDepartmentTop" : "salesByDepartmentBottom", (List) deptSaleCount);
			}
		}
	}

	private static void loadSalesByFuelProduct(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<FuelProductSale> fuelProductSales = new ArrayList<FuelProductSale>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String desc = String.valueOf(doc.get("FuelProductDesc"));
				Double volume = getDoubleValue(String.valueOf(doc.get("Amount")));
				Double amount = getDoubleValue(String.valueOf(doc.get("Volume")));
				fuelProductSales.add(new FuelProductSale(date, desc, volume, amount));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!fuelProductSales.isEmpty()) {
				collectionsMap.put("salesByFuelProduct", (List) fuelProductSales);
			}
		}
	}

	private static void loadCustomerWaitTimeByCashier(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<CashierTrackingData> cashierTrackingDataList = new ArrayList<CashierTrackingData>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String cashierName = String.valueOf(doc.get("CashierName"));
				Double avgWaitTime = getDoubleValue(String.valueOf(doc.get("AvgWaitTime")));
				cashierTrackingDataList.add(new CashierTrackingData(date, cashierName, avgWaitTime));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!cashierTrackingDataList.isEmpty()) {
				collectionsMap.put("customerWaitTimeByCashier", (List) cashierTrackingDataList);
			}
		}
	}

	private static void loadDailySalesCount(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<DailySalesInfo> dailySaleCounts = new ArrayList<DailySalesInfo>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				dailySaleCounts.add(new DailySalesInfo(date, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!dailySaleCounts.isEmpty()) {
				collectionsMap.put("dailySalesCount", (List) dailySaleCounts);
			}
		}
	}

	private static void loadAverageTransactionAmount(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<DailySalesInfo> dailySaleAmounts = new ArrayList<DailySalesInfo>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				Double avgWaitTime = getDoubleValue(String.valueOf(doc.get("AvgAmount")));
				dailySaleAmounts.add(new DailySalesInfo(date, avgWaitTime));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!dailySaleAmounts.isEmpty()) {
				collectionsMap.put("averageTransactionAmount", (List) dailySaleAmounts);
			}
		}		
	}

	private static void loadSalesByMOP(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<DailySalesInfo> dailySaleMopCounts = new ArrayList<DailySalesInfo>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String mopName = String.valueOf(doc.get("MopName"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				dailySaleMopCounts.add(new DailySalesInfo(date, mopName, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!dailySaleMopCounts.isEmpty()) {
				collectionsMap.put("salesByMOP", (List) dailySaleMopCounts);
			}
		}
		
	}

	private static void loadSalesByCardType(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<DailySalesInfo> dailySaleCardTypeCounts = new ArrayList<DailySalesInfo>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String cardType = String.valueOf(doc.get("CardType"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				dailySaleCardTypeCounts.add(new DailySalesInfo(date, cardType, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!dailySaleCardTypeCounts.isEmpty()) {
				collectionsMap.put("salesByCardType", (List) dailySaleCardTypeCounts);
			}
		}
		
	}

	private static void loadSalesByEntryMethod(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<DailySalesInfo> dailySaleEntryMethodCounts = new ArrayList<DailySalesInfo>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String entryMethod = String.valueOf(doc.get("EntryMethod"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				dailySaleEntryMethodCounts.add(new DailySalesInfo(date, entryMethod, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!dailySaleEntryMethodCounts.isEmpty()) {
				collectionsMap.put("salesByEntryMethod", (List) dailySaleEntryMethodCounts);
			}
		}
	}

	private static void loadDiscountsByLoyaltyProgram(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<LoyaltyTransactionData> loyaltyTxnDataList = new ArrayList<LoyaltyTransactionData>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
				String date = String.valueOf(doc.get("Date"));
				String program = String.valueOf(doc.get("LoyaltyProgram"));
				Double discAmt = getDoubleValue(String.valueOf(doc.get("DiscAmount")));
				loyaltyTxnDataList.add(new LoyaltyTransactionData(date, program, discAmt));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!loyaltyTxnDataList.isEmpty()) {
				collectionsMap.put("discountsByLoyaltyProgram", (List) loyaltyTxnDataList);
			}
		}
		
	}

	private static void loadRecurringCustomerCount(MongoCursor<Document> iterator) {
		if(iterator != null) {
			List<CustomerCount> customerCounts = new ArrayList<CustomerCount>();
			while(iterator.hasNext()) {
				Document doc = iterator.next();
//				String date = String.valueOf(doc.get("Date"));
				String custCardNum = String.valueOf(doc.get("CustCardNumber"));
				Integer count = getIntegerValue(String.valueOf(doc.get("Count")));
				customerCounts.add(new CustomerCount("", custCardNum, count));
			}
			// store the data in the collection map based on the flag 'isTopList'
			if(!customerCounts.isEmpty()) {
				collectionsMap.put("recurringCustomerCount", (List) customerCounts);
			}
		}
		
	}


	/**
	 * Update the json collection map 
	 * @param object
	 * @param collection
	 */
//	private static void updateJsonCollectionMap(Object object, String collection) {
//		String jsonContent = null;
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			jsonContent = objectMapper.writeValueAsString(object);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		
//		if(jsonContent != null) {
//			jsonCollectionMap.put(collection, jsonContent);
//		}
//	}

	private static Integer getIntegerValue(String value) {
		Integer intValue = 0;
		if(value != null) {
			try {
				intValue = Integer.valueOf(value);
			} catch (NumberFormatException nfe) {
//				nfe.printStackTrace();
			}
		}
		return intValue;
	}
	
	private static Double getDoubleValue(String value) {
		Double doubleValue = 0.0;
		if(value != null) {
			try {
				doubleValue = Double.valueOf(value);
			} catch (NumberFormatException nfe) {
//				nfe.printStackTrace();
			}
			
		}
		return doubleValue;
	}
	
	@Override
	public Map<String, List<TransactionData>> getCollection(String siteId) throws IllegalArgumentException {
		return siteMap.get(siteId);
	}

	public static void main(String[] args) {
		TranssetAnalyserServiceImpl o = new TranssetAnalyserServiceImpl();
		o.getCollection("");
	}


}
