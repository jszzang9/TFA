package com.marlboro.exception;

import com.marlboro.common.ProtocolCommon;


/**
 * 요청한 데이터가 존재하지 않을 때 발생하는 예외 클래스이다.
 * 
 * @author delta829
 */
public class NoDataException extends MarlboroException {
	private static final long serialVersionUID = 7412185750784614910L;
	private String data = "";
	
	public NoDataException(String data) {
		this.data = data;
	}

	@Override
	public String getResultCode() {
		return ProtocolCommon.RESULT_CODE_NO_DATA;
	}

	@Override
	public String getResultMessage() {
		return ProtocolCommon.RESULT_MESSAGE_NO_DATA + (data.length() > 0 ? ", " + data : "");
	}
}
