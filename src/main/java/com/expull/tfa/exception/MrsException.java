package com.expull.tfa.exception;

import com.expull.tfa.core.app.protocol.response.Response;
import com.expull.tfa.core.app.protocol.response.ResponseDUMMY000;

/**
 * MRS 서비스에서 발생하는 추상 예외 클래스이다.
 * 
 * @author delta829
 */
public abstract class MrsException extends Exception {
	private static final long serialVersionUID = 1994463492472728132L;
	
	private String telegramNumber = "";
	
	/**
	 * {@link MrsException} 생성자.
	 */
	public MrsException() {
		setTelegramNumber("");
	}
	
	/**
	 * {@link MrsException} 생성자.
	 * 
	 * @param telegramNumber 전문 번호.
	 */
	public MrsException(String telegramNumber) {
		setTelegramNumber(telegramNumber);
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
	}	
	
	/**
	 * 에러 메시지를 생성한다.
	 * 
	 * @param lastTelegramNumber 마지막 전문 번호.
	 * @return 에러 메시지.
	 */
	public Response createErrorResponse(String lastTelegramNumber) {
		Response response = new ResponseDUMMY000();
		response.setResultCode(getResultCode());
		response.setResultMessage(getResultMessage());
		if (getTelegramNumber().length() > 0)
			response.setTelegramNumber(getTelegramNumber());
		else if (lastTelegramNumber != null)
			response.setTelegramNumber(lastTelegramNumber);
		
		return response;
	}
	
	/**
	 * 결과 코드를 가져온다.
	 * 
	 * @return 결과 코드.
	 */
	public abstract String getResultCode();
	
	/**
	 * 결과 메시지를 가져온다.
	 * @return 결과 메시지.
	 */
	public abstract String getResultMessage();
}
