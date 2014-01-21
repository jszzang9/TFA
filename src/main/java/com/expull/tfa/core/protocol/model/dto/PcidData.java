package com.expull.tfa.core.protocol.model.dto;

public class PcidData {
	private String pcid;
	private String lid;
	
	public PcidData() {
	}
	
	public PcidData(String pcid, String lid) {
		this.pcid = pcid;
		this.lid = lid;
	}
	
	public String getPcid() {
		return pcid;
	}
	
	public void setPcid(String pcid) {
		this.pcid = pcid;
	}
	
	public String getLid() {
		return lid;
	}
	
	public void setLid(String lid) {
		this.lid = lid;
	}
}
