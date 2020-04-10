package com.mobile.android.smsspamfilter.adapter;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.data.Call;
import com.mobile.android.smsspamfilter.data.DataManager;

public class CallHistoryArrayAdapter extends ArrayAdapter<Call> {
    private final LayoutInflater mInflater;

    public CallHistoryArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Call> data) {
        clear();
        if (data != null) {
            for (Call appEntry : data) {
                add(appEntry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.call_item, parent, false);
        } else {
            view = convertView;
        }

        Call item = getItem(position);
        String phone = item.getPhone();
        String name = DataManager.getInstance().getContactMap().get(phone);
        ((TextView)view.findViewById(R.id.phone)).setText(phone);
        ((TextView)view.findViewById(R.id.name)).setText(name);
        ((TextView)view.findViewById(R.id.data)).setText(item.getTime());

        return view;
    }
} 
