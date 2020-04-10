package com.mobile.android.smsspamfilter.adapter;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.BlockItem;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.data.Sms;
import com.mobile.android.smsspamfilter.dialog.FromSmsBaseDialog;
import com.mobile.android.smsspamfilter.wrapper.BlockedListWrapper;
import com.mobile.android.smsspamfilter.wrapper.SmsListWrapper;

public class ExpandableSmsListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private Dialog mDialog;
	private SmsListWrapper smsListWrapper;

	protected ArrayAdapter<BlockItem> listAdapter;
	protected Uri contentUri;
	protected int type;

	public ExpandableSmsListAdapter(Context context, int type, ArrayAdapter<BlockItem> blockedListAdapter, Uri contentUri) {
		mContext = context;

		this.listAdapter = blockedListAdapter;
		this.contentUri = contentUri;
		this.type = type;

		/** Use wrapper to DB access */
		smsListWrapper = new SmsListWrapper(context);
	}

	@Override
	public int getGroupCount() {
		return smsListWrapper.getGroupCount();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return smsListWrapper.getChildrenCount(groupPosition);
	}

	@Override
	public Sms getGroup(int groupPosition) {
		return smsListWrapper.getGroup(groupPosition);
	}

	@Override
	public Sms getChild(int groupPosition, int childPosition) {
		return smsListWrapper.getChild(groupPosition, childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sms_item, null);
		}

		ImageView expandImg = (ImageView) convertView.findViewById(R.id.expand);
		ImageView collapseImg = (ImageView) convertView.findViewById(R.id.collapse);

		if (isExpanded) {
			expandImg.setVisibility(View.VISIBLE);
			collapseImg.setVisibility(View.INVISIBLE);
		} else {
			expandImg.setVisibility(View.INVISIBLE);
			collapseImg.setVisibility(View.VISIBLE);
		}

		final Sms groupItem = getGroup(groupPosition);
		setColumns(groupItem, convertView);

		/** Hide group indicator when group contains no children */
		if (getChildrenCount(groupPosition) == 0) {
			expandImg.setVisibility(View.INVISIBLE);
			collapseImg.setVisibility(View.INVISIBLE);
		}

		/** Add onItemClick listener */
		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.click_layout);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (type == 0)
					addItemOnClick(groupItem.getThreadID(), groupItem.getPhone());
				//	Toast.makeText(mContext,"select"+groupItem.getThreadID()+"/ "+groupItem.getMessage()+"/"+groupItem.getPhone(),Toast.LENGTH_SHORT).show();	
				else
					addItemOnClick(groupItem.getThreadID(), groupItem.getPhone());
				//	Toast.makeText(mContext,"select"+groupItem.getThreadID()+"/ "+groupItem.getPhone()+"/ "+groupItem.getMessage(),Toast.LENGTH_SHORT).show();		
				mDialog.dismiss();
			}
		});

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sms_item, null);
		}

		final Sms childItem = getChild(groupPosition, childPosition);
		setColumns(childItem, convertView);

		ImageView expandImg = (ImageView) convertView.findViewById(R.id.expand);
		expandImg.setVisibility(View.INVISIBLE);
		ImageView collapseImg = (ImageView) convertView.findViewById(R.id.collapse);
		collapseImg.setVisibility(View.INVISIBLE);

		/** Add onItemClick listener */
		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.click_layout);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (type == FromSmsBaseDialog.FROM_SMS_BODY_DIALOG)
					addItemOnClick(childItem.getThreadID(), childItem.getMessage());
				else
					addItemOnClick(childItem.getThreadID(), childItem.getPhone());
				mDialog.dismiss();
			}
		});

		return convertView;
	}
	
	private void setColumns(Sms item, View convertView){
		TextView draft = (TextView) convertView.findViewById(R.id.draft);
		if(item.isDraft())
			draft.setVisibility(View.VISIBLE);
		else
			draft.setVisibility(View.INVISIBLE);
		TextView addressGroup = (TextView) convertView.findViewById(R.id.address);
		String from = item.getPhone();
		if(from == null)
			from = DataManager.getInstance().getPhoneByThreadID(item.getThreadID());
		String address = DataManager.getInstance().getContactMap().get(from);
		addressGroup.setText(address != null ? (address + " (" + from + ")") : from);
		TextView dataGroup = (TextView) convertView.findViewById(R.id.data);
		dataGroup.setText(item.getTime());
		TextView textGroup = (TextView) convertView.findViewById(R.id.text);
		textGroup.setText(item.getMessage());
	}

	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	protected void addItemOnClick(long threadID, String value) {
		if(type == 1 && value == null)
			value = DataManager.getInstance().getPhoneByThreadID(threadID);
		BlockedListWrapper.insertBlockedItem(mContext.getApplicationContext().getContentResolver(), contentUri, true,
				type == FromSmsBaseDialog.FROM_SMS_BODY_DIALOG ? false : true, value);
		
		
	//	Toast.makeText(mContext,"select111"+value,Toast.LENGTH_SHORT).show();	
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}