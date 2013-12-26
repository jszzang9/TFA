package com.integration_test.client;

import java.net.InetAddress;

import com.marlboro.MarlboroBorn;
import com.marlboro.common.ProtocolCommon;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class ClientHelper {

	public static JSONObject send(String telegramNumber, String bodyData) throws Exception {
		Client client = new Client(InetAddress.getLocalHost().getHostAddress(), MarlboroBorn.DEFAULT_PORT);
		client.connect();
		JSONObject received = client.send(telegramNumber, bodyData);
		client.disconnect();
		
		return received;
	}

	public static boolean isContainsKeys(JSONObject received, String ... otherKeys) {
		if (received == null)
			return false;
		if (received.containsKey(ProtocolCommon.RESULT_CODE_KEY) == false ||
			received.containsKey(ProtocolCommon.RESULT_MESSAGE_KEY) == false)
			return false;
		
		for (String otherKey : otherKeys) {
			if (received.containsKey(otherKey) == false)
				return false;
		}

		return true;
	}
	
	public static String getStringValue(JSONObject received, String key) {
		if (received == null)
			return "";
		
		return received.getString(key);
	}
	
	public static JSONObject getObjectValue(JSONObject received, String key) {
		if (received == null)
			return new JSONObject();
		
		return received.getJSONObject(key);
	}
	
	public static JSONArray getListValue(JSONObject received, String key) {
		if (received == null)
			return new JSONArray();
		
		return received.getJSONArray(key);
	}
	
	public static String getResultCode(JSONObject received) {
		return getStringValue(received, ProtocolCommon.RESULT_CODE_KEY);
	}
	
	public static String getResultMessage(JSONObject received) {
		return getStringValue(received, ProtocolCommon.RESULT_MESSAGE_KEY);
	}
	
	public static String getResultMessageValue(JSONObject received, String key) {
		if (received == null)
			return "";
		
		return JSONObject.fromObject(received.getString(ProtocolCommon.RESULT_MESSAGE_KEY)).getString(key);
	}
}
