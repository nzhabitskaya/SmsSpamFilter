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

public class FromSmsBodyDialog extends FromSmsBaseDialog{
	private ExpandableSmsListAdapter adapter;

	public FromSmsBodyDialog(Context context, ArrayAdapter<BlockItem> blockedListAdapter, Uri contenturi) {
		super(context);
		setTitle(R.string.from_sms_body);
		adapter = new ExpandableSmsListAdapter(context, FROM_SMS_BODY_DIALOG, blockedListAdapter, contenturi);
		setAdapter(adapter);
	}
}

