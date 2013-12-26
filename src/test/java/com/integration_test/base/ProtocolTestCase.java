package com.integration_test.base;

import junit.framework.TestCase;


public abstract class ProtocolTestCase extends TestCase{
	private ProtocolTestCaseInfo protocolTestInfo;

	public ProtocolTestCase(ProtocolTestCaseInfo protocolTestInfo) {
		this.protocolTestInfo = protocolTestInfo;

		setName(getClass().getSimpleName() + "[" + protocolTestInfo.name() + "] ");
	}

	public ProtocolTestCaseInfo getProtocolTestCaseInfo() {
		return protocolTestInfo;
	}
}
