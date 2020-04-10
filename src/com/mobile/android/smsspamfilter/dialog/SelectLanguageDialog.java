package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.Constants;
import com.mobile.android.smsspamfilter.data.DataManager;

public class SelectLanguageDialog extends Dialog implements OnItemSelectedListener{
	private Spinner typeSpinner;
	private DataManager manager;
	
	private Locale localeDefault;
	private Locale localeRu;

	public SelectLanguageDialog(Context context) {
		super(context);
		setContentView(R.layout.select_language_dialog);
		setTitle(R.string.choose_language);
		manager = DataManager.getInstance();
		
		localeDefault = Locale.getDefault();
		localeRu = new Locale("ru");

		typeSpinner = (Spinner) findViewById(R.id.spinner);
		typeSpinner.setOnItemSelectedListener(this);
		
		Button buttonOk = (Button) findViewById(R.id.btn_ok);
		buttonOk.setOnClickListener(listener);
		Button buttonCancel = (Button) findViewById(R.id.btn_cancel);
		buttonCancel.setOnClickListener(listener);
	}
	
	android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_ok:
				int pos = typeSpinner.getSelectedItemPosition();
				if(pos == 0){
					Locale.setDefault(localeDefault);
			        Log.d(Constants.TAG, "lang = " + localeDefault.getLanguage());
				}else{
					Locale.setDefault(localeRu);
					Log.d(Constants.TAG, "lang = " + localeRu.getLanguage());
				}
				dismiss();
				break;
			case R.id.btn_cancel:
				dismiss();
				break;
			}
		}
	};

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	public void setType(int type){
		typeSpinner.setSelection(type);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {	
	}
}
