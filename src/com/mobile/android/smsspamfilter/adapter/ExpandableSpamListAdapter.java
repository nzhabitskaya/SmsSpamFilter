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
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.DataManager;
import com.mobile.android.smsspamfilter.data.Sms;
import com.mobile.android.smsspamfilter.dialog.AllowDialog;
import com.mobile.android.smsspamfilter.wrapper.SpamListWrapper;

public class ExpandableSpamListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
    private AllowDialog allowDialog;
    private SpamListWrapper spamListWrapper;
  
    public ExpandableSpamListAdapter (Context context){
        mContext = context;
        allowDialog = new AllowDialog(context);  
        
        /** Use wrapper to DB access */
        spamListWrapper = new SpamListWrapper(context);
    }
    
    @Override
    public int getGroupCount() {
        return spamListWrapper.getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
    	return spamListWrapper.getChildrenCount(groupPosition);
    }

    @Override
    public Sms getGroup(int groupPosition) {
        return spamListWrapper.getGroup(groupPosition);
    }

    @Override
    public Sms getChild(int groupPosition, int childPosition) {
        return spamListWrapper.getChild(groupPosition, childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sms_white_item, null);
        }
        
        ImageView expandImg = (ImageView) convertView.findViewById(R.id.expand);
        ImageView collapseImg = (ImageView) convertView.findViewById(R.id.collapse);
        
        if(isExpanded){
        	expandImg.setVisibility(View.VISIBLE);
        	collapseImg.setVisibility(View.INVISIBLE);        	
        }else{
        	expandImg.setVisibility(View.INVISIBLE);
        	collapseImg.setVisibility(View.VISIBLE);
        }

        final Sms groupItem = getGroup(groupPosition);
        setColumns(groupItem, convertView);
	        
        /** Hide group indicator when group contains no children */    
        if(getChildrenCount(groupPosition) == 0){
        	expandImg.setVisibility(View.INVISIBLE);
        	collapseImg.setVisibility(View.INVISIBLE);
        }
        
        /** Add onItemClick listener */
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.click_layout);
 		layout.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				allowDialog.setItem(groupItem);
 				allowDialog.show();
 			}
 		});
        
        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sms_white_item, null);
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
 			public void onClick(View v) {
 				allowDialog.setItem(childItem);
 				allowDialog.show();
 			}
 		});

        return convertView;
    }
    
    private void setColumns(Sms item, View convertView){
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}