package com.marlboro.core.protocol.request;

import net.sf.json.JSONObject;

import com.marlboro.exception.MissingParameterException;

public class RequestDUMMY000 extends Request{

	@Override
	public String getDesscription() {
		return "더미";
	}

	@Override
	public void setBodyData(JSONObject bodyData)
			throws MissingParameterException {
		
	}
}
