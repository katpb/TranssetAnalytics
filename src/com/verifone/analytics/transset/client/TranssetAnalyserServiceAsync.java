package com.verifone.analytics.transset.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TranssetAnalyserServiceAsync {
	void getCollection(String collectionName, AsyncCallback<String> callback) throws IllegalArgumentException;
}
