package com.expull.tfa.exception;

import com.expull.tfa.common.ProtocolCommon;

/**
 * 요청 데이터가 JSON 형식이 아닐 때 발생하는 예외 클래스이다.
 * 
 * @author delta829
 */
public class InvalidJsonFormatException extends MrsException {
	private static final long serialVersionUID = 265201635147491991L;

	/**
	 * {@link InvalidJsonFormatException} 생성자.
	 * 
	 * @param telegramNumber 전문 번호.
	 */
	public InvalidJsonFormatException(String telegramNumber) {
		super(telegramNumber);
	}

	@Override
	public String getResultCode() {
		return ProtocolCommon.RESULT_CODE_INVALID_JSON_FORMAT;
	}

	@Override
	public String getResultMessage() {
		return ProtocolCommon.RESULT_MESSAGE_INVALID_JSON_FORMAT;
	}
}
