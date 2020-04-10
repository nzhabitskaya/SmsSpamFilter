package com.mobile.android.smsspamfilter.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmailLogManager {
	public static final String DEFAULT_LOG_FILE_NAME = "log.txt";
	public static final String DEFAULT_ZIP_FILE_NAME = "log.zip";

	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss:SSS";

	private final StringBuffer stringBuffer;

	private final SimpleDateFormat sdf;

	private EmailLogManager() {
		stringBuffer = new StringBuffer();
		sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	}

	private static class SingletonHolder {
		public static EmailLogManager instance = new EmailLogManager();
	}

	public static EmailLogManager getInstance() {
		return SingletonHolder.instance;
	}

	private void append(String type, String tag, String message) {
		stringBuffer.append(getCurrentTime() + " " + type + " " + tag + " " + message + "\n");
	}

	public void e(String tag, String message) {
		append("e", tag, message);
		//Log.e(tag, message);
	}

	public void d(String tag, String message) {
		append("d", tag, message);
		//Log.d(tag, message);
	}

	public void i(String tag, String message) {
		append("i", tag, message);
		//Log.i(tag, message);
	}

	public String getLog() {
		return stringBuffer.toString();
	}

	private String getCurrentTime() {
		Date currentDate = new Date(System.currentTimeMillis());
		return sdf.format(currentDate);
	}

	public String[] getAdbLogCat() {
		try {
			Process p = Runtime.getRuntime().exec("/path/to/adb shell logcat");
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			final StringBuffer output = new StringBuffer();
			String line;
			ArrayList<String> arrList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				output.append(line);
			}
			return (String[]) arrList.toArray(new String[0]);
		} catch (IOException e) {
			System.err.println(e);
			e.printStackTrace();
			return new String[] {};
		}
	}
}
