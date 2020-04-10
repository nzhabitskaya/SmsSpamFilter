package com.mobile.android.smsspamfilter.receiver;

import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.activities.MainTabHostActivity;
import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;
import com.mobile.android.smsspamfilter.wrapper.ContactsWrapper;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;
import com.mobile.android.smsspamfilter.wrapper.SpamListWrapper;

public class SmsReceiver extends BroadcastReceiver {
	private long smsTime;
	private String smsAddress;
	private String smsBody;
	private String blockMessage = "";
	private Context mContext;
	private SharedPreferences settings;
	private SmsMessage[] messages;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;

		/** Application settings */
		settings = mContext.getSharedPreferences(Constants.SETTINGS, Context.MODE_MULTI_PROCESS);

		/** Get client phone number from Intent bundle */
		Bundle extras = intent.getExtras();

		if (extras != null) {

			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			messages = new SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}

			SmsMessage sms = messages[0];
			try {
				if (messages.length == 1 || sms.isReplace()) {
					smsBody = sms.getDisplayMessageBody();
				} else {
					StringBuilder bodyText = new StringBuilder();
					for (int i = 0; i < messages.length; i++) {
						bodyText.append(messages[i].getMessageBody());
					}
					smsBody = bodyText.toString();
				}
			} catch (Exception e) {
			}

			/** Get sms body and sender address */
			smsAddress = sms.getOriginatingAddress();
			if (smsAddress != null)
				smsAddress = smsAddress.replaceAll("'", "''").replaceAll(" ", "");
			if (smsBody != null)
				smsBody = smsBody.replaceAll("'", "''");
			smsTime = sms.getTimestampMillis();

			if (isBlock()) {
				/** Check Allowed Regexp */
				if (isRegexpAllowed()) {
					Log.e("1. Regexp allowed: ", smsAddress);
				}
				else if (isNumberAllowed()) {
					Log.e("2. Number allowed: ", smsAddress);
				}
				else if (isTextAllowed()) {
					Log.e("3. Text allowed: ", smsAddress);
				}
				else if (isRegexpBlocked()) {
					blockMessage = mContext.getString(R.string.regexp_contains_spam, smsAddress);
					Log.e("4. Regexp blocked: ", smsAddress);
					receiveSpam();
				}
				else if (isNumberBlocked()) {
					blockMessage = mContext.getString(R.string.number_contains_spam, smsAddress);
					Log.e("5. Number blocked: ", smsAddress);
					receiveSpam();
				}
				else if (isTextBlocked()) {
					blockMessage = mContext.getString(R.string.text_contains_spam, smsAddress);
					Log.e("6. Text blocked: ", smsAddress);
					receiveSpam();
				}
				else if (isFromContacts(smsAddress)) {
					Log.e("7. Number is in contacts: ", smsAddress);
				}
				else if (isNotFromContacts(smsAddress)) {
					blockMessage = mContext.getString(R.string.block_not_my_contacts, smsAddress);
					Log.e("8. Number is not in contacts: ", smsAddress);
					receiveSpam();
				}
				else if (isAllowFromAnyCid()) {
					Log.e("9. Allow from any cid: ", smsAddress);
				}
				else if (isBlockFromAnyCid()) {
					blockMessage = mContext.getString(R.string.block_from_any_cid, smsAddress);
					Log.e("10. Block from any cid: ", smsAddress);
					receiveSpam();
				}
				else if (isInTimePeriod()) {
					blockMessage = mContext.getString(R.string.spam_received_in_blocked_time_period, smsAddress);
					Log.e("11. In time period: ", smsAddress);
					receiveSpam();
				}
			}
		}
	}

	private void receiveSpam() {
		abortBroadcast();
		if (settings.getBoolean(Constants.PREF_NOTIFICATION_ON_OFF, true))
			createSpamNotification(mContext, "spam");

		Uri uri = SpamListWrapper.insertSpamItem(mContext, smsAddress, smsBody, smsTime);
		SpamListWrapper.updateSpamItem(mContext, uri, ContentUris.parseId(uri), smsAddress);
	}

	private boolean isMatch(Set<String> regexpList) {
		boolean isMatch = false;
		for (String pattern : regexpList) {
			Pattern pat = Pattern.compile(pattern);
			Matcher matcher = pat.matcher(smsBody);
			if (matcher.find()) {
				isMatch = true;
			}
		}
		return isMatch;
	}

	private void createSpamNotification(Context context, String registrationId) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(android.R.drawable.stat_notify_error, context.getString(R.string.title_spam_blocked),
				System.currentTimeMillis());
		/** Hide the notification after its selected */
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context, MainTabHostActivity.class);
		intent.putExtra("registration_id", registrationId);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, context.getString(R.string.title_spam_blocked), blockMessage/*
																											 * +
																											 * ", "
																											 * +
																											 * context
																											 * .
																											 * getString
																											 * (
																											 * R
																											 * .
																											 * string
																											 * .
																											 * msg_touch
																											 * )
																											 */, pendingIntent);

		/** Add vibration and sound */
		boolean isVibration = settings.getBoolean(Constants.PREF_VIBRATION_ON_OFF, false);
		boolean isSound = settings.getBoolean(Constants.PREF_SOUND_ON_OFF, false);
		if (isVibration) {
			notification.vibrate = new long[] { 100, 200, 100, 500 };
		}
		if (isSound) {
			notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		}
		notificationManager.notify(12345, notification);
	}

	// ----------------------- Check preference settings
	// ----------------------------
	public boolean isBlock() {
		// Log.e("0. Is blocked: ", "" +
		// settings.getBoolean(Constants.PREF_USE_SMS_BLOCK, false));
		return settings.getBoolean(Constants.PREF_USE_SMS_BLOCK, true);
	}

	public boolean isAllowFromAnyCid() {
		return settings.getBoolean(Constants.PREF_ALLOW_ANY_CID, false) && isCID();
	}

	public boolean isBlockFromAnyCid() {
		return settings.getBoolean(Constants.PREF_BLOCK_ANY_CID, true) && isCID();
	}

	public boolean isCID() {
		Pattern pat = Pattern.compile("[^+0-9]");
		Matcher matcher = pat.matcher(smsAddress);
		return matcher.find();
	}

	public boolean isNumberAllowed() {
		return BlockedListWrapper.isNumberAllowed(mContext, smsAddress);
	}

	public boolean isNumberBlocked() {
		return BlockedListWrapper.isNumberBlocked(mContext, smsAddress);
	}

	public boolean isTextAllowed() {
		return BlockedListWrapper.isTextAllowed(mContext, smsBody);
	}

	public boolean isTextBlocked() {
		return BlockedListWrapper.isTextBlocked(mContext, smsBody);
	}

	public boolean isRegexpAllowed() {
		return BlockedListWrapper.isMatch(mContext, DataProvider.ALLOWED_CONTENT_URI, smsAddress, smsBody);
	}

	public boolean isRegexpBlocked() {
		return BlockedListWrapper.isMatch(mContext, DataProvider.BLOCKED_CONTENT_URI, smsAddress, smsBody);
	}

	public boolean isInTimePeriod() {
		int hourFrom = settings.getInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_FROM, 0);
		int minuteFrom = settings.getInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_FROM, 0);
		int hourTill = settings.getInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_TILL, 0);
		int minuteTill = settings.getInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_TILL, 0);

		Date today = new Date();
		int year = today.getYear();
		int month = today.getMonth();
		int date = today.getDate();

		Date dateFrom = new Date(year, month, date, hourFrom, minuteFrom);
		Date dateTill = new Date(year, month, date, hourTill, minuteTill);
		return settings.getBoolean(Constants.PREF_BLOCK_SPAM_TIME, false) && today.after(dateFrom) && today.before(dateTill);
	}

	private boolean isFromContacts(String smsAddress) {
		return settings.getBoolean(Constants.PREF_ALLOW_MY_CONTACTS, true) && ContactsWrapper.isFromContacts(mContext, smsAddress);
	}

	private boolean isNotFromContacts(String smsAddress) {
		return settings.getBoolean(Constants.PREF_BLOCK_NOT_MY_CONTACTS, true) && !ContactsWrapper.isFromContacts(mContext, smsAddress);
	}
}