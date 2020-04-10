package com.mobile.android.smsspamfilter.utils;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.fragments.SettingsRestoreCallback;

public class BackupUtils {
	
	public static String getExternalPath(Context context) {
		return Environment.getExternalStorageDirectory() + Constants.DIR + File.separator + Constants.BACKUP + ".xml";
	}
	
	private static String getSettingsPath(Context context){
		return "/data/data/" + context.getPackageName() +  Constants.PREFS_DIR + File.separator + Constants.SETTINGS + ".xml";
	}
	
	public static String getSettingsPathLog(Context context){
		return "/data/data/" + context.getPackageName() +  Constants.PREFS_DIR + File.separator + Constants.LOG + ".log";
	}
	
	
	private static File createExternalDir(Context context) {
		try {
			String directory = Environment.getExternalStorageDirectory().toString();
		    
		    /** Create folder */
		    File folder = new File(directory + Constants.DIR);
		    folder.mkdir();
			
			File file = new File(getExternalPath(context));	
			file.createNewFile();
			return file;
		} catch (IOException e) {
			Log.e(Constants.TAG, e.getMessage());
		}
		return null;
	}
	
	public static String BackupStorage(Context context){
	
			return getExternalPath(context);
	                                                  }
	
	

	
	public static String saveBackupFromInternalToExternalStorage(Context context){
		File internalStorageFile = new File(getSettingsPath(context));
		if(internalStorageFile.exists()){
			File externalStorageFile = createExternalDir(context);
//			Toast.makeText(this,"select111",Toast.LENGTH_SHORT).show();	
	//		UserDialog.showInfoDialog(this,"1111111111111111111111111111");		
	//Log.e(Constants.TAG, "Copy from " + internalStorageFile + " to " + externalStorageFile);
			try {
				copyFile(internalStorageFile, externalStorageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return /* "" + internalStorageFile + " to " + externalStorageFile;*/              getExternalPath(context);
		}
		return null;
	}
	
	public static void restoreBackupFromExternalToInternalStorage(Context context, SettingsRestoreCallback callback){
		File externalStorageFile = new File(getExternalPath(context));
		if(externalStorageFile.exists()){
			//Log.e(Constants.TAG, "Copy from " + externalStorageFile + " to " + getSettingsPath(context));
			try {
				copyFile(externalStorageFile, new File(getSettingsPath(context)));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				callback.onRestore();
			}
		}
		//Log.e(Constants.TAG, "File restored from " + getExternalStorageFilePath(context));
	}
	
	public static void moveFile(File src, File targetDirectory) throws IOException {
        if (!src.renameTo(new File(targetDirectory, src.getName()))) {
            String str = (new StringBuilder()).append("Failed to move ").append(src).append(" to ").append(targetDirectory).toString();
            throw new IOException(str);
        } else            return;
    }
	
	private static void copyFile(File src, File dest) throws IOException {
        FileInputStream fileSrc = new FileInputStream(src);
        FileOutputStream fileDest = new FileOutputStream(dest);
        copyInputStream(fileSrc, fileDest);
    }
    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte buffer[] = new byte[4096];
        for (int len = in.read(buffer); len >= 0; len = in.read(buffer))
            out.write(buffer, 0, len);
        in.close();
        out.close();
    }
}
