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
import com.mobile.android.smsspamfilter.adapter.CallHistoryArrayAdapter;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.Call;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;

public class FromCallHistoryDialog extends AlertDialog.Builder implements OnClickListener {
	private CallHistoryArrayAdapter mAdapter;
	private ArrayAdapter<BlockItem> listAdapter;
	private Uri contentUri;
	private Context mContext;

	public FromCallHistoryDialog(Context context, ArrayAdapter<BlockItem> listAdapter, Uri contentUri) {
		super(context);
		this.contentUri = contentUri;
		mContext = context;
		
		this.listAdapter = listAdapter;
		setTitle(R.string.from_call_history);
		setNegativeButton(R.string.cancel, null);
	    
		mAdapter = new CallHistoryArrayAdapter(context);
	    setAdapter(mAdapter, this);
	    setCallList();
	}

	@Override
	public void onClick(DialogInterface arg0, int id) {
		String phone = DataManager.getInstance().getCalls().get(id).getPhone();
		BlockedListWrapper.insertBlockedItem(mContext.getApplicationContext().getContentResolver(), contentUri, true, true, phone);
		listAdapter.notifyDataSetChanged();
	}
	
	private void setCallList() {
		List<Call> data = DataManager.getInstance().getCalls();
		mAdapter.setData(data);
		mAdapter.notifyDataSetChanged();
	}
}