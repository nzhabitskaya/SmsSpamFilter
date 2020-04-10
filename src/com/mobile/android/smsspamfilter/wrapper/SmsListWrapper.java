package com.mobile.android.smsspamfilter.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.Telephony.TextBasedSmsColumns;
import android.provider.Telephony.ThreadsColumns;
import android.util.Log;

import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.data.Sms;

public class SmsListWrapper {
	public static final String[] PROJECTION = new String[] { BaseColumns._ID, TextBasedSmsColumns.THREAD_ID, TextBasedSmsColumns.ADDRESS, TextBasedSmsColumns.BODY, TextBasedSmsColumns.DATE, TextBasedSmsColumns.TYPE };
	private Context mContext;
	private List<Sms> childList;

	public SmsListWrapper(Context context) {
		mContext = context;
	}

	public int getGroupCount() {
		return DataManager.getInstance().getGroups().size();
	}

	public int getChildrenCount(int groupPosition) {
		return getChildren(getGroup(groupPosition).getThreadID()).size() - 1;
	}

	public Sms getGroup(int groupPosition) {
		return DataManager.getInstance().getGroups().get(groupPosition);
	}

	public Sms getChild(int groupPosition, int childPosition) {
		return getChildren(getGroup(groupPosition).getThreadID()).get(childPosition + 1);
	}

	public static List<Sms> loadGroups(Context context) {
		List<Sms> groupList = new ArrayList<Sms>();
		
		/** Load groups from sms db */
		String selection = BaseColumns._ID + " not null )" + " GROUP BY (" + TextBasedSmsColumns.THREAD_ID;
		String sortOrder = "date" + " DESC";
		Cursor cursor = context.getContentResolver().query(Constants.SMS_URI, PROJECTION, selection, null, sortOrder);
		while (cursor.moveToNext()) {
			
			long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
			long threadID = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.THREAD_ID));
			int type = cursor.getInt(cursor.getColumnIndex(TextBasedSmsColumns.TYPE));
			String phone = getAddressesByThreadID(context, threadID);
			if(phone != null)
				phone = phone.replaceAll("''","'");
			String message = cursor.getString(cursor.getColumnIndex(TextBasedSmsColumns.BODY));
			if(message != null)
				message = message.replaceAll("''","'");
			long timestamp = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.DATE));
			boolean isDraft = (type==TextBasedSmsColumns.MESSAGE_TYPE_DRAFT);
			Sms sms = new Sms(id, threadID, phone, message, timestamp, isDraft);
			
			groupList.add(sms);
		}
		cursor.close();
		return groupList;
	}

	public List<Sms> getChildren(long threadID) {
		childList = new ArrayList<Sms>();
		String selection = TextBasedSmsColumns.THREAD_ID + "='" + threadID + "'";
		String sortOrder = "date" + " DESC";
		Cursor cursor = mContext.getContentResolver().query(Constants.SMS_URI, PROJECTION, selection, null, sortOrder);
		while (cursor.moveToNext()) {
			Sms sms = readSms(cursor);
			childList.add(sms);
		}
		cursor.close();
		return childList;
	}
	
	private static Sms readSms(Cursor cursor){
		long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
		long threadID = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.THREAD_ID));
		int type = cursor.getInt(cursor.getColumnIndex(TextBasedSmsColumns.TYPE));
		String phone = cursor.getString(cursor.getColumnIndex(TextBasedSmsColumns.ADDRESS));
		if(phone != null)
			phone = phone.replaceAll("''","'");
		String message = cursor.getString(cursor.getColumnIndex(TextBasedSmsColumns.BODY));
		if(message != null)
			message = message.replaceAll("''","'");
		long timestamp = cursor.getLong(cursor.getColumnIndex(TextBasedSmsColumns.DATE));
		boolean isDraft = (type==TextBasedSmsColumns.MESSAGE_TYPE_DRAFT);
		return new Sms(id, threadID, phone, message, timestamp, isDraft);
	}
	
	public static void loadData(Context context){
		DataManager.getInstance().setGroups(loadGroups(context));
	}
	
	private static String getAddressesByThreadID(Context context, long threadID){
		/** Load recipiens from conversations */
		String names = "";
		String[] projection = new String[] { BaseColumns._ID, ThreadsColumns.MESSAGE_COUNT, ThreadsColumns.RECIPIENT_IDS, ThreadsColumns.SNIPPET };
		String selection = BaseColumns._ID + "='" + threadID + "'";
		Cursor cursor = context.getContentResolver().query(Constants.CONVERSATIONS_URI, projection, selection, null, null);
		while (cursor.moveToNext()) {
			String recipientIDs = cursor.getString(cursor.getColumnIndexOrThrow(ThreadsColumns.RECIPIENT_IDS));
			names = getRecipientNames(recipientIDs);
		}
		cursor.close();
		return names;
	}
	
	private static String getRecipientNames(String recipientIDs){
		StringBuffer buffer = new StringBuffer();
		StringTokenizer tokenizer = new StringTokenizer(recipientIDs, " "); 
		int count = tokenizer.countTokens();
		while(tokenizer.hasMoreTokens()){
			long id = Long.parseLong(tokenizer.nextToken()); 
			String phone = DataManager.getInstance().getRecepientByID(id);
			String address = DataManager.getInstance().getAddressByPhone(phone);
			
			if(address.length() > 0 && count > 1)
				buffer.append(address);
			if(phone.length() > 0){
				if(address.length() > 0 && count > 1)
					buffer.append(" (");
				buffer.append(phone);
				if(address.length() > 0 && count > 1)
					buffer.append(")");
			}
			if(phone.length() > 0 || address.length() > 0)
				buffer.append(", ");
		}
		if(buffer.length() > 2)
			return buffer.substring(0, buffer.length() - 2);
		
		return "";
	}
}
