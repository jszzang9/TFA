package com.marlboro.core.protocol.request;

import com.marlboro.common.ProtocolCommon;
import com.marlboro.exception.MissingParameterException;

import net.sf.json.JSONObject;


public abstract class Request {
private String telegramNumber;
	
	/**
	 * {@link Request} 생성자.
	 */
	public Request() {
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
	 * 요청 클래스의 설명을 가져온다.
	 * 
	 * @return 요청 클래스의 설명.
	 */
	public abstract String getDesscription();
	
	/**
	 * 바디 데이터를 설정한다.
	 * 
	 * @param bodyData 바디 데이터.
	 * @throws MissingParameterException
	 */
	public abstract void setBodyData(JSONObject bodyData) throws MissingParameterException;
}
