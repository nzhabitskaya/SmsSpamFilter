package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.Sms;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;
import com.mobile.android.smsspamfilter.wrapper.SpamListWrapper;

public class AllowDialog extends Dialog {
	private String phone;
	private String message;
	private long time;
	private Sms item;
	
	private Context mContext;

	public AllowDialog(Context context) {
		super(context);
		setContentView(R.layout.allow_dialog);
		setTitle(R.string.title_selection);
		mContext = context;

		TextView buttonDelete = (TextView) findViewById(R.id.delete);
		buttonDelete.setOnClickListener(listener);
		TextView buttonInsert = (TextView) findViewById(R.id.insert_in_sms_list);
		buttonInsert.setOnClickListener(listener);	
		TextView buttonAllowNumber = (TextView) findViewById(R.id.allow_number);
		buttonAllowNumber.setOnClickListener(listener);
		TextView buttonAllowText = (TextView) findViewById(R.id.allow_text);
		buttonAllowText.setOnClickListener(listener);
	}

	android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.delete:
				SpamListWrapper.deleteSpamItem(mContext, item.getId());
				dismiss();
				break;
			case R.id.insert_in_sms_list:
				restoreSms();
				dismiss();
				break;
			case R.id.allow_number:
				BlockedListWrapper.insertBlockedItem(mContext.getApplicationContext().getContentResolver(), DataProvider.ALLOWED_CONTENT_URI, true, true, item.getPhone());
				dismiss();
				break;
			case R.id.allow_text:
				BlockedListWrapper.insertBlockedItem(mContext.getApplicationContext().getContentResolver(), DataProvider.ALLOWED_CONTENT_URI, true, false, item.getMessage());
				dismiss();
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
	}
	
	public void setItem(Sms item){
		this.phone = item.getPhone();
		this.message = item.getMessage();
		this.time = item.getTimestamp();
		this.item = item;
	}
	
	public void restoreSms() {
	    try {
	        ContentValues values = new ContentValues();
	        values.put("address", phone);
	        values.put("body", message);
	        values.put("read", false);
	        values.put("date", time);
	        mContext.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	    } catch (Exception ex) {
	    }
	}
}