package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.Constants;

public class SelectTimeDialog extends Dialog{
	private Context mContext;
	private SharedPreferences settings;
	private TimePicker timePicker1;
	private TimePicker timePicker2;
	private TextView timeLabel;
	
	private int hourFrom = 0;
	private int minuteFrom = 0;
	private int hourTill = 0;
	private int minuteTill = 0;
	private String timeString = "";

	public SelectTimeDialog(Context context, SharedPreferences settings, TextView timeLabel) {
		super(context);
		mContext = context;
		this.settings = settings;
		this.timeLabel = timeLabel;
		setContentView(R.layout.select_time_dialog);
		setTitle(R.string.block_spam_time);
		
		timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
		timePicker2 = (TimePicker) findViewById(R.id.timePicker2);
		timePicker1.setIs24HourView(true);
		timePicker2.setIs24HourView(true);
		
		int hourFrom = settings.getInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_FROM, 0);
		int minuteFrom = settings.getInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_FROM, 0);
		int hourTill = settings.getInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_TILL, 0);
		int minuteTill = settings.getInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_TILL, 0);
		
		timePicker1.setCurrentHour(hourFrom);
		timePicker1.setCurrentMinute(minuteFrom);
		timePicker2.setCurrentHour(hourTill);
		timePicker2.setCurrentMinute(minuteTill);
		
		Button buttonOk = (Button) findViewById(R.id.btn_ok);
		buttonOk.setOnClickListener(listener);
		Button buttonCancel = (Button) findViewById(R.id.btn_cancel);
		buttonCancel.setOnClickListener(listener);
	}
	
	android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_ok:
				Editor editor = settings.edit();
				
				hourFrom = timePicker1.getCurrentHour();
				minuteFrom = timePicker1.getCurrentMinute();
				hourTill = timePicker2.getCurrentHour();
				minuteTill = timePicker2.getCurrentMinute();
				timeString = setTimePeriod();
				
				editor.putInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_FROM, hourFrom);
				editor.putInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_FROM, minuteFrom);
				editor.putInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_TILL, hourTill);
				editor.putInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_TILL, minuteTill);
				editor.putString(Constants.PREF_BLOCK_SPAM_TIME_STRING, timeString);
				
				editor.commit();
				timeLabel.setText(timeString);
				dismiss();
				break;
			case R.id.btn_cancel:
				dismiss();
				break;
			}
		}
	};
	
	private String setTimePeriod(){
		String hourFromStr = String.valueOf(hourFrom);
		String minuteFromStr = String.valueOf(minuteFrom);
		String hourTillStr = String.valueOf(hourTill);
		String minuteTillStr = String.valueOf(minuteTill);
		
		if(hourFromStr.length() == 1)
			hourFromStr = '0' + hourFromStr;
		if(minuteFromStr.length() == 1)
			minuteFromStr = '0' + minuteFromStr;
		if(hourTillStr.length() == 1)
			hourTillStr = '0' + hourTillStr;
		if(minuteTillStr.length() == 1)
			minuteTillStr = '0' + minuteTillStr;
		
		return mContext.getString(R.string.spam_time, hourFromStr, minuteFromStr, hourTillStr, minuteTillStr);
	}
}

