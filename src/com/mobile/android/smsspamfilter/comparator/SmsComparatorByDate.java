package com.mobile.android.smsspamfilter.comparator;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.util.Comparator;

import com.mobile.android.smsspamfilter.data.Sms;

public class SmsComparatorByDate implements Comparator<Sms>{

	@Override
	public int compare(Sms sms1, Sms sms2) {
		long timestamp1 = sms1.getTimestamp();
		long timestamp2 = sms2.getTimestamp();
		if(timestamp1 > timestamp2)
			return -1;
		else
			return 1;
	}
}
