package com.expull.tfa.core.app.protocol.request;

import net.sf.json.JSONObject;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.exception.MissingParameterException;

/**
 * 요청 추상 클래스이다.
 * 
 * @author delta829
 */
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
	 * 바디 데이터를 설정한다.
	 * 
	 * @param bodyData 바디 데이터.
	 * @throws MissingParameterException
	 */
	public abstract void setBodyData(JSONObject bodyData) throws MissingParameterException;
}
