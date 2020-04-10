package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.widget.ArrayAdapter;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.adapter.ContactsArrayAdapter;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.Call;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;

public class FromContactsDialog extends AlertDialog.Builder implements OnClickListener {
	private ContactsArrayAdapter mAdapter;
	private ArrayAdapter<BlockItem> listAdapter;
	private Uri contentUri;
	private Context mContext;

	public FromContactsDialog(Context context, ArrayAdapter<BlockItem> listAdapter, Uri contentUri) {
		super(context);
		this.contentUri = contentUri;
		mContext = context;
		
		this.listAdapter = listAdapter;
		setTitle(R.string.from_contacts);
		setNegativeButton(R.string.cancel, null);
	    
	    mAdapter = new ContactsArrayAdapter(context);
	    setAdapter(mAdapter, this);
	    getContactList();
	}

	@Override
	public void onClick(DialogInterface arg0, int id) {
		Call call = DataManager.getInstance().getContacts().get(id);
		
		String phone = call.getPhone();
		BlockedListWrapper.insertBlockedItem(mContext.getApplicationContext().getContentResolver(), contentUri, true, true, phone);
		listAdapter.notifyDataSetChanged();
	}
	
	private void getContactList() {
		List<Call> data = DataManager.getInstance().getContacts();
		mAdapter.setData(data);
		mAdapter.notifyDataSetChanged();
	}
}
