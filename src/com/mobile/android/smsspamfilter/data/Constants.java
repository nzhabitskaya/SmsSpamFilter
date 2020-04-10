package com.mobile.android.smsspamfilter.data;
import android.net.Uri;


public class Constants {
	public static final String TAG = "SmsSpamFilter";	
	public static final Uri SMS_URI = Uri.parse("content://sms");
	public static final Uri CANONICAL_ADDRESSES_URI = Uri.parse("content://mms-sms/canonical-addresses");
	public static final Uri CONVERSATIONS_URI = Uri.parse("content://mms-sms/conversations?simple=true");
	
	public static final String BACKUP = "backup";
	public static final String SETTINGS = "settings";
	public static final String LOG = "sms";
	
	public static final String PREF_BTN_SEND_LOG2 = "btn_send_check_log2";
	public static final String PREF_USE_BACKUP = "use_backup";
	public static final String PREF_USE_SMS_BLOCK = "use_sms_block";
	public static final String PREF_ALLOW_ANY_CID = "allow_any_cid";
	public static final String PREF_ALLOW_MY_CONTACTS = "allow_my_contacts";
	public static final String PREF_BLOCK_ANY_CID = "block_any_cid";
	public static final String PREF_BLOCK_NOT_MY_CONTACTS = "block_not_my_contacts";
	public static final String PREF_BLOCK_SPAM_TIME = "block_spam_time";
	public static final String PREF_BLOCK_SPAM_TIME_STRING = "block_spam_time_string";
	public static final String PREF_BLOCK_SPAM_HOUR_VALUE_FROM = "block_spam_hour_value_from";
	public static final String PREF_BLOCK_SPAM_MINUTE_VALUE_FROM = "block_spam_minute_value_from";
	public static final String PREF_BLOCK_SPAM_HOUR_VALUE_TILL = "block_spam_hour_value_till";
	public static final String PREF_BLOCK_SPAM_MINUTE_VALUE_TILL = "block_spam_minute_value_till";
	public static final String PREF_NOTIFICATION_ON_OFF = "notification_on_off";
	public static final String PREF_SOUND_ON_OFF = "sound_on_off";
	public static final String PREF_VIBRATION_ON_OFF = "vibration_on_off";
	
	public static final String INTERNAL_BLOCKED_LIST = "blockedlist01";
	public static final String INTERNAL_ALLOWED_LIST = "allowedlist01";
	
	public static final String COUNT = "count";
	public static final String FOLDER = "folder";
	public static final String FILES = "files";
	public static final String PATH = "path";
	public static final String DATA = "data";
	public static final String ID = "id";
	
	public final static String DIR = "/SmsSpamFilter";
	public final static String LOG_DIR = "/logs";
	public final static String PREFS_DIR = "/shared_prefs";
	
	public final static  String BLOCKED_ID = "_id";
	public final static  String IS_CHECKED = "is_checked";
	public final static  String IS_NUMBER = "is_number";
	public final static  String VALUE = "value";
	
	public final static  String _ID = "_id";
}
