package com.verifone.analytics.transset.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("transsetAnalyserService")
public interface TranssetAnalyserService extends RemoteService {
	String getCollection(String collection) throws IllegalArgumentException;
}
