package com.mobile.android.smsspamfilter.activities;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mobile.android.smsspamfilter.log.CrashReportHandler;
import com.mobile.android.smsspamfilter.log.EmailLogManager;

public class LogActivity extends FragmentActivity implements LoggedActivity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		/** Attach crash log */
		CrashReportHandler.attach(this);
	}
	
		public EmailLogManager getLog() {
		return EmailLogManager.getInstance();
	}
}
