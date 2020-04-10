package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.adapter.ExpandableSmsListAdapter;
import com.mobile.android.smsspamfilter.wrapper.SmsListWrapper;

public class FromSmsBaseDialog extends Dialog {
	public static final int FROM_SMS_BODY_DIALOG = 0;
	public static final int FROM_SMS_PHONE_DIALOG = 1;
	
	private ExpandableSmsListAdapter mAdapter;
	private ExpandableListView list;
	private Context mContext;

	public FromSmsBaseDialog(Context context) {
		super(context);
		mContext = context;
		
		setContentView(R.layout.from_sms_dialog);
		setTitle(R.string.block_spam_time);
		
		list = (ExpandableListView) findViewById(R.id.expandable_sms_list);
		
		Button buttonCancel = (Button) findViewById(R.id.btn_cancel);
		buttonCancel.setOnClickListener(listener);
		
		/** Load groups in cache */
		SmsListWrapper.loadData(context.getApplicationContext());		
	}
	
	android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
			}
		}
	};
	
	public void setAdapter(ExpandableSmsListAdapter adapter){
		mAdapter = adapter;
        list.setAdapter(mAdapter);
        mAdapter.setDialog(this);
	}
}
