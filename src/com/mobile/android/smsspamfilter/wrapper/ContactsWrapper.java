package com.mobile.android.smsspamfilter.wrapper;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactsWrapper {

	public static boolean isFromContacts(Context context, String phone) {
		boolean isFromContacts = false;
		String[] projection = new String[] { Phone.NUMBER };
		// Было Phone.NUMBER стало Phone.NORMALIZED_NUMBER  - потому что андроид сам приведёт к виду +7 вместо 8
		//String selection = "REPLACE( " + Phone.NUMBER + ", ' ', '')" + "= '" + phone + "'";
		String selection = "REPLACE( " + Phone.NORMALIZED_NUMBER + ", ' ', '')" + "= '" + phone + "'";
		
		Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, null,
				null);
		while (cursor.moveToNext()) {
			isFromContacts = true;
		}
		cursor.close();
		return isFromContacts;
	}
}
