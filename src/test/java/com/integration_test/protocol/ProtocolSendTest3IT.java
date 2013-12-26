package com.integration_test.protocol;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import com.integration_test.client.ClientHelper;
import com.marlboro.common.ProtocolCommon;

public class ProtocolSendTest3IT extends ProtocolIT{
	@Test
	public void success() throws Exception {
		String id = makeRandomString();
		String pw = makeRandomString();
		String number = makeRandomString();
		
		JSONObject received1 = ClientHelper.send("TEST0001", 
				makeBody("marlboroid", id, "marlboropw", pw, "marlboronumber", number));
		Assert.assertTrue(ClientHelper.isContainsKeys(received1));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_SUCCESS, ClientHelper.getResultCode(received1));
		
		
		JSONObject received2 = ClientHelper.send("TEST0003", 
				makeBody("marlboroid", id, "marlboronumber", number));
		Assert.assertTrue(ClientHelper.isContainsKeys(received2));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_SUCCESS, ClientHelper.getResultCode(received2));
	}
}
