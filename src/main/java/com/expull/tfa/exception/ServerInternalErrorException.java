package com.expull.tfa.exception;

import com.expull.tfa.common.ProtocolCommon;

/**
 * 서버 내부 에러 예외 클래스이다.
 * 
 * @author delta829
 */
public class ServerInternalErrorException extends MrsException {
	private static final long serialVersionUID = 5064994379411846213L;
	
	/**
	 * {@link ServerInternalErrorException} 생성자.
	 */
	public ServerInternalErrorException() {
	}
	
	/**
	 * {@link ServerInternalErrorException} 생성자.
	 * 
	 * @param telegramNumber 전문번호.
	 */
	public ServerInternalErrorException(String telegramNumber) {
		super(telegramNumber);
	}

	@Override
	public String getResultCode() {
		return ProtocolCommon.RESULT_CODE_SEVER_INTERNAL_ERROR;
	}

	@Override
	public String getResultMessage() {
		return ProtocolCommon.RESULT_MESSAGE_SERVER_INTERNAL_ERROR;
	}
}
