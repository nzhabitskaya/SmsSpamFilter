package com.mobile.android.smsspamfilter.fragments;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.adapter.ExpandableSpamListAdapter;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;

public class SpamExpandableListFragment extends Fragment {	
	private ExpandableListView listView;
	private ExpandableSpamListAdapter adapter;
	
//	private SpamContentObserver spamContentObserver;
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.spam_fragment, container, false);
		listView = (ExpandableListView) view.findViewById(R.id.expandable_sms_list);

		adapter = new ExpandableSpamListAdapter(getActivity());
        listView.setAdapter(adapter);
        
        // Register spam content observer to update UI after rows were inserted in DB
        handler = new UpdateSpamListHandler();
/*        spamContentObserver = new SpamContentObserver(handler);
        getActivity().getApplicationContext().getContentResolver().registerContentObserver(DataProvider.SPAM_CONTENT_URI, true, spamContentObserver);
  */    
		return view;
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	/*	cancelNotification(getActivity(), "spam");
		adapter.notifyDataSetChanged();
*/	}
	
	public static void cancelNotification(Context ctx, String tag) {
	/*	String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
	    nMgr.cancel(12345);
*/	}
	
	/**
	 * Update adapter after data was added in DB
	 */
	class SpamContentObserver extends ContentObserver {
		Handler mHandler;
		
		public SpamContentObserver(Handler handler) {
			super(handler);
			mHandler = handler;
		}

		@SuppressLint("Override")
		@Override
		public void onChange(boolean selfChange) {
			this.onChange(selfChange, null);
		}

		@SuppressLint("Override")
		public void onChange(boolean selfChange, Uri uri) {
			mHandler.sendEmptyMessage(0);
		}
	}
	
	/**
	 * Handler to update UI
	 */
	class UpdateSpamListHandler extends Handler{
		
		public void handleMessage(android.os.Message msg) {
	        adapter.notifyDataSetChanged();
	      }
	}
	
	public void onDestroy(){
		super.onDestroy();
/*		getActivity().getApplicationContext().getContentResolver().unregisterContentObserver(spamContentObserver);
		spamContentObserver = null;
		handler = null;
*/	}
}

