package com.mobile.android.smsspamfilter.log;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.fragments.UserDialog;
import com.mobile.android.smsspamfilter.utils.FileUtils;

public class SendEmailUtil {

	public static void sendEmail(Context context) {
		String subject = context.getString(R.string.support_email_subject);
		String path = getPath(context);

		File attach = getFile(path);
		if (attach == null) {
			showFileNotFoundDialog(context);
		} else {
			String body;
			try {
				body = FileUtils.getStringFromFile(path);
				EmailController.sendEmail(context, "", subject, body, Uri.parse(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void showFileNotFoundDialog(Context context) {
		UserDialog.showInfoDialog(context, R.string.msg_file_is_not_available);
	}
	
	private static String getPath(Context context){
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String path = prefs.getString("ERROR_REPORT_PATH", "");
		Log.i(Constants.TAG, path);
		return path;
	}
	
	private static File getFile(String path){
		if(path.length() > 0){
        	return new File(path);
        }
        return null;
	}
}
