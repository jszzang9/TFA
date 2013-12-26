package com.marlboro.core.model.dto;

public class TestData {
	private String primaryKey;
	private String marlboroId;
	private String marlboroPw;
	private String marlboroNumber;
	
	public TestData() {
	}
	
	public TestData(String marlboroId, String marlboroPw, String marlboroNumber) {
		this.marlboroId = marlboroId;
		this.marlboroPw = marlboroPw;
		this.marlboroNumber = marlboroNumber;
	}
	
	public String getPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public String getMarlboroId() {
		return marlboroId;
	}
	
	public void setMarlboroId(String marlboroId) {
		this.marlboroId = marlboroId;
	}
	
	public String getMarlboroPw() {
		return marlboroPw;
	}
	
	public void setMarlboroPw(String marlboroPw) {
		this.marlboroPw = marlboroPw;
	}

	public String getMarlboroNumber() {
		return marlboroNumber;
	}

	public void setMarlboroNumber(String marlboroNumber) {
		this.marlboroNumber = marlboroNumber;
	}
}
