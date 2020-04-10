package com.mobile.android.smsspamfilter.dialog;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.android.smsspamfilter.R;

public class AboutDialogActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transparent_layout);

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.action_about));
		LinearLayout layout = new LinearLayout(this);
		LayoutParams params = new LayoutParams();
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(5, 2, 5, 2);
		layout.setLayoutParams(params);
		TextView view1 = new TextView(this);
		view1.setText(R.string.app_name_full);
		TextView view2 = new TextView(this);
		view2.setText(getString(R.string.version, getVersion()));
		layout.addView(view1);
		layout.addView(view2);
		alert.setView(layout);
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		alert.create();
		alert.show();
	}

	private String getVersion(){
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
