package com.marlboro.core.protocol.response;

import java.util.HashMap;
import java.util.Map;

import com.marlboro.common.ProtocolCommon;
import com.marlboro.util.JsonGenerator;

import net.sf.json.JSONObject;


public abstract class Response {
	private String telegramNumber;
	private String resultCode = ProtocolCommon.RESULT_CODE_SUCCESS;
	private String resultMessage = ProtocolCommon.RESULT_MESSAGE_SUCCESS;

	/**
	 * {@link Response} 생성자.
	 */
	public Response() {
		telegramNumber = ProtocolCommon.createWhiteSpaceString(ProtocolCommon.TELEGRAM_NUMBER_SIZE);
	}
	
	/**
	 * 전문 번호를 가져온다.
	 * 
	 * @return 전문 번호.
	 */
	public String getTelegramNumber() {
		return telegramNumber;
	}

	/**
	 * 전문 번호를 설정한다.
	 * 
	 * @param telegramNumber 전문 번호.
	 */
	public void setTelegramNumber(String telegramNumber) {
		this.telegramNumber = telegramNumber;
		
		if (this.telegramNumber.length() < ProtocolCommon.TELEGRAM_NUMBER_SIZE)
			this.telegramNumber += ProtocolCommon.createWhiteSpaceString(ProtocolCommon.TELEGRAM_NUMBER_SIZE-this.telegramNumber.length());
		else if (this.telegramNumber.length() > ProtocolCommon.TELEGRAM_NUMBER_SIZE)
			this.telegramNumber = telegramNumber.substring(0, ProtocolCommon.TELEGRAM_NUMBER_SIZE);
	}

	/**
	 * 결과 코드를 가져온다.
	 * 
	 * @return 결과 코드.
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * 결과 코드를 설정한다.
	 * 
	 * @param resultCode 결과 코드.
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 결과 메시지를 가져온다.
	 * 
	 * @return 결과 메시지.
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * 결과 메시지를 설정한다.
	 * 
	 * @param resultMessage 결과 메시지.
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	/**
	 * 바디 데이터를 가져온다.
	 * 
	 * @return 바디 데이터.
	 */
	public JSONObject getBodyData() {
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		bodyMap.put(ProtocolCommon.RESULT_CODE_KEY, getResultCode());
		bodyMap.put(ProtocolCommon.RESULT_MESSAGE_KEY, (JsonGenerator.mayBeJSON(getResultMessage()) ? JSONObject.fromObject(getResultMessage()) : getResultMessage()));

		return JSONObject.fromObject(bodyMap);
	}
}
