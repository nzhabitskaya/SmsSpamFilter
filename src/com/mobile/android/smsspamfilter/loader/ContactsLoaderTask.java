package com.mobile.android.smsspamfilter.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.AsyncTaskLoader;

import com.mobile.android.smsspamfilter.data.Call;
import com.mobile.android.smsspamfilter.data.DataManager;

public class ContactsLoaderTask extends AsyncTaskLoader<List<Call>> {
	private Context mContext;
	private List<Call> mList;
	private Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

	public ContactsLoaderTask(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public List<Call> loadInBackground() {
		mList = new ArrayList<Call>();
		DataManager.getInstance().resetContacts();

		Cursor cursor = mContext.getContentResolver().query(uri, new String[] {Phone.DISPLAY_NAME, Phone.NUMBER}, null, null, null);
		
		while (cursor.moveToNext()) {
			String phoneName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER)).replaceAll(" ","");	
            mList.add(new Call(phoneName, phoneNumber));
		}
		cursor.close();
		return mList;
	}

	@Override
	public void deliverResult(List<Call> listOfData) {
		if (isReset()) {
			if (listOfData != null) {
				onReleaseResources(listOfData);
			}
		}
		List<Call> oldApps = listOfData;
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
	public void onCanceled(List<Call> apps) {
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


	protected void onReleaseResources(List<Call> apps) {
	}
}

