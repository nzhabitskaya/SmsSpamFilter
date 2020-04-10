package com.mobile.android.smsspamfilter.fragments;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.data.Sms;
import com.mobile.android.smsspamfilter.dialog.RegexpDialog;
import com.mobile.android.smsspamfilter.dialog.SelectLanguageDialog;
import com.mobile.android.smsspamfilter.dialog.SelectTimeDialog;
import com.mobile.android.smsspamfilter.dialog.UpdateDialog;
import com.mobile.android.smsspamfilter.log.SendEmailUtil;
import com.mobile.android.smsspamfilter.utils.BackupUtils;
import com.mobile.android.smsspamfilter.utils.StorageUtils;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;
import com.mobile.android.smsspamfilter.wrapper.SmsListWrapper;


public class SettingsFragment extends Fragment {
	
	private Context mContext;	
	private BlockedListWrapper allowedListWrapper;
	private SmsListWrapper smsListWrapper;
	
	private TextView myContactsCountView;
	private CheckBox cb_allow_any_cid;
	private CheckBox cb_use_sms_block;
	private CheckBox cb_allow_my_contacts;
	private CheckBox cb_block_any_cid;
	private CheckBox cb_block_not_my_contacts;
	private CheckBox btn_send_check_log2;
	//PREF_BTN_SEND_LOG2 = "btn_send_log2";
	//PREF_BTN_SEND_LOG2 = "btn_send_check_log2";
	private CheckBox cb_block_spam_time;
	private TextView btn_block_spam_time;
	private TextView spam_time;
	private CheckBox cb_notification_on_off;
	private CheckBox cb_sound_on_off;
	private CheckBox cb_vibration_on_off;
	private RelativeLayout btn_backup_settings;
	private RelativeLayout btn_restore_settings;
	private RelativeLayout btn_send_log,btn_send_log2;

	private SelectLanguageDialog selectLanguageDialog;
	private SelectTimeDialog selectTimeDialog;
	
	/** Save settings in database */
	private SharedPreferences settings;
	private SettingsRestoreCallback callback;
	
	public SettingsFragment() {
	//	super();
	//	mContext = context;
	
		/** Use wrapper to DB access */
   //     allowedListWrapper = new BlockedListWrapper(getActivity().getApplicationContext(), DataProvider.ALLOWED_CONTENT_URI);
	}

	public BlockItem getItem(int position){
		return allowedListWrapper.getItem(position);
	}	
	
	
	public Sms getGroup(int groupPosition) {
		return smsListWrapper.getGroup(groupPosition);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.settings_fragment, container, false);
		myContactsCountView = (TextView) view.findViewById(R.id.comment_my_contacts_count);
		setContactsCount();
	
		
		settings = getActivity().getSharedPreferences(Constants.SETTINGS, Context.MODE_MULTI_PROCESS);
		Editor editor = settings.edit();
		editor.putBoolean(Constants.PREF_USE_BACKUP, true);
		editor.commit();
		callback = new SettingsCallback();

		btn_send_check_log2= (CheckBox) view.findViewById(R.id.btn_send_check_log2);
		btn_send_check_log2.setOnCheckedChangeListener(listener);
		cb_allow_any_cid = (CheckBox) view.findViewById(R.id.cb_allow_any_cid);
		cb_allow_any_cid.setOnCheckedChangeListener(listener);
		cb_use_sms_block = (CheckBox) view.findViewById(R.id.cb_use_sms_block);
		cb_use_sms_block.setOnCheckedChangeListener(listener);
		cb_allow_my_contacts = (CheckBox) view.findViewById(R.id.cb_allow_my_contacts);
		cb_allow_my_contacts.setOnCheckedChangeListener(listener);
		cb_block_any_cid = (CheckBox) view.findViewById(R.id.cb_block_any_number);
		cb_block_any_cid.setOnCheckedChangeListener(listener);
		cb_block_not_my_contacts = (CheckBox) view.findViewById(R.id.cb_block_not_my_contacts);
		cb_block_not_my_contacts.setOnCheckedChangeListener(listener);

		cb_block_spam_time = (CheckBox) view.findViewById(R.id.cb_block_spam_time);
		cb_block_spam_time.setOnCheckedChangeListener(listener);
		spam_time = (TextView) view.findViewById(R.id.spam_time);
		spam_time.setOnClickListener(onClickListener);
		btn_block_spam_time = (TextView) view.findViewById(R.id.btn_block_spam_time);
		btn_block_spam_time.setOnClickListener(onClickListener);
		
		setTimePeriod();

		cb_notification_on_off = (CheckBox) view.findViewById(R.id.cb_notification_on_off);
		cb_notification_on_off.setOnCheckedChangeListener(listener);
		cb_sound_on_off = (CheckBox) view.findViewById(R.id.cb_sound_on_off);
		cb_sound_on_off.setOnCheckedChangeListener(listener);
		cb_vibration_on_off = (CheckBox) view.findViewById(R.id.cb_vibration_on_off);
		cb_vibration_on_off.setOnCheckedChangeListener(listener);
		btn_backup_settings = (RelativeLayout) view.findViewById(R.id.btn_backup_settings);
		btn_backup_settings.setOnClickListener(onClickListener);
		btn_restore_settings = (RelativeLayout) view.findViewById(R.id.btn_restore_settings);
		btn_restore_settings.setOnClickListener(onClickListener);
		btn_send_log = (RelativeLayout) view.findViewById(R.id.btn_send_log);
		btn_send_log.setOnClickListener(onClickListener);
		btn_send_log2 = (RelativeLayout) view.findViewById(R.id.btn_send_log2);
		btn_send_log2.setOnClickListener(onClickListener);
		initSettings();

		selectLanguageDialog = new SelectLanguageDialog(getActivity());
		selectTimeDialog = new SelectTimeDialog(getActivity(), settings, spam_time);

	    return view;
	}

	OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			Editor editor = settings.edit();
			if (buttonView == cb_allow_any_cid) {
				editor.putBoolean(Constants.PREF_ALLOW_ANY_CID, isChecked);
				if(isChecked){
					boolean isBlockChecked = cb_allow_any_cid.isChecked();
					cb_block_any_cid.setChecked(!isBlockChecked);
					editor.putBoolean(Constants.PREF_BLOCK_ANY_CID, !isBlockChecked);
				}
			} else if (buttonView == cb_use_sms_block) {
				editor.putBoolean(Constants.PREF_USE_SMS_BLOCK, isChecked);
			} else if (buttonView == cb_allow_my_contacts) {
				editor.putBoolean(Constants.PREF_ALLOW_MY_CONTACTS, isChecked);
			} else if (buttonView == cb_block_any_cid) {
				editor.putBoolean(Constants.PREF_BLOCK_ANY_CID, isChecked);
				if(isChecked){
					boolean isAllowChecked = cb_block_any_cid.isChecked();
					cb_allow_any_cid.setChecked(!isAllowChecked);
					editor.putBoolean(Constants.PREF_ALLOW_ANY_CID, !isAllowChecked);
				}
			} else if (buttonView == cb_block_not_my_contacts) {
				editor.putBoolean(Constants.PREF_BLOCK_NOT_MY_CONTACTS, isChecked);
			} else if (buttonView == cb_block_spam_time) {
				editor.putBoolean(Constants.PREF_BLOCK_SPAM_TIME, isChecked);
			}	
			
			else if (buttonView == cb_notification_on_off) {
				editor.putBoolean(Constants.PREF_NOTIFICATION_ON_OFF, isChecked);
			} else if (buttonView == cb_sound_on_off) {
				editor.putBoolean(Constants.PREF_SOUND_ON_OFF, isChecked);
			} else if (buttonView == cb_vibration_on_off) {
				editor.putBoolean(Constants.PREF_VIBRATION_ON_OFF, isChecked);
			}

			editor.commit();
		}
	};

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_block_spam_time:
				selectTimeDialog.show();
		//	Toast.makeText(this,"select111",Toast.LENGTH_SHORT).show();	
				
				break;
			case R.id.spam_time:
				selectTimeDialog.show();
				break;
			case R.id.btn_backup_settings:
				backupSettings();
				break;
			case R.id.btn_restore_settings:
				restoreSettings();
				break;
			case R.id.btn_send_log:
				SendEmailUtil.sendEmail(getActivity());
				break;
			case R.id.btn_send_log2:
				saveLog();
				break;	
				
			}
		}
	};

	private void initSettings() {
		cb_use_sms_block.setChecked(settings.getBoolean(Constants.PREF_USE_SMS_BLOCK, true));
		cb_allow_any_cid.setChecked(settings.getBoolean(Constants.PREF_ALLOW_ANY_CID, false));
		cb_allow_my_contacts.setChecked(settings.getBoolean(Constants.PREF_ALLOW_MY_CONTACTS,true));
		cb_block_any_cid.setChecked(settings.getBoolean(Constants.PREF_BLOCK_ANY_CID, true));
		cb_block_not_my_contacts.setChecked(settings.getBoolean(Constants.PREF_BLOCK_NOT_MY_CONTACTS, true));
		
		cb_block_spam_time.setChecked(settings.getBoolean(Constants.PREF_BLOCK_SPAM_TIME, false));
		spam_time.setText(settings.getString(Constants.PREF_BLOCK_SPAM_TIME_STRING, "00:00 - 00:00"));
		cb_notification_on_off.setChecked(settings.getBoolean(Constants.PREF_NOTIFICATION_ON_OFF, true));
		cb_sound_on_off.setChecked(settings.getBoolean(Constants.PREF_SOUND_ON_OFF, false));
		cb_vibration_on_off.setChecked(settings.getBoolean(Constants.PREF_VIBRATION_ON_OFF, false));
		btn_send_check_log2.setChecked(settings.getBoolean(Constants.PREF_BTN_SEND_LOG2, true));
	}
	
	private void saveLog()
	{
		String backupPathLog = BackupUtils.getSettingsPathLog(getActivity().getApplicationContext());
		File CreateFileLog = new File(backupPathLog);	
		//smsListWrapper = new SmsListWrapper(getActivity().getApplicationContext());		
		  allowedListWrapper = new BlockedListWrapper(getActivity(), DataProvider.ALLOWED_CONTENT_URI);

		  try {
			CreateFileLog.createNewFile();
		//final BlockItem item = getItem(position);
		//	int sizesms=getCount();
			int sizesms=allowedListWrapper.getCount();
			BufferedWriter bw = new BufferedWriter(new FileWriter(CreateFileLog));
			
			for(int i=0;i<sizesms;i++)
			{
			//	Sms groupItem = getGroup(1);	
			
			 BlockItem item = getItem(i);
			 
			 String from = item.getValue();
			 bw.write(from);
			} 
  //   UserDialog.showInfoDialog(getActivity(), getString(R.string.msg_backup," //"+groupItem.getTime()+" "+groupItem.getPhone()));	
			
	//UserDialog.showInfoDialog(getActivity(), getString(R.string.msg_backup," //"+sizesms));		
		//	}
			
	/*		BufferedWriter bw = new BufferedWriter(new FileWriter(CreateFileLog));
		      // пишем данные
		  */  //  bw.write("Тестовая запись в лог");
		      // закрываем поток
		      bw.close();			
			
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	int sizesms=getGroupCount();
	//	Sms groupItem = getGroup(1);
		
	}
	private void backupSettings(){
		// Read Blocked and Allowed lists
		String backupPath2 = BackupUtils.BackupStorage(getActivity().getApplicationContext());
		
		File internalStorageFile = new File(backupPath2);
		
		if(internalStorageFile.exists()){
			Date lastModified = new Date(internalStorageFile.lastModified());
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String data=dt1.format(lastModified);
			
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Файл от "+data)
			.setCancelable(false)
				.setPositiveButton("Экспортировать", 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						List<BlockItem> allowedList = BlockedListWrapper.exportList(getActivity().getApplicationContext().getContentResolver(), DataProvider.ALLOWED_CONTENT_URI);
						List<BlockItem> blockedList = BlockedListWrapper.exportList(getActivity().getApplicationContext().getContentResolver(), DataProvider.BLOCKED_CONTENT_URI);
						
						// Save lists into settings 
						StorageUtils.saveAllowedList(getActivity(), settings, allowedList);
						StorageUtils.saveBlockedList(getActivity(), settings, blockedList);
						
						String backupPath = BackupUtils.saveBackupFromInternalToExternalStorage(getActivity().getApplicationContext());
						UserDialog.showInfoDialog(getActivity(), getString(R.string.msg_backup, backupPath));			
						
					}
				}
        )
				.setNegativeButton("Отменить",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();	
		
		}	
		
	}
	
	private void restoreSettings(){	
		/** Restore settings if backup exists */
		File backupFile = new File(BackupUtils.getExternalPath(getActivity().getApplicationContext()));
		if(backupFile.exists()) {
				Date lastModified = new Date(backupFile.lastModified());
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String data=dt1.format(lastModified);
			
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Файл от: "+data)
			.setCancelable(false)
				.setPositiveButton("Импортировать", 
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					
			BackupUtils.restoreBackupFromExternalToInternalStorage(getActivity().getApplicationContext(), callback);			
	
					}
				}
        )
        				.setNegativeButton("Отменить",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();	
		
		
		
		} else{
			UserDialog.showInfoDialog(getActivity(), R.string.msg_restore_not_found);
		}
			
	}
	
	public class SettingsCallback implements SettingsRestoreCallback {
		
		public void onRestore(){
			
			settings = getActivity().getSharedPreferences(Constants.SETTINGS, Context.MODE_MULTI_PROCESS);
			
			/** Restore Blocked and Allowed lists */
			StorageUtils.restoreAllowedList(getActivity(), settings);
			StorageUtils.restoreBlockedList(getActivity(), settings);
			
			cb_use_sms_block.setChecked(settings.getBoolean(Constants.PREF_USE_SMS_BLOCK, true));
			cb_allow_any_cid.setChecked(settings.getBoolean(Constants.PREF_ALLOW_ANY_CID, false));
			cb_allow_my_contacts.setChecked(settings.getBoolean(Constants.PREF_ALLOW_MY_CONTACTS, true));
			cb_block_any_cid.setChecked(settings.getBoolean(Constants.PREF_BLOCK_ANY_CID, true));
			cb_block_not_my_contacts.setChecked(settings.getBoolean(Constants.PREF_BLOCK_NOT_MY_CONTACTS, true));
			
			cb_block_spam_time.setChecked(settings.getBoolean(Constants.PREF_BLOCK_SPAM_TIME, false));
			spam_time.setText(settings.getString(Constants.PREF_BLOCK_SPAM_TIME_STRING, ""));
			cb_notification_on_off.setChecked(settings.getBoolean(Constants.PREF_NOTIFICATION_ON_OFF, true));
			cb_sound_on_off.setChecked(settings.getBoolean(Constants.PREF_SOUND_ON_OFF, false));
			cb_vibration_on_off.setChecked(settings.getBoolean(Constants.PREF_VIBRATION_ON_OFF, false));
			
			UserDialog.showInfoDialog(getActivity(), R.string.msg_restore);			
		}
	};
	
	private void setTimePeriod(){
		String hourFrom = String.valueOf(settings.getInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_FROM, 0));
		String minuteFrom = String.valueOf(settings.getInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_FROM, 0));
		String hourTill = String.valueOf(settings.getInt(Constants.PREF_BLOCK_SPAM_HOUR_VALUE_TILL, 0));
		String minuteTill = String.valueOf(settings.getInt(Constants.PREF_BLOCK_SPAM_MINUTE_VALUE_TILL, 0));
		
		if(hourFrom.length() == 1)
			hourFrom = '0' + hourFrom;
		if(minuteFrom.length() == 1)
			minuteFrom = '0' + minuteFrom;
		if(hourTill.length() == 1)
			hourTill = '0' + hourTill;
		if(minuteTill.length() == 1)
			minuteTill = '0' + minuteTill;
		
		String timeValue = getString(R.string.spam_time, hourFrom, minuteFrom, hourTill, minuteTill);
		spam_time.setText(timeValue);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		setTimePeriod();
		initSettings();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	private void setContactsCount(){
		int contactsCount = DataManager.getInstance().getContactSet().size();
		myContactsCountView.setText(getString(R.string.comment_my_contacts_count, contactsCount));
	}
}
