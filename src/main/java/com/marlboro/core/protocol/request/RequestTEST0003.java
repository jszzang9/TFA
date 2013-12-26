package com.marlboro.core.protocol.request;

import net.sf.json.JSONObject;

import com.marlboro.exception.MissingParameterException;

public class RequestTEST0003 extends Request{
	private String marlboroId;
	private String marlboroNumber;
	
	@Override
	public String getDesscription() {
		return "Test 삭제";
	}

	@Override
	public void setBodyData(JSONObject bodyData)
			throws MissingParameterException {
		if (bodyData.containsKey("marlboroid") == false)
			throw new MissingParameterException(getTelegramNumber(), "marlboroid");
		if (bodyData.containsKey("marlboronumber") == false)
			throw new MissingParameterException(getTelegramNumber(), "marlboronumber");
		
		setMarlboroId(bodyData.getString("marlboroid"));
		setMarlboroNumber(bodyData.getString("marlboronumber"));
	}

	public String getMarlboroId() {
		return marlboroId;
	}

	public void setMarlboroId(String marlboroId) {
		this.marlboroId = marlboroId;
	}

	public String getMarlboroNumber() {
		return marlboroNumber;
	}

	public void setMarlboroNumber(String marlboroNumber) {
		this.marlboroNumber = marlboroNumber;
	}
}
