package com.expull.tfa.util;

import com.expull.tfa.common.ProtocolCommon;

/**
 * Json 형식의 응답 객체 클래스이다.
 * 
 * @author delta829
 */
public class JsonResponse {
	private final String message;
	private final String code;

	/**
	 * {@link JsonResponse} 생성자.
	 */
	public JsonResponse() {
		this.code = ProtocolCommon.RESULT_CODE_SUCCESS;
		this.message = ProtocolCommon.RESULT_MESSAGE_SUCCESS;
	}

	/**
	 * {@link JsonResponse} 생성자.
	 * 
	 * @param code 코드.
	 * @param message 메시지.
	 */
	public JsonResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return "{\"resultcode\":\""+code+"\", \"resultmessage\":\""+message+"\"}";
	}

	/**
	 * 응답 코드를 가져온다.
	 * 
	 * @return 응답 코드.
	 */
	public String getResultCode() {
		return this.code;
	}

	/**
	 * 응답 메시지를 가져온다.
	 * 
	 * @return 응답 메시지.
	 */
	public String getResultMessage() {
		return this.message;
	}
}
