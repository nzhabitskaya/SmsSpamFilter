package com.mobile.android.smsspamfilter.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static DateFormat dateFormat = new SimpleDateFormat("MMM dd");
	private static DateFormat timeFormat = new SimpleDateFormat("yyyy MMM dd, HH:mm");
	private static Date today = new Date();

	public static String convertTime(String timestampStr){
		long timestamp = Long.parseLong(timestampStr);
		Date date = new Date(timestamp);
		
		String text = timeFormat.format(date);
		return text;
	}
}
