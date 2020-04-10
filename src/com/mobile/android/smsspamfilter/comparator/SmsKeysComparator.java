package com.mobile.android.smsspamfilter.comparator;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.mobile.android.smsspamfilter.data.Sms;

public class SmsKeysComparator implements Comparator<String>{
	private Map<String, List<Sms>> mGroups;
	
	public SmsKeysComparator(Map<String, List<Sms>>groups){
		mGroups = groups;
	}

	@Override
	public int compare(String key1, String key2) {
		long timestamp1 = 0;
		long timestamp2 = 0;
		List<Sms> group1 = mGroups.get(key1);
		List<Sms> group2 = mGroups.get(key2);
		
		if(group1 != null && group1.size() > 0)
			timestamp1 = mGroups.get(key1).get(0).getTimestamp();
		if(group2 != null && group2.size() > 0)
			timestamp2 = mGroups.get(key2).get(0).getTimestamp();
		
		if(timestamp1 > timestamp2)
			return -1;
		else
			return 1;
	}
}
