package com.mobile.android.smsspamfilter.wrapper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony.TextBasedSmsColumns;
import android.util.Log;

import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.data.Sms;

public class SpamListWrapper {
	static final String[] PROJECTION = new String[] { BaseColumns._ID, TextBasedSmsColumns.THREAD_ID, TextBasedSmsColumns.ADDRESS, TextBasedSmsColumns.BODY, TextBasedSmsColumns.DATE };
	private Context mContext;
	private List<Sms> groupList;
	private List<Sms> childList;

	public SpamListWrapper(Context context) {
		mContext = context;
	}

	public int getGroupCount() {
		return getGroups().size();
	}

	public int getChildrenCount(int groupPosition) {
		return getChildren(getGroup(groupPosition).getPhone()).size() - 1;
	}

	public Sms getGroup(int groupPosition) {
		return getGroups().get(groupPosition);
	}

	public Sms getChild(int groupPosition, int childPosition) {
		return getChildren(getGroup(groupPosition).getPhone()).get(childPosition + 1);
	}

	public List<Sms> getGroups() {
		groupList = new ArrayList<Sms>();
		String selection = BaseColumns._ID + " not null " + " GROUP BY " + TextBasedSmsColumns.ADDRESS;
		String sortOrder = TextBasedSmsColumns.DATE + " DESC";
		Cursor cursor = mContext.getContentResolver().query(DataProvider.SPAM_CONTENT_URI, PROJECTION, selection, null, sortOrder);
		while (cursor.moveToNext()) {
			Sms sms = readSpam(cursor);
			groupList.add(sms);
		}
		cursor.close();
		return groupList;
	}

	public List<Sms> getChildren(String address) {
		childList = new ArrayList<Sms>();
		String selection = TextBasedSmsColumns.ADDRESS + "='" + address + "'";
		String sortOrder = TextBasedSmsColumns.DATE + " DESC";
		Cursor cursor = mContext.getContentResolver().query(DataProvider.SPAM_CONTENT_URI, PROJECTION, selection, null, sortOrder);
		while (cursor.moveToNext()) {
			Sms sms = readSpam(cursor);
			childList.add(sms);
		}
		cursor.close();
		return childList;
	}
	
	private static Sms readSpam(Cursor cursor){
		long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
		long threadID = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.THREAD_ID));
		String phone = cursor.getString(cursor.getColumnIndex(TextBasedSmsColumns.ADDRESS));
		if(phone != null)
			phone = phone.replaceAll("''","'");
		String message = cursor.getString(cursor.getColumnIndex(TextBasedSmsColumns.BODY));
		if(message != null)
			message = message.replaceAll("''","'");
		long timestamp = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.DATE));
		boolean isDraft = false;
		return new Sms(id, threadID, phone, message, timestamp, isDraft);
	}

	public static Uri insertSpamItem(Context context, String smsAddress, String smsBody, long smsTime) {
		ContentValues cv = new ContentValues();
		cv.put(TextBasedSmsColumns.ADDRESS, smsAddress);
		cv.put(TextBasedSmsColumns.BODY, smsBody);
		cv.put(TextBasedSmsColumns.DATE, smsTime);
		return context.getContentResolver().insert(DataProvider.SPAM_CONTENT_URI, cv);
	}
	
	public static void updateSpamItem(Context context, Uri contentUri, long id, String phone) {
		ContentValues cv = new ContentValues();
		long threadID = getThreadID(id, phone);
		cv.put(TextBasedSmsColumns.THREAD_ID, threadID);
		context.getContentResolver().update(contentUri, cv, null, null);
	}
	
	private static long getThreadID(long id, String phone){
		long threadID = id;
		if(DataManager.getInstance().isExistThreadID(phone))
			threadID = DataManager.getInstance().getThreadIdByPhone(phone);
		else
			DataManager.getInstance().insertPhoneByThreadID(threadID, phone);
		return threadID;
	}

	public static void deleteSpamItem(Context context, long id) {
		Uri uri = ContentUris.withAppendedId(DataProvider.SPAM_CONTENT_URI, id);
		context.getContentResolver().delete(uri, null, null);
	}
}
