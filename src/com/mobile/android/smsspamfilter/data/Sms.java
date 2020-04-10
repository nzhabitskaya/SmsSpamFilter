package com.mobile.android.smsspamfilter.data;
import java.io.Serializable;

import com.mobile.android.smsspamfilter.utils.DateUtils;


public class Sms implements Serializable, Comparable {
	private static final long serialVersionUID = 6417719419544597500L;
	private long id;
	private long threadID;
	private String phone;
	private String message;
	private long time;
	private boolean isDraft;
	
	public Sms() {
		
	}
	
	public Sms(long id, long threadID, String name, String message, long time, boolean isDraft) {
		this.id = id;
		this.threadID = threadID;
		this.phone = name;
		this.message = message;
		this.time = time;
		this.isDraft = isDraft;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return DateUtils.convertTime(String.valueOf(time));
	}
	
	public long getTimestamp() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isDraft() {
		return isDraft;
	}

	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

	public long getThreadID() {
		return threadID;
	}

	public void setThreadID(long threadID) {
		this.threadID = threadID;
	}

	@Override
	public int compareTo(Object sms) {
		long smsTime1 = getTimestamp();
		long smsTime2 = ((Sms)sms).getTimestamp();

		return smsTime1 > smsTime2 ? 1 : (smsTime1 < smsTime2 ? -1 : 0);
	}
}
