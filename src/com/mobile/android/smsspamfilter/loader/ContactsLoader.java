package com.mobile.android.smsspamfilter.loader;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.mobile.android.smsspamfilter.data.Call;
import com.mobile.android.smsspamfilter.data.DataManager;

public class ContactsLoader implements LoaderManager.LoaderCallbacks<List<Call>>{	
	private Context mContext;	

	public ContactsLoader(Context context) {
		mContext = context;
	}
	
	/** Loader methods for sms data */
	@Override
	public Loader<List<Call>> onCreateLoader(int arg0, Bundle arg1) {
		return new ContactsLoaderTask(mContext);
	}

	@Override
	public void onLoadFinished(Loader<List<Call>> arg0, List<Call> data) {
		DataManager.getInstance().setContacts(data);
		for(Call item : data){
			DataManager.getInstance().addContact(item);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Call>> arg0) {
	}
}

