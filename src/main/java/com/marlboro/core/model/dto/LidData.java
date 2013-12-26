package com.marlboro.core.model.dto;

public class LidData {
	private String lid;
	private String mac;
	
	public LidData() {
	}
	
	public LidData(String lid, String mac) {
		this.lid = lid;
		this.mac = mac;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
}
