package com.mobile.android.smsspamfilter.adapter;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.dialog.RegexpDialog;
import com.mobile.android.smsspamfilter.dialog.UpdateDialog;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;

public class BlockedSmsAdapter extends ArrayAdapter<BlockItem> {
	private Context mContext;
	private RegexpDialog regexpDialog;
	private UpdateDialog updateDialog;
	private BlockedListWrapper blockedListWrapper;

	public BlockedSmsAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		mContext = context;
		regexpDialog = new RegexpDialog(context, this, DataProvider.BLOCKED_CONTENT_URI);
		updateDialog = new UpdateDialog(context, this, DataProvider.BLOCKED_CONTENT_URI);
		
		/** Use wrapper to DB access */
		blockedListWrapper = new BlockedListWrapper(context, DataProvider.BLOCKED_CONTENT_URI);
	}
	
	public int getCount(){
		return blockedListWrapper.getCount();
	}
	
	public BlockItem getItem(int position){
		return blockedListWrapper.getItem(position);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.allow_sms_item, null);
		}

		final BlockItem item = getItem(position);

		if (item != null) {
			LinearLayout layout = (LinearLayout) view.findViewById(R.id.allow_item_layout);
			TextView textView = (TextView) view.findViewById(R.id.text_allow_sms);
			final CheckBox cb = (CheckBox) view.findViewById(R.id.cb_allow_sms);

			int typeId = item.getType() == 0 ? R.string.txt : R.string.num;
			String from = item.getValue();
			if(typeId==0){
				textView.setText(mContext.getString(typeId) + ": " + from);
			} else{ 
				String address = DataManager.getInstance().getContactMap().get(from);
				textView.setText(mContext.getString(typeId) + ": " + (address!=null?(from + " [" + address + "]"):from));
			}
			cb.setChecked(item.isChecked());

			// Add click listener
			cb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					item.setChecked(cb.isChecked());
					blockedListWrapper.updateBlockedItem(item.getId(), cb.isChecked());
				}
			});

			// Add onItemClick listener
			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!updateDialog.isShow()) {
						BlockItem item = blockedListWrapper.getItem(position);
						regexpDialog.setType(item.getType());
						regexpDialog.setValue(item.getValue());
						regexpDialog.setId(item.getId());
						regexpDialog.setUpdate(true);
						regexpDialog.show();
					}
				}
			});

			// Add onItemLongClick listener
			layout.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					updateDialog.setIsShow(true);
					updateDialog.setItem(item);
					updateDialog.show();
					return false;
				}
			});
		}

		return view;
	}
}
