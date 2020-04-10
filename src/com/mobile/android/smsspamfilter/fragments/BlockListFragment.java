package com.mobile.android.smsspamfilter.fragments;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.mobile.android.smsspamfilter.R;
import com.mobile.android.smsspamfilter.adapter.BlockedSmsAdapter;
import com.mobile.android.smsspamfilter.dialog.FromCallHistoryDialog;
import com.mobile.android.smsspamfilter.dialog.FromContactsDialog;
import com.mobile.android.smsspamfilter.dialog.FromSmsBodyDialog;
import com.mobile.android.smsspamfilter.dialog.FromSmsNumberDialog;
import com.mobile.android.smsspamfilter.dialog.RegexpDialog;
import com.mobile.android.smsspamfilter.wrapper.DataProvider;

public class BlockListFragment extends ListFragment{
	private ListView listView;
	private BlockedSmsAdapter mAdapter;
	private RegexpDialog regexpDialog;
	private FromSmsBodyDialog fromSmsBodyDialog;
	private FromSmsNumberDialog fromSmsNumberDialog;
	private FromCallHistoryDialog fromCallHistoryDialog;
	private FromContactsDialog fromContactsDialog;
	
	private Button addNewButton;
	private Button fromSmsNumberButton;
	private Button fromCallHistoryButton;
	private Button fromSmsBodyButton;
	private Button fromContactsButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.allow_list_fragment, container, false);
		listView = (ListView) view.findViewById(android.R.id.list);
		mAdapter = new BlockedSmsAdapter(getActivity(), R.layout.allow_sms_item);
		listView.setAdapter(mAdapter);
		
		addNewButton = (Button) view.findViewById(R.id.btn_add_new);
		addNewButton.setOnClickListener(listener);
		fromSmsNumberButton = (Button) view.findViewById(R.id.btn_from_sms_number);
		fromSmsNumberButton.setOnClickListener(listener);
		fromCallHistoryButton = (Button) view.findViewById(R.id.btn_from_call_history);
		fromCallHistoryButton.setOnClickListener(listener);
		fromContactsButton = (Button) view.findViewById(R.id.btn_from_contacts);
		fromContactsButton.setOnClickListener(listener);
		fromSmsBodyButton = (Button) view.findViewById(R.id.btn_from_sms_body);
		fromSmsBodyButton.setOnClickListener(listener);

		return view;
	}
	
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_add_new:
				if(regexpDialog == null)
					regexpDialog = new RegexpDialog(getActivity(), mAdapter, DataProvider.BLOCKED_CONTENT_URI);
				regexpDialog.setType(0);
				regexpDialog.setValue("");
				regexpDialog.show();
				break;
			case R.id.btn_from_sms_number:
				if(fromSmsNumberDialog == null)
					fromSmsNumberDialog = new FromSmsNumberDialog(getActivity(), mAdapter, DataProvider.BLOCKED_CONTENT_URI);
				fromSmsNumberDialog.show();
				break;
			case R.id.btn_from_call_history:
				if(fromCallHistoryDialog == null)
					fromCallHistoryDialog = new FromCallHistoryDialog(getActivity(), mAdapter, DataProvider.BLOCKED_CONTENT_URI);
				fromCallHistoryDialog.show();
				break;
			case R.id.btn_from_sms_body:
				if(fromSmsBodyDialog == null)
					fromSmsBodyDialog = new FromSmsBodyDialog(getActivity(), mAdapter, DataProvider.BLOCKED_CONTENT_URI);
				fromSmsBodyDialog.show();
				break;
			case R.id.btn_from_contacts:
				if(fromContactsDialog == null)
					fromContactsDialog = new FromContactsDialog(getActivity(), mAdapter, DataProvider.BLOCKED_CONTENT_URI);
				fromContactsDialog.show();
				break;
			}
		}
	};
	
	@Override
	public void onPause() {
		super.onPause();
		mAdapter = null;
		regexpDialog = null;
		fromSmsBodyDialog = null;
		fromSmsNumberDialog = null;
		fromCallHistoryDialog = null;
		fromContactsDialog = null;
	}
}