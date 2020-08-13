package com.verifone.analytics.transset.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

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
		dataPanel.add(new VFIPieChart().getPanel());

		mainBoard.add(menubar);
		mainBoard.add(dataPanel);

		root.add(mainBoard);
	}

	private void addSiteNames() {
		dbService.getSiteName(new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				
				for (String siteName : result) {
					siteList.addItem(siteName);
				}
				siteList.setItemSelected(1, true);
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

}
