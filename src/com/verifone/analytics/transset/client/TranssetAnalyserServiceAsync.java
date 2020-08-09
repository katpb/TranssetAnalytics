package com.verifone.analytics.transset.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.verifone.analytics.transset.shared.TransactionData;

public interface TranssetAnalyserServiceAsync {
	void getCollection(String siteId, AsyncCallback<Map<String, List<TransactionData>>> callback) throws IllegalArgumentException;
}
