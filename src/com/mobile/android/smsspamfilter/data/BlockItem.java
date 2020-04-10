package com.mobile.android.smsspamfilter.data;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.io.Serializable;

public class BlockItem implements Serializable {
	private static final long serialVersionUID = 622458333530462937L;
	public static final int TXT = 0;
	public static final int NUM = 1;

	
	private long id;
	private boolean isChecked = false;
	private int type = TXT;
	private String value;
	
	public BlockItem(long id, int type, boolean isChecked, String value){
		this.id = id;
		this.isChecked = isChecked;
		this.type = type;
		this.value = value;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
