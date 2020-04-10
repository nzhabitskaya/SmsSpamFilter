package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.content.Context;
import android.net.Uri;
import android.widget.ArrayAdapter;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.adapter.ExpandableSmsListAdapter;
import com.mobile.android.smsspamfilter.data.BlockItem;

public class FromSmsNumberDialog extends FromSmsBaseDialog{
	private ExpandableSmsListAdapter adapter;
	
	public FromSmsNumberDialog(Context context, ArrayAdapter<BlockItem> blockedListAdapter, Uri contentUri) {
		super(context);
		setTitle(R.string.from_sms_number);
		adapter = new ExpandableSmsListAdapter(context, FROM_SMS_PHONE_DIALOG, blockedListAdapter, contentUri);
		setAdapter(adapter);
	}
}