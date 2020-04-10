package com.mobile.android.smsspamfilter.log;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.fragments.UserDialog;

public class EmailController {

private static final Intent mIntent = new Intent(Intent.ACTION_SEND);
	
	public static void sendEmail(final Context context, String address, String subject, String body, Uri attachFile) {
		try {
			if (address == null)
				address = "";
			if (subject == null)
				subject = "";
			if (body == null)
				body = "";

			showInfoDialog(context, R.string.warning_label, R.string.send_log_warning_message);

			mIntent.setType("text/plain");
			mIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
			mIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			mIntent.putExtra(Intent.EXTRA_TEXT, body);
			mIntent.putExtra(Intent.EXTRA_STREAM, attachFile);

		} catch (ActivityNotFoundException activityNotFoundException) {
			//Log.e("EmailController", "Can't send email");
			UserDialog.showInfoDialog(context, R.string.send_log_failed);
		}
	}
	
	/**
	 * Show message than the is no email client on device.
	 * @param context
	 */
	private static void showInfoDialog(final Context context, final int title, final int message){
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(title == R.string.warning_label)
					context.startActivity(Intent.createChooser(mIntent, context.getString(R.string.support_email_chooser)));
				dialog.cancel();
			}
		}).
		show();
	}
	
}