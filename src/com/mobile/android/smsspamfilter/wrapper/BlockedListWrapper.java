package com.mobile.android.smsspamfilter.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.database.Result1Database;


public class BlockedListWrapper {
	private static Context mContext;
	private Uri contentUri;

	public BlockedListWrapper(Context context, Uri contentUri) {
		mContext = context;
		this.contentUri = contentUri;
	}

	public int getCount() {
		int i = 0;
	String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		Cursor cursor = mContext.getContentResolver().query(contentUri, projection, null, null, null);
		while (cursor.moveToNext()) {
			i++;
		}
		cursor.close();
		return i;
	}
	
	public BlockItem getItem(int pos) {
		String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		String orderBy = Constants.VALUE + " ASC";
		Cursor cursor = mContext.getContentResolver().query(contentUri, projection, null, null, orderBy);//это выбор из базы

		List<Result1Database> result1=new  ArrayList<Result1Database>();
		int i=0;
		if (cursor.moveToFirst()) {
			do {
				
				long id = cursor.getLong(cursor.getColumnIndex(Constants.BLOCKED_ID));
				int isChecked =cursor.getInt(cursor.getColumnIndex(Constants.IS_CHECKED));
				int type = cursor.getInt(cursor.getColumnIndex(Constants.IS_NUMBER));	
				String value =cursor.getString(cursor.getColumnIndex(Constants.VALUE));
				i++;
				int dlina=value.length();
				if(dlina>0)
				{
					
					char c = value.charAt(0);
					if(c=='+')
					{
						
					
					value = value.substring(1,value.length() - 1);
					}

				}
				
				if(value != null)	
					value = value.replaceAll("''","'");
			    result1.add(new Result1Database(id,isChecked,type,value));
				
			} while (cursor.moveToNext());
					
		}
		
		  Collections.sort(result1,new Result1Database.SortByName());
		
		
		int size=result1.size();
		if(pos<size)
		{
			Result1Database result1database=result1.get(pos);
			
			long id =result1database.getId();
			int isChecked = result1database.getIsChecked();
			int type = result1database.getType();
			String value = result1database.getValue();
			
			cursor.close();
			return new BlockItem(id, type, isChecked == 0 ? false : true, value);
			
		}
		/*
		if (cursor.moveToPosition(pos)) {
			long id = cursor.getLong(cursor.getColumnIndex(Constants.BLOCKED_ID));
			int isChecked =cursor.getInt(cursor.getColumnIndex(Constants.IS_CHECKED));
			int type = cursor.getInt(cursor.getColumnIndex(Constants.IS_NUMBER));
			String value = cursor.getString(cursor.getColumnIndex(Constants.VALUE));
			if(value != null)	
				value = value.replaceAll("''","'");
			cursor.close();
			return new BlockItem(id, type, isChecked == 0 ? false : true, value);
		}
		*/
		
			
		cursor.close();
/*	*/	return null;
	}
	
	public static boolean isTextAllowed(Context context, String value) {
		return isTextAllowed(context, DataProvider.ALLOWED_CONTENT_URI, value);
	}

	public static boolean isNumberAllowed(Context context, String value) {
		return isNumberAllowed(context, DataProvider.ALLOWED_CONTENT_URI, value);
	}
	
	public static boolean isTextBlocked(Context context, String value) {
		return isTextAllowed(context, DataProvider.BLOCKED_CONTENT_URI, value);
	}

	public static boolean isNumberBlocked(Context context, String value) {
		return isNumberAllowed(context, DataProvider.BLOCKED_CONTENT_URI, value);
	}

	private static boolean isTextAllowed(Context context, Uri contentUri, String value) {
		String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		String selection = Constants.IS_CHECKED + "='1'" + " AND " + Constants.IS_NUMBER + "='0'" + " AND " + Constants.VALUE + "='"
				+ value + "'";
		Cursor cursor = context.getContentResolver().query(contentUri, projection, selection, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	private static boolean isNumberAllowed(Context context, Uri contentUri, String value) {
		String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		String selection = Constants.IS_CHECKED + "='1'" + " AND " + Constants.IS_NUMBER + "='1'" + " AND " + Constants.VALUE + "='"
				+ value + "'";
		Cursor cursor = context.getContentResolver().query(contentUri, projection, selection, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}
	
	public static boolean isMatch(Context context, Uri contentUri, String phone, String smsBody) {
		boolean isMatch = false;
		String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		String selection = Constants.IS_CHECKED + "='1'";
		Cursor cursor = context.getContentResolver().query(contentUri, projection, selection, null, null);
		while (cursor.moveToNext()) {
			int type = cursor.getInt(cursor.getColumnIndex(Constants.IS_NUMBER));
			String pattern = cursor.getString(cursor.getColumnIndex(Constants.VALUE));			
			switch(type){
			case 0:
				if(isMatchToPattern(pattern, smsBody)){
					isMatch = true;
					break;
				}
			case 1:
				if(isMatchToPattern(pattern, phone)){
					isMatch = true;
					break;
				}	
			}
		}
		cursor.close();
		return isMatch;
	}
	
	private static boolean isMatchToPattern(String pattern, String smsBody) {
		boolean isMatch = false;
		String patternWithoutSpecialSymbols = pattern.replace('+', ' ').replace('{', ' ').replace('}', ' ').replace('*', ' ').replace('?', ' ').replace('[', ' ').replace(']', ' ');
		Pattern pat = Pattern.compile(patternWithoutSpecialSymbols);
		Matcher matcher = pat.matcher(smsBody);
		if(matcher.find()){
			isMatch = true;
		}
		return isMatch;
	}

	public void insertBlockedItem(boolean isChecked, boolean isNumber, String value) {
	
		ContentValues cv = new ContentValues();
		cv.put(Constants.IS_CHECKED, isChecked);
		cv.put(Constants.IS_NUMBER, isNumber);
		cv.put(Constants.VALUE, value);
		mContext.getApplicationContext().getContentResolver().insert(contentUri, cv);
		
	//	Toast.makeText(mContext,"???  "+contentUri,Toast.LENGTH_SHORT).show();
	

	}

	public static void insertBlockedItem(ContentResolver contentResolver, Uri contentUri, boolean isChecked, boolean isNumber, String value) {
	
		String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		Cursor cursor = contentResolver.query(contentUri,
				projection, null, null, null);	
		int i=0;
		if (cursor.moveToFirst()) {
			do {
				if(value.equals(cursor.getString(3))) {
				i++;
				}
				
				} while (cursor.moveToNext());
		}
		
		
	
		if(cursor != null && !cursor.isClosed()) {
			
			cursor.close();
		}	
		 
		
		if(i>0){
			
			Toast.makeText(mContext,"“ака€ запись уже есть в списке!",Toast.LENGTH_SHORT).show();	
			
		} else
		{
			ContentValues cv = new ContentValues();
			cv.put(Constants.IS_CHECKED, isChecked);
			cv.put(Constants.IS_NUMBER, isNumber);
			cv.put(Constants.VALUE, value);
			contentResolver.insert(contentUri, cv);	
		}
		}

	public static void deleteBlockedItem(Context context, Uri contentUri, long id) {
		Uri uri = ContentUris.withAppendedId(contentUri, id);
		context.getContentResolver().delete(uri, null, null);
	}
	
	public static void deleteAll(Context context, Uri contentUri) {
		context.getContentResolver().delete(contentUri, null, null);
	}

	public void updateBlockedItem(long id, boolean isChecked) {
		Uri uri = ContentUris.withAppendedId(contentUri, id);
		ContentValues cv = new ContentValues();
		cv.put(Constants.IS_CHECKED, isChecked);
		mContext.getContentResolver().update(uri, cv, null, null);
	}

	public static void updateBlockedItem(ContentResolver contentResolver, Uri contentUri, long id, boolean isNumber, String value) {
		Uri uri = ContentUris.withAppendedId(contentUri, id);
		ContentValues cv = new ContentValues();
		cv.put(Constants.IS_NUMBER, isNumber);
		cv.put(Constants.VALUE, value);
		contentResolver.update(uri, cv, null, null);
	}
	
	public static List<BlockItem> exportList(ContentResolver contentResolver, Uri contentUri){
		List<BlockItem> list = new ArrayList<BlockItem>();
		
		String[] projection = new String[] { Constants.BLOCKED_ID, Constants.IS_CHECKED, Constants.IS_NUMBER, Constants.VALUE };
		Cursor cursor = contentResolver.query(contentUri,
				projection, null, null, null);
		while (cursor.moveToNext()) {
			long id = Long.parseLong(cursor.getString(cursor
					.getColumnIndex(Constants.BLOCKED_ID)));
			boolean isChecked = cursor.getInt(cursor
					.getColumnIndex(Constants.IS_CHECKED))==0 ? false : true;
			int type = cursor.getInt(cursor
					.getColumnIndex(Constants.IS_NUMBER));
			String value = cursor.getString(cursor
					.getColumnIndex(Constants.VALUE));
			list.add(new BlockItem(id, type, isChecked, value));
		}
		cursor.close();
		return list;
	}
	
	public static void importList(ContentResolver contentResolver, Uri contentUri, List<BlockItem> list){
		contentResolver.delete(contentUri, Constants.BLOCKED_ID + " not null ", null);
		for(BlockItem item : list){
			insertBlockedItem(contentResolver, contentUri, item.isChecked(), item.getType()==0 ? false : true, item.getValue());
		}
	}
}