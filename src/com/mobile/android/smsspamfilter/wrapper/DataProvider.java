package com.mobile.android.smsspamfilter.wrapper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony.TextBasedSmsColumns;
import android.text.TextUtils;

import com.mobile.android.smsspamfilter.data.Constants;

public class DataProvider extends ContentProvider {
	public final String LOG_TAG = "ContentProvider";
	
	public static final String DB_NAME = "smsspamfilter.db";
	public static final int DB_VERSION = 1;

	public static final String SPAM_TABLE = "spam";
	public static final String ALLOWED_TABLE = "allowed";
	public static final String BLOCKED_TABLE = "blocked";

	public static final String DB_CREATE_SPAM_TABLE = "create table " + SPAM_TABLE + "(" + BaseColumns._ID
			+ " integer primary key autoincrement, "
			+ TextBasedSmsColumns.THREAD_ID + " long, "
			+ TextBasedSmsColumns.ADDRESS + " text, "
			+ TextBasedSmsColumns.BODY + " text, "
			+ TextBasedSmsColumns.DATE + " long" + ");";

	public static final String DB_CREATE_ALLOWED_TABLE = "create table " + ALLOWED_TABLE + "(" + Constants.BLOCKED_ID
			+ " integer primary key autoincrement, " + Constants.IS_CHECKED + " boolean, " + Constants.IS_NUMBER + " boolean, "
			+ Constants.VALUE + " text" + ");";

	public static final String DB_CREATE_BLOCKED_TABLE = "create table " + BLOCKED_TABLE + "(" + Constants.BLOCKED_ID
			+ " integer primary key autoincrement, " + Constants.IS_CHECKED + " boolean, " + Constants.IS_NUMBER + " boolean, "
			+ Constants.VALUE + " text" + ");";

	public static final String AUTHORITY = "com.mobile.android.smsspamfilter.contentprovider";

	public static final String SPAM_PATH = "spam";
	public static final String ALLOWED_PATH = "allowed";
	public static final String BLOCKED_PATH = "blocked";

	public static final Uri SPAM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SPAM_PATH);
	public static final String SPAM_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + SPAM_PATH;
	public static final String SPAM_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + SPAM_PATH;

	public static final Uri ALLOWED_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ALLOWED_PATH);
	public static final String ALLOWED_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + ALLOWED_PATH;
	public static final String ALLOWED_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + ALLOWED_PATH;

	public static final Uri BLOCKED_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BLOCKED_PATH);
	public static final String BLOCKED_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + BLOCKED_PATH;
	public static final String BLOCKED_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + BLOCKED_PATH;

	public static final int URI_SPAM = 0;
	public static final int URI_ALLOWED = 1;
	public static final int URI_BLOCKED = 2;
	public static final int URI_SPAM_ID = 3;
	public static final int URI_ALLOWED_ID = 4;
	public static final int URI_BLOCKED_ID = 5;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, SPAM_PATH, URI_SPAM);
		uriMatcher.addURI(AUTHORITY, ALLOWED_PATH, URI_ALLOWED);
		uriMatcher.addURI(AUTHORITY, BLOCKED_PATH, URI_BLOCKED);
		uriMatcher.addURI(AUTHORITY, SPAM_PATH + "/#", URI_SPAM_ID);
		uriMatcher.addURI(AUTHORITY, ALLOWED_PATH + "/#", URI_ALLOWED_ID);
		uriMatcher.addURI(AUTHORITY, BLOCKED_PATH + "/#", URI_BLOCKED_ID);
	}

	private DBHelper dbHelper;
	private SQLiteDatabase db;

	public boolean onCreate() {
		// Log.d(LOG_TAG, "onCreate");
		dbHelper = new DBHelper(getContext());
		return true;
	}

	/**
	 * Execute query
	 */
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		String table = "";
		Uri contentUri = null;
		switch (uriMatcher.match(uri)) {
		case URI_SPAM:
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = TextBasedSmsColumns.DATE + " DESC";
			}
			table = SPAM_TABLE;
			contentUri = SPAM_CONTENT_URI;
			break;
		case URI_ALLOWED:
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Constants.BLOCKED_ID + " DESC";
			}
			table = ALLOWED_TABLE;
			contentUri = ALLOWED_CONTENT_URI;
			break;
		case URI_BLOCKED:
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = Constants.BLOCKED_ID + " DESC";
			}
			table = BLOCKED_TABLE;
			contentUri = BLOCKED_CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		db = dbHelper.getWritableDatabase();
		cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), contentUri);
		return cursor;
	}

	/**
	 * Insert row
	 */
	public Uri insert(Uri uri, ContentValues values) {
		String table = "";
		Uri contentUri = null;
		switch (uriMatcher.match(uri)) {
		case URI_SPAM:
			table = SPAM_TABLE;
			contentUri = SPAM_CONTENT_URI;
			break;
		case URI_ALLOWED:
			table = ALLOWED_TABLE;
			contentUri = ALLOWED_CONTENT_URI;
			break;
		case URI_BLOCKED:
			table = BLOCKED_TABLE;
			contentUri = BLOCKED_CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		db = dbHelper.getWritableDatabase();
		long rowID = db.insert(table, null, values);
		Uri resultUri = ContentUris.withAppendedId(contentUri, rowID);
		getContext().getContentResolver().notifyChange(uri, null);
		return resultUri;
	}

	/**
	 * Delete row
	 */
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		db = dbHelper.getWritableDatabase();
		String table = "";
		switch (uriMatcher.match(uri)) {
		case URI_SPAM:
			table = SPAM_TABLE;
			break;
		case URI_ALLOWED:
			table = ALLOWED_TABLE;
			break;
		case URI_BLOCKED:
			table = BLOCKED_TABLE;
			break;
		case URI_SPAM_ID:
			table = SPAM_TABLE;
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = BaseColumns._ID + " = " + id;
			} else {
				selection = selection + " AND " + BaseColumns._ID + " = " + id;
			}
			break;
		case URI_ALLOWED_ID:
			table = ALLOWED_TABLE;
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = Constants.BLOCKED_ID + " = " + id;
			} else {
				selection = selection + " AND " + Constants.BLOCKED_ID + " = " + id;
			}
			break;
		case URI_BLOCKED_ID:
			table = BLOCKED_TABLE;
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = Constants.BLOCKED_ID + " = " + id;
			} else {
				selection = selection + " AND " + Constants.BLOCKED_ID + " = " + id;
			}
			break;
		}
		int result = db.delete(table, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return result;
	}

	/**
	 * Update row
	 */
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		db = dbHelper.getWritableDatabase();
		String table = "";
		switch (uriMatcher.match(uri)) {
		case URI_SPAM:
			table = SPAM_TABLE;
			break;
		case URI_ALLOWED:
			table = ALLOWED_TABLE;
			break;
		case URI_BLOCKED:
			table = BLOCKED_TABLE;
			break;
		case URI_SPAM_ID:
			table = SPAM_TABLE;
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = BaseColumns._ID + " = " + id;
			} else {
				selection = selection + " AND " + BaseColumns._ID + " = " + id;
			}
			break;
		case URI_ALLOWED_ID:
			table = ALLOWED_TABLE;
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = Constants.BLOCKED_ID + " = " + id;
			} else {
				selection = selection + " AND " + Constants.BLOCKED_ID + " = " + id;
			}
			break;
		case URI_BLOCKED_ID:
			table = BLOCKED_TABLE;
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = Constants.BLOCKED_ID + " = " + id;
			} else {
				selection = selection + " AND " + Constants.BLOCKED_ID + " = " + id;
			}
			break;
		}
		int result = db.update(table, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return result;
	}

	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case URI_SPAM:
			return SPAM_CONTENT_TYPE;
		case URI_ALLOWED:
			return ALLOWED_CONTENT_TYPE;
		case URI_BLOCKED:
			return BLOCKED_CONTENT_TYPE;
		case URI_SPAM_ID:
			return SPAM_CONTENT_ITEM_TYPE;
		case URI_ALLOWED_ID:
			return ALLOWED_CONTENT_ITEM_TYPE;
		case URI_BLOCKED_ID:
			return BLOCKED_CONTENT_ITEM_TYPE;
		default:
			return null;
		}
	}

	public class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_SPAM_TABLE);
			db.execSQL(DB_CREATE_ALLOWED_TABLE);
			db.execSQL(DB_CREATE_BLOCKED_TABLE);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
