package com.marlboro.core.protocol.request;

import net.sf.json.JSONObject;

import com.marlboro.exception.MissingParameterException;

public class RequestTEST0002 extends Request{
	private String marlboroId;
	private String marlboroNumber;
	private String newPassWord;
	
	@Override
	public String getDesscription() {
		return "테스트 수정";
	}

	@Override
	public void setBodyData(JSONObject bodyData)
			throws MissingParameterException {
		if (bodyData.containsKey("marlboroid") == false)
			throw new MissingParameterException(getTelegramNumber(), "marlboroid");
		if (bodyData.containsKey("marlboronumber") == false)
			throw new MissingParameterException(getTelegramNumber(), "marlboronumber");
		if (bodyData.containsKey("newpassword") == false) 
			throw new MissingParameterException(getTelegramNumber(), "newpassword");
		
		setMarlboroId(bodyData.getString("marlboroid"));
		setMarlboroNumber(bodyData.getString("marlboronumber"));
		setNewPassWord(bodyData.getString("newpassword"));
		
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

	public String getNewPassWord() {
		return newPassWord;
	}

	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}
}
