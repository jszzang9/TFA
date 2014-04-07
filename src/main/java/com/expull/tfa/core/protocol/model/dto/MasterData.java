package com.expull.tfa.core.protocol.model.dto;

public class MasterData {
	private String token;
	private String userid;
	private String contact;
	private String auth;
	private String exception;
	private String approval;
	private String pc;
	private String pc_limit;
	private String location;
	private String location_limit;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}
	public String getPc_limit() {
		return pc_limit;
	}
	public void setPc_limit(String pc_limit) {
		this.pc_limit = pc_limit;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation_limit() {
		return location_limit;
	}
	public void setLocation_limit(String location_limit) {
		this.location_limit = location_limit;
	}
}
