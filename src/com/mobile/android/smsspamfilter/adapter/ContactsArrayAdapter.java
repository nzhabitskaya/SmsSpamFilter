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

public class ContactsArrayAdapter extends ArrayAdapter<Call> {
    private final LayoutInflater mInflater;

    public ContactsArrayAdapter(Context context) {
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
        ((TextView)view.findViewById(R.id.name)).setText(item.getName());
        ((TextView)view.findViewById(R.id.phone)).setText(item.getPhone());
        ((TextView)view.findViewById(R.id.data)).setVisibility(View.INVISIBLE);

        return view;
    }
} 
