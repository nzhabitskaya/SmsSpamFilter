package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;

public class UpdateDialog extends Dialog {
	private boolean isShow = false;
	private ArrayAdapter adapter;
	private Context mContext;
	private RegexpDialog regexpDialog;
	private BlockItem item;
	private Uri contentUri;

	public UpdateDialog(Context context, ArrayAdapter adapter, Uri contentUri) {
		super(context);
		setContentView(R.layout.update_dialog);
		setTitle(R.string.title_selection);
		this.adapter = adapter;
		this.contentUri = contentUri;
		mContext = context;
		regexpDialog = new RegexpDialog(mContext, adapter, contentUri);

		TextView buttonUpdate = (TextView) findViewById(R.id.update);
		buttonUpdate.setOnClickListener(listener);
		TextView buttonDelete = (TextView) findViewById(R.id.delete);
		buttonDelete.setOnClickListener(listener);
	}

	android.view.View.OnClickListener listener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.update:
				regexpDialog.setType(item.getType());
				regexpDialog.setValue(item.getValue());
				regexpDialog.setId(item.getId());
				regexpDialog.setUpdate(true);
				regexpDialog.show();
				dismiss();
				isShow = false;
				break;
			case R.id.delete:
				BlockedListWrapper.deleteBlockedItem(mContext, contentUri, item.getId());
				adapter.notifyDataSetChanged();
				dismiss();
				isShow = false;
				break;
			}
		}
	};
	
	public void setIsShow(boolean isShow){
		this.isShow = isShow;
	}
	
	public boolean isShow(){
		return isShow;
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		isShow = false;
	}
	
	public void setItem(BlockItem item){
		this.item = item;
	}
}
