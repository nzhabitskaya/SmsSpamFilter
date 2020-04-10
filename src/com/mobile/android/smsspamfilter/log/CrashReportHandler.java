package com.mobile.android.smsspamfilter.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.Constants;

public class CrashReportHandler implements UncaughtExceptionHandler {
	private Context mContext;
	
	private UncaughtExceptionHandler defaultUEH;
	
	private CrashReportHandler(Context context) {
		mContext = context;
	}

	public static void attach(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(new CrashReportHandler(context));
	}

	public void uncaughtException(Thread t, Throwable e) {
		String timestamp = "" + System.currentTimeMillis();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".log";
        writeToFile(stacktrace, filename);

        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
        	String internalStorageDirectory = mContext.getFilesDir().toString();
		    String folderPath = internalStorageDirectory + Constants.LOG_DIR;
		    String localPath = folderPath  + "/" + filename;
		    /** Create Folder */
		    File folder = new File(folderPath);
		    folder.mkdirs();
		    
            BufferedWriter bos = new BufferedWriter(new FileWriter(localPath));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
            
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ERROR_REPORT_PATH", localPath);
            editor.commit();
            //Log.e(Constants.TAG, "Report file was created: " + localPath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

	private void showLogNotFoundDialog() {
		AlertDialog mLogNotFoundDialog = new AlertDialog.Builder(mContext)
	            .setMessage(R.string.msg_file_is_not_available).setPositiveButton(R.string.ok, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}})
	            .setCancelable(false).create();
	    mLogNotFoundDialog.show();
	}
}
