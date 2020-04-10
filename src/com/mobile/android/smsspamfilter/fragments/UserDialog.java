package com.mobile.android.smsspamfilter.fragments;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.mobile.android.smsspamfilter.R;

public class UserDialog {
	
	public static void showInfoDialog(Context context, int message) {
		AlertDialog mDialog = new AlertDialog.Builder(context)
	            .setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}})
	            .setCancelable(false).create();
	    mDialog.show();
	}
	
	public static void showInfoDialog(Context context, String message) {
		AlertDialog mDialog = new AlertDialog.Builder(context)
	            .setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}})
	            .setCancelable(false).create();
	    mDialog.show();
	}
}
