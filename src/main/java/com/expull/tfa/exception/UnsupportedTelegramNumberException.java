package com.expull.tfa.exception;

import com.expull.tfa.common.ProtocolCommon;

/**
 * 요청 클래스가 존재하지 않을 때 발생하는 예외 클래스이다.
 * 
 * @author delta829
 */
public class UnsupportedTelegramNumberException extends MrsException {
	private static final long serialVersionUID = 6929077944385975012L;
	
	/**
	 * {@link UnsupportedTelegramNumberException} 생성자.
	 * 
	 * @param telegramNumber 전문 번호.
	 */
	public UnsupportedTelegramNumberException(String telegramNumber) {
		super(telegramNumber);
	}
	
	@Override
	public String getResultCode() {
		return ProtocolCommon.RESULT_CODE_UNSUPPORTED_TELEGRAM_NUMBER;
	}

	@Override
	public String getResultMessage() {
		return ProtocolCommon.RESULT_MESSAGE_UNSUPPORTED_TELEGRAM_NUMBER;
	}
}
