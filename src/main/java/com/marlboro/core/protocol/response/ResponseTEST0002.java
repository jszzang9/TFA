package com.marlboro.core.protocol.response;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.marlboro.common.ProtocolCommon;

public class ResponseTEST0002 extends Response{
	private String newPassWord;
	
	public String getNewPassWord() {
		return newPassWord;
	}

	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}

	@Override
	public JSONObject getBodyData() {
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put(ProtocolCommon.RESULT_CODE_KEY, getResultCode());
		bodyMap.put(ProtocolCommon.RESULT_MESSAGE_KEY, getResultMessage());
		bodyMap.put("datalist", getNewPassWord());

		return JSONObject.fromObject(bodyMap);
	}
}
