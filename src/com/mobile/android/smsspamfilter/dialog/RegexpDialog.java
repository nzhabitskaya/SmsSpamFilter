package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.text.Selection;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;

public class RegexpDialog extends Dialog implements OnItemSelectedListener{
	private Context mContext;
	private ArrayAdapter<BlockItem> mAdapter;
	private Spinner typeSpinner;
	private EditText valueEditText;
	private int type = 0;
	private String value;
	private int pos;
	private long id;
	private Uri contentUri;
	private ContentResolver contentResolver;
	private boolean isUpdate = false;

	public RegexpDialog(Context context, ArrayAdapter<BlockItem> adapter, Uri contentUri) {
		super(context);
		setContentView(R.layout.block_dialog);
		setTitle(R.string.add_new);
		this.mAdapter = adapter;
		this.contentUri = contentUri;
		this.contentResolver = context.getContentResolver();

		typeSpinner = (Spinner) findViewById(R.id.spinner);
		typeSpinner.setOnItemSelectedListener(this);
		valueEditText = (EditText) findViewById(R.id.regexp);
		
		Button buttonOk = (Button) findViewById(R.id.btn_ok);
		buttonOk.setOnClickListener(listener);
		Button buttonCancel = (Button) findViewById(R.id.btn_cancel);
		buttonCancel.setOnClickListener(listener);
		
		Button buttonOr = (Button) findViewById(R.id.btn_or);
		buttonOr.setOnClickListener(listener);
		Button buttonAnd = (Button) findViewById(R.id.btn_and);
		buttonAnd.setOnClickListener(listener);
		Button buttonAny = (Button) findViewById(R.id.btn_any);
		buttonAny.setOnClickListener(listener);
	}
	
	android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View view) {
			
			value = valueEditText.getText().toString();
			pos = valueEditText.getSelectionEnd();
			
			switch (view.getId()) {
			case R.id.btn_ok:
				if(value.length() > 0){
					if(isUpdate)
						BlockedListWrapper.updateBlockedItem(contentResolver, contentUri, id, typeSpinner.getSelectedItemPosition()==0?false:true, value);
					else
						BlockedListWrapper.insertBlockedItem(contentResolver, contentUri, true, typeSpinner.getSelectedItemPosition()==0?false:true, value);
					mAdapter.notifyDataSetChanged();
				}
				dismiss();
				break;
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_or:
				value = value.substring(0, pos) + '|' + value.substring(pos, value.length());
				valueEditText.setText(value);
				Selection.setSelection(valueEditText.getText(), pos + 1);
				break;
			case R.id.btn_and:
				value = value.substring(0, pos) + ".*" + value.substring(pos, value.length());
				valueEditText.setText(value);
				Selection.setSelection(valueEditText.getText(), pos + 2);
				break;
			case R.id.btn_any:
				value = value.substring(0, pos) + ".{0,3}" + value.substring(pos, value.length());
				valueEditText.setText(value);
				Selection.setSelection(valueEditText.getText(), pos + 6);
				break;
			}
		}
	};


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
		type = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	public void setType(int type){
		typeSpinner.setSelection(type);
	}
	
	public void setValue(String value){
		valueEditText.setText(value);
	}
	
	public void setId(long id){
		this.id = id;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
}