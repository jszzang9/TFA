package com.marlboro.core.protocol.request;

import org.apache.log4j.Level;

import net.sf.json.JSONObject;

import com.marlboro.exception.MissingParameterException;
import com.marlboro.util.QueuedLogger.QueuedLogger;

public class RequestTEST0001 extends Request{
	private String marlboroId;
	private String marlboroPw;
	private String marlboroNumber;
	
	@Override
	public String getDesscription() {
		return "테스트 생성";
	}

	@Override
	public void setBodyData(JSONObject bodyData)
			throws MissingParameterException {
		if (bodyData.containsKey("marlboroid") == false) 
			throw new MissingParameterException(getTelegramNumber(), "marlboroid");
		if (bodyData.containsKey("marlboropw") == false)
			throw new MissingParameterException(getTelegramNumber(), "marlboropw");
		if (bodyData.containsKey("marlboronumber") == false)
			throw new MissingParameterException(getTelegramNumber(), "marlboronumber");
		
		QueuedLogger.push(Level.INFO, "MARLBOROID" + bodyData.getString("marlboroid"));
		
		setMarlboroId(bodyData.getString("marlboroid"));
		setMarlboroPw(bodyData.getString("marlboropw"));
		setMarlboroNumber(bodyData.getString("marlboronumber"));
	}

	public String getMarlboroId() {
		return marlboroId;
	}

	public void setMarlboroId(String marlboroId) {
		this.marlboroId = marlboroId;
	}

	public String getMarlboroPw() {
		return marlboroPw;
	}

	public void setMarlboroPw(String marlboroPw) {
		this.marlboroPw = marlboroPw;
	}

	public String getMarlboroNumber() {
		return marlboroNumber;
	}

	public void setMarlboroNumber(String marlboroNumber) {
		this.marlboroNumber = marlboroNumber;
	}
}
