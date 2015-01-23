package com.lovealot.stickers;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;

public class MainStickersTap extends Activity {
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2, Tab3;
	Fragment fragmentTab1 = new StickersTab1();
	Fragment fragmentTab2 = new StickersTab2();
	Fragment fragmentTab3 = new StickersTab3();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintap);
		
		ActionBar actionBar = getActionBar();

		// Hide Actionbar Icon
		actionBar.setDisplayShowHomeEnabled(false);

		// Hide Actionbar Title
		actionBar.setDisplayShowTitleEnabled(false);

		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set Tab Icon and Titles
		Tab1 = actionBar.newTab().setIcon(R.drawable.stic_m);
		Tab2 = actionBar.newTab().setIcon(R.drawable.stic_f);
		Tab3 = actionBar.newTab().setIcon(R.drawable.stic_two);

		// Set Tab Listeners
		Tab1.setTabListener(new StickersTabListener(fragmentTab1));
		Tab2.setTabListener(new StickersTabListener(fragmentTab2));
		Tab3.setTabListener(new StickersTabListener(fragmentTab3));

		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
		actionBar.addTab(Tab3);
	}
}
