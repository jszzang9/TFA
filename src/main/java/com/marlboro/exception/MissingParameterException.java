package com.marlboro.exception;

import com.marlboro.common.ProtocolCommon;




/**
 * 요청 데이터가 연동 규격서에 맞지 않게 되어 있을 때 발생하는 예외 클래스이다.
 *
 * @author delta829
 */
public class MissingParameterException extends MarlboroException {
	
	private static final long serialVersionUID = 7319496259362383212L;
	private String missingParam = null;

	/**
	 * {@link MissingParameterException} 생성자.
	 * 
	 * @param telegramNumber 전문 번호.
	 */
	public MissingParameterException(String telegramNumber) {
		super(telegramNumber);
	}

	/**
	 * {@link MissingParameterException} 생성자.
	 * 
	 * @param telegramNumber 전문 번호.
	 * @param missingParam 빠진 파라미터.
	 */
	public MissingParameterException(String telegramNumber, String missingParam) {
		super(telegramNumber);
		this.missingParam = missingParam;
	}

	@Override
	public String getResultCode() {
		return ProtocolCommon.RESULT_CODE_MISSING_PARAMETER;
	}

	@Override
	public String getResultMessage() {
		return ProtocolCommon.RESULT_MESSAGE_MISSING_PARAMETER + (this.missingParam != null ? ", " + this.missingParam : "");
	}
}
