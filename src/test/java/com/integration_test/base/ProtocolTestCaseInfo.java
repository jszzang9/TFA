package com.integration_test.base;

public enum ProtocolTestCaseInfo {
	DUMMY000(false),
	TEST0001(true),	TEST0002(true),	TEST0003(true),
	
	;
	
	private boolean isExistParam;

	private ProtocolTestCaseInfo(boolean isExistParam) {
		this.isExistParam = isExistParam;
	}
	
	public boolean isExistParam() {
		return isExistParam;
	}

	public void setExistParam(boolean isExistParam) {
		this.isExistParam = isExistParam;
	}
}
