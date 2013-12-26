package com.integration_test.protocol;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.integration_test.client.ClientHelper;
import com.marlboro.common.ProtocolCommon;

public class ProtocolSendTest2DataIT extends ProtocolIT{
	
	@Test @Ignore
	public void success() throws Exception {
		String id = makeRandomString();
		String number = makeRandomString();
		
		JSONObject received1 = ClientHelper.send("TEST0001", 
				makeBody("marlboroid", id, "marlboropw", makeRandomString(), "marlboronumber", number));
		Assert.assertTrue(ClientHelper.isContainsKeys(received1));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_SUCCESS, ClientHelper.getResultCode(received1));
		
		
		JSONObject received2 = ClientHelper.send("TEST0002", 
				makeBody("marlboroid", id, "newpassword", "asdf", "marlboronumber", number));
		Assert.assertTrue(ClientHelper.isContainsKeys(received2));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_SUCCESS, ClientHelper.getResultCode(received2));
	}
}
