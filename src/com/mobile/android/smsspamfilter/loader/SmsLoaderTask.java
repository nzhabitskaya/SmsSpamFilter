package com.mobile.android.smsspamfilter.loader;

import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.TextBasedSmsColumns;
import android.support.v4.content.AsyncTaskLoader;

import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.data.DataManager;

public class SmsLoaderTask extends AsyncTaskLoader<Map<Long, String>> {
	private Context mContext;
	private Map<Long, String> mList;
	private Uri uri = Constants.SMS_URI;

	public SmsLoaderTask(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public Map<Long, String> loadInBackground() {
		Cursor cursor = mContext.getContentResolver().query(uri, new String[] { TextBasedSmsColumns.THREAD_ID, TextBasedSmsColumns.ADDRESS }, null, null, null);
		
		while (cursor.moveToNext()) {
			long threadID = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.THREAD_ID));
            String phone = cursor.getString(cursor.getColumnIndex(TextBasedSmsColumns.ADDRESS));	
            DataManager.getInstance().insertPhoneByThreadID(threadID, phone);
		}
		cursor.close();
		return null;
	}

	@Override
	public void deliverResult(Map<Long, String> listOfData) {
		if (isReset()) {
			if (listOfData != null) {
				onReleaseResources(listOfData);
			}
		}
		Map<Long, String> oldApps = listOfData;
		mList = listOfData;

		if (isStarted()) {
			super.deliverResult(listOfData);
		}

		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mList != null) {
			deliverResult(mList);
		}

		if (takeContentChanged() || mList == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(Map<Long, String> apps) {
		super.onCanceled(apps);
		onReleaseResources(apps);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();

		if (mList != null) {
			onReleaseResources(mList);
			mList = null;
		}
	}


	protected void onReleaseResources(Map<Long, String> apps) {
	}
}
