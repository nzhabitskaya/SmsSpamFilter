package com.mobile.android.smsspamfilter.loader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.content.AsyncTaskLoader;

import com.mobile.android.smsspamfilter.data.Call;
import com.mobile.android.smsspamfilter.data.DataManager;

public class CallHistoryLoaderTask extends AsyncTaskLoader<List<Call>> {
	private Context mContext;
	private Set<String> keys;
	private List<Call> mModels;
	private Uri uri = CallLog.Calls.CONTENT_URI;

	public CallHistoryLoaderTask(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public List<Call> loadInBackground() {
		keys = new HashSet<String>();
		mModels = new ArrayList<Call>();
		String[] projection = new String[] { CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.DATE };
		String sortOrder = " _ID DESC";

		Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, sortOrder);
		
		Call item;
		while (cursor.moveToNext()) {
			String from = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
			String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
			if(name == null)
				name = DataManager.getInstance().getAddressByPhone(from);
			String time = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
			
			item = new Call(name, from, time);
			if(!keys.contains(from)){
				keys.add(from);
				mModels.add(item);
			}
		}
		cursor.close();
		return mModels;
	}

	@Override
	public void deliverResult(List<Call> listOfData) {
		if (isReset()) {
			if (listOfData != null) {
				onReleaseResources(listOfData);
			}
		}
		List<Call> oldApps = listOfData;
		mModels = listOfData;

		if (isStarted()) {
			super.deliverResult(listOfData);
		}

		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mModels != null) {
			deliverResult(mModels);
		}

		if (takeContentChanged() || mModels == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(List<Call> apps) {
		super.onCanceled(apps);
		onReleaseResources(apps);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();

		if (mModels != null) {
			onReleaseResources(mModels);
			mModels = null;
		}
	}

	protected void onReleaseResources(List<Call> apps) {
	}
}
