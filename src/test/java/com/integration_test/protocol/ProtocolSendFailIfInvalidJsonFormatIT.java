package com.integration_test.protocol;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.json.JSONObject;

import org.junit.Assert;

import com.integration_test.base.ProtocolTestCase;
import com.integration_test.base.ProtocolTestCaseInfo;
import com.integration_test.client.ClientHelper;
import com.marlboro.common.ProtocolCommon;


public class ProtocolSendFailIfInvalidJsonFormatIT extends ProtocolTestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		for (ProtocolTestCaseInfo protocolTestCaseInfo : ProtocolTestCaseInfo.values())
			suite.addTest(new ProtocolSendFailIfInvalidJsonFormatIT(protocolTestCaseInfo));
		
		return suite;
	}
	
	public ProtocolSendFailIfInvalidJsonFormatIT(ProtocolTestCaseInfo protocolTestInfo) {
		super(protocolTestInfo);
	}

	@Override
	protected void runTest() throws Throwable {
		JSONObject received = ClientHelper.send(getProtocolTestCaseInfo().name(), "...");
		Assert.assertTrue(ClientHelper.isContainsKeys(received));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_INVALID_JSON_FORMAT, ClientHelper.getResultCode(received));
	}
}
