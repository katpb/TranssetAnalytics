package com.verifone.analytics.transset.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.verifone.analytics.transset.shared.TransactionData;

public class ChangeSiteNameHandler implements ChangeHandler {

	@Override
	public void onChange(ChangeEvent event) {
		String selectedText = ((ListBox) event.getSource()).getSelectedItemText();
		GWT.log("Selected value : " + selectedText);
		WorkingArea.getInstance().getDbService().getCollection(selectedText,
				new AsyncCallback<Map<String, List<? extends TransactionData>>>() {
					public void onFailure(Throwable caught) {
						GWT.log("Error in fecthing Data : ", caught);
					}

					@Override
					public void onSuccess(Map<String, List<? extends TransactionData>> result) {
						GWT.log("" + result.toString());
					}
				});

	}

}
