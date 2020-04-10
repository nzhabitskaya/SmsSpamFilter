package com.mobile.android.smsspamfilter.activities;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.dialog.AboutDialogActivity;
import com.mobile.android.smsspamfilter.fragments.AllowListFragment;
import com.mobile.android.smsspamfilter.fragments.BlockListFragment;
import com.mobile.android.smsspamfilter.fragments.SettingsFragment;
import com.mobile.android.smsspamfilter.fragments.SpamExpandableListFragment;
import com.mobile.android.smsspamfilter.loader.CallHistoryLoader;
import com.mobile.android.smsspamfilter.loader.ContactsLoader;
import com.mobile.android.smsspamfilter.loader.RecipientsLoader;
import com.mobile.android.smsspamfilter.loader.SmsLoader;
import com.mobile.android.smsspamfilter.log.SendEmailUtil;
import com.mobile.android.smsspamfilter.service.ServiceTool;
import com.mobile.android.smsspamfilter.service.SmsReceiverService;

public class MainTabHostActivity extends LogActivity{
	private FragmentTabHost mTabHost;
	
	/** Data Loaders */
	public static final int CONTACT_LOADER_ID = 0;
	public static final int RECIPIENTS_LOADER_ID = 1;
	public static final int CALL_HISTORY_LOADER_ID = 2;
	public static final int SMS_LOADER_ID = 3;
	
	private ContactsLoader contactsLoader;
	private RecipientsLoader recipientsLoader;
	private CallHistoryLoader callHistoryLoader;
	private SmsLoader smsLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_layout);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.addTab(
				mTabHost.newTabSpec(getString(R.string.tab_spam)).setIndicator(getString(R.string.tab_spam),
						getResources().getDrawable(android.R.drawable.ic_menu_delete)), SpamExpandableListFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(getString(R.string.tab_allow)).setIndicator(getString(R.string.tab_allow),
						getResources().getDrawable(android.R.drawable.ic_menu_add)), AllowListFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(getString(R.string.tab_block)).setIndicator(getString(R.string.tab_block),
						getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel)), BlockListFragment.class, null);
		mTabHost.addTab(
				mTabHost.newTabSpec(getString(R.string.tab_settings)).setIndicator(getString(R.string.tab_settings),
						getResources().getDrawable(android.R.drawable.ic_menu_manage)), SettingsFragment.class, null);
		
		/** Open spam tab from push notification */
		Bundle extras = getIntent().getExtras(); 
		if (extras != null) {
			String reg_id = getIntent().getStringExtra("registration_id");
			if(reg_id.equals("spam"))
				mTabHost.setCurrentTab(0);
		}
		
		/** Load SmsHistory and list of contacts */
		contactsLoader = new ContactsLoader(this);
		recipientsLoader = new RecipientsLoader(this);
		callHistoryLoader = new CallHistoryLoader(this);
		smsLoader = new SmsLoader(this);
		getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, null, contactsLoader);
		getSupportLoaderManager().initLoader(RECIPIENTS_LOADER_ID, null, recipientsLoader);
		getSupportLoaderManager().initLoader(CALL_HISTORY_LOADER_ID, null, callHistoryLoader);
		getSupportLoaderManager().initLoader(SMS_LOADER_ID, null, smsLoader);
		
		mTabHost.setCurrentTab(0);
		
		/** Create service */
		if(!ServiceTool.isServiceRunning(getApplicationContext(), SmsReceiverService.class.getName())) {
			Intent smsReceiverService = new Intent(this, SmsReceiverService.class);
			startService(smsReceiverService);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mTabHost.setCurrentTab(0);
	    super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    /** Handle item selection */
	    switch (item.getItemId()) {
	        case R.id.action_about:
	            Intent intent = new Intent(this, AboutDialogActivity.class);
	            startActivity(intent);
	            return true;
	        case R.id.action_send_log:
	        	SendEmailUtil.sendEmail(this);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		getSupportLoaderManager().destroyLoader(CONTACT_LOADER_ID);
		getSupportLoaderManager().destroyLoader(RECIPIENTS_LOADER_ID);
		getSupportLoaderManager().destroyLoader(CALL_HISTORY_LOADER_ID);
		getSupportLoaderManager().destroyLoader(SMS_LOADER_ID);
		contactsLoader = null;
		recipientsLoader = null;
		callHistoryLoader = null;
		smsLoader = null;
	}
}
