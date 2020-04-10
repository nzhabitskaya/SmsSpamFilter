package com.mobile.android.smsspamfilter.loader;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class RecipientsLoader implements LoaderManager.LoaderCallbacks<Map<Long, String>>{	
	private Context mContext;	

	public RecipientsLoader(Context context) {
		mContext = context;
	}
	
	/** Loader methods for sms data */
	@Override
	public Loader<Map<Long, String>> onCreateLoader(int arg0, Bundle arg1) {
		return new RecipientsLoaderTask(mContext);
	}

	@Override
	public void onLoadFinished(Loader<Map<Long, String>> arg0, Map<Long, String> data) {
	}

	@Override
	public void onLoaderReset(Loader<Map<Long, String>> arg0) {
	}
}