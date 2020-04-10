package com.mobile.android.smsspamfilter.data;
/*
superslon74@gmail.com
skype - superslon74
schamanskij gennadij aleksandrovich
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataManager {
	private static DataManager manager;
	
	/** Call history dialog data */
	private List<Call> calls;

	/** Contact map to get contact name */
	private Map<String, String> contactMap;
	private List<Call> contacts;
	
	/** Find phone by threadID */
	private Map<Long, String> phoneByThreadID;
	private Map<String, Long> threadIdByPhone;
	
	/** Recepient address by id */
	private Map<Long, String> recepientByID;

	/** Cache sms groups */
	private List<Sms> groups;

	DataManager(){
		calls = new ArrayList<Call>();
		contactMap = new HashMap<String, String>();
		contacts = new ArrayList<Call>();
		phoneByThreadID = new HashMap<Long, String>();
		threadIdByPhone = new HashMap<String, Long>();
		recepientByID = new HashMap<Long, String>();
		groups = new ArrayList<Sms>();
	}
	
	public static DataManager getInstance(){
		if(manager == null)
			manager = new DataManager();
		return manager;
	}

	public List<Call> getCalls() {
		return calls;
	}

	public void setCalls(List<Call> callList) {
		this.calls = callList;
	}

	public Set<String> getContactSet() {
		return contactMap.keySet();
	}

	public Map<String, String> getContactMap() {
		return contactMap;
	}
	
	public Map<String, String> getPhoneByThreadID() {
		return contactMap;
	}

	public void setContactMap(Map<String, String> contactMap) {
		this.contactMap = contactMap;
	}
	
	public String getAddressByPhone(String phone) {
		if(contactMap.containsKey(phone)){
			return contactMap.get(phone);
		}
		return "";
	}

	public List<Call> getContacts() {
		return contacts;
	}

	public void setContacts(List<Call> contactList) {
		this.contacts = contactList;
	}
	
	public void resetContacts() {
		this.contacts.clear();
	}
	
	public void addContact(Call contact) {
		String key = contact.getPhone();
		if(!contactMap.containsKey(key)){
			contactMap.put(key, contact.getName());
		}
	}
	
	public void insertPhoneByThreadID(long threadID, String phone) {
		if(!phoneByThreadID.containsKey(threadID)){
			phoneByThreadID.put(threadID, phone);
			threadIdByPhone.put(phone, threadID);
		}
	}
	
	public String getPhoneByThreadID(long threadID) {
		if(phoneByThreadID.containsKey(threadID)){
			return phoneByThreadID.get(threadID);
		}
		return "";
	}
	
	public boolean isExistThreadID(String phone) {
		if(threadIdByPhone.containsKey(phone)){
			return true;
		}
		return false;
	}
	
	public long getThreadIdByPhone(String phone) {
		return threadIdByPhone.get(phone);
	}

	public List<Sms> getGroups() {
		return groups;
	}

	public void setGroups(List<Sms> groups) {
		this.groups = groups;
	}

	public Map<Long, String> getRecepientByID() {
		return recepientByID;
	}

	public void setRecepientByID(Map<Long, String> recepientByID) {
		this.recepientByID = recepientByID;
	}
	
	public void insertRecepientByID(long id, String phone) {
		if(!recepientByID.containsKey(id)){
			recepientByID.put(id, phone);
		}
	}
	
	public String getRecepientByID(long id) {
		if(recepientByID.containsKey(id)){
			return recepientByID.get(id);
		}
		return "";
	}
}
