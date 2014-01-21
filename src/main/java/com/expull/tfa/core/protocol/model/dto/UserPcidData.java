package com.expull.tfa.core.protocol.model.dto;

public class UserPcidData {
	private String userId_pcId;
	
	public UserPcidData() {
	}
	
	public UserPcidData(String userId_pcId) {
		this.userId_pcId = userId_pcId;
	}

	public String getUserId_pcId() {
		return userId_pcId;
	}

	public void setUserId_pcId(String userId_pcId) {
		this.userId_pcId = userId_pcId;
	}
}
