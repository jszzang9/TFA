package com.integration_test.protocol;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import com.integration_test.client.ClientHelper;
import com.marlboro.common.ProtocolCommon;

public class ProtocolSendTestDataIT extends ProtocolIT{
	
	@Test
	public void success() throws Exception {
		JSONObject received1 = ClientHelper.send("TEST0001", 
				makeBody("marlboroid", makeRandomString(), "marlboropw", makeRandomString(), "marlboronumber", makeRandomString()));
		Assert.assertTrue(ClientHelper.isContainsKeys(received1));
		Assert.assertEquals(ProtocolCommon.RESULT_CODE_SUCCESS, ClientHelper.getResultCode(received1));
	}
}
