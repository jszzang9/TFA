package com.expull.tfa.core.protocol.model.dto;

public class UserData {
	private String uid;
	private String pid;
	
	public UserData() {
	}
	
	public UserData(String uid, String pid) {
		this.uid = uid;
		this.pid = pid;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
}
