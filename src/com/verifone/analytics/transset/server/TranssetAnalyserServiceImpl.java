package com.verifone.analytics.transset.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.verifone.analytics.transset.client.TranssetAnalyserService;
import com.verifone.analytics.transset.shared.PluDescriptionDateCount;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TranssetAnalyserServiceImpl extends RemoteServiceServlet implements TranssetAnalyserService {

	private static final String RESULTSET_DB_NAME = "resultSetDB";
	private static Map<String, String> jsonCollectionMap = new HashMap<String, String>();

	/**
	 * 
	 */
	public TranssetAnalyserServiceImpl() {
		super();
		loadResults();
	}
	
	/**
	 * Loads all the resultSetDB collections to the map. This pre-processing will
	 * reduce the user response time greatly.
	 */
	private static void loadResults() {
		try (MongoClient connection = new MongoClient("localhost", 27017)) {
			loadGroupPluByDate(connection);
			
			/**
			 * TODO Add specific methods to load your filtered collections
			 * from resultsetDB to the jsonCollectionMap.
			 */
			
		} finally {
		}
	}
	
	private static void loadGroupPluByDate(MongoClient connection) {
		String collection = "groupPluByDate";
		try (MongoCursor<Document> cursor = connection.getDatabase(RESULTSET_DB_NAME).getCollection(collection)
				.find().iterator();) {
			List<PluDescriptionDateCount> pluDescDateCountList = new ArrayList<PluDescriptionDateCount>();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				String desc = String.valueOf(doc.get("trPluDesc"));
				String date = String.valueOf(doc.get("trDate"));
				Integer count = Integer.valueOf(String.valueOf(doc.get("count")));
				pluDescDateCountList.add(new PluDescriptionDateCount(desc, date, count));
			}
			updateJsonCollectionMap(pluDescDateCountList, collection);
		} finally {
		}
	}


	/**
	 * Update the json collection map 
	 * @param object
	 * @param collection
	 */
	private static void updateJsonCollectionMap(Object object, String collection) {
		String jsonContent = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonContent = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		if(jsonContent != null) {
			jsonCollectionMap.put(collection, jsonContent);
		}
	}

	@Override
	public String getCollection(String collection) throws IllegalArgumentException {
		return jsonCollectionMap.get(collection);
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
//	private String escapeHtml(String html) {
//		if (html == null) {
//			return null;
//		}
//		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//	}



}
