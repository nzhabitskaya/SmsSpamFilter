package com.mobile.android.smsspamfilter.data;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import com.mobile.android.smsspamfilter.utils.DateUtils;


public class Call {
	private String name;
	private String phone;
	private String time;
	
	public Call(String name, String phone, String time){
		this.name = name;
		this.phone = phone;
		this.time = DateUtils.convertTime(time);
	}
	
	public Call(String name, String phone){
		this.name = name;
		this.phone = phone;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
