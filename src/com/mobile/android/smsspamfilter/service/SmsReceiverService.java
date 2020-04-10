package com.mobile.android.smsspamfilter.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.mobile.android.smsspamfilter.receiver.SmsReceiver;

public class SmsReceiverService extends Service {
	// Incoming sms receiver
	private SmsReceiver smsReceiver;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// Register sms receiver
		smsReceiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(2147483647);
		registerReceiver(smsReceiver, filter);
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister sms receiver when activity is destroyed
		unregisterReceiver(smsReceiver);
		smsReceiver = null;
	}
}
