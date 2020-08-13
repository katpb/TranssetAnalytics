package com.verifone.analytics.transset.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.verifone.analytics.transset.shared.TransactionData;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("transsetAnalyserService")
public interface TranssetAnalyserService extends RemoteService {
	Map<String, List<? extends TransactionData>> getCollection(String siteId) throws IllegalArgumentException;

	List<String> getSiteName();
}
