package com.mobile.android.smsspamfilter.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;

public final class StorageUtils {

	public static void saveObjectToSharedPrefs(Context context, SharedPreferences preferences, String name, Object obj) {
		Editor editor = preferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		editor.putString(name, json);
		editor.commit();
	}

	public static List<BlockItem> loadBlockedListFromSharedPrefs(Context context, SharedPreferences preferences, String name) {
		String json = preferences.getString(name, "");
		Gson gson = new Gson();
		List<BlockItem>  list = gson.fromJson(json, new TypeToken<List<BlockItem>>() {
		}.getType());
		if(list != null)
			return list;
		else
			return new ArrayList<BlockItem>();
	}
	
	//-------------------------------------------------------------------------------------------------------------------------
	
	public static void saveAllowedList(Context context, SharedPreferences prefs, List<BlockItem> allowedList) {
		saveObjectToSharedPrefs(context, prefs, Constants.INTERNAL_ALLOWED_LIST, allowedList);
	}
	
	public static void restoreAllowedList(Context context, SharedPreferences prefs) {
		List<BlockItem> list = (List<BlockItem>) loadBlockedListFromSharedPrefs(context, prefs, Constants.INTERNAL_ALLOWED_LIST);
		BlockedListWrapper.deleteAll(context, DataProvider.ALLOWED_CONTENT_URI);
		for(BlockItem item : list)
			BlockedListWrapper.insertBlockedItem(context.getContentResolver(), DataProvider.ALLOWED_CONTENT_URI, item.isChecked(), item.getType()==0 ? false : true, item.getValue());
	}
	
	public static void saveBlockedList(Context context, SharedPreferences prefs, List<BlockItem> blockedList) {
		saveObjectToSharedPrefs(context, prefs, Constants.INTERNAL_BLOCKED_LIST, blockedList);
	}
	
	public static void restoreBlockedList(Context context, SharedPreferences prefs) {
		List<BlockItem> list = (List<BlockItem>) loadBlockedListFromSharedPrefs(context, prefs, Constants.INTERNAL_BLOCKED_LIST);
		BlockedListWrapper.deleteAll(context, DataProvider.BLOCKED_CONTENT_URI);
		for(BlockItem item : list)
			BlockedListWrapper.insertBlockedItem(context.getContentResolver(), DataProvider.BLOCKED_CONTENT_URI, item.isChecked(), item.getType()==0 ? false : true, item.getValue());
	} 
}
