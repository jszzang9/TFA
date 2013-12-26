package com.marlboro.exception;

import com.marlboro.common.ProtocolCommon;



public class InvalidJsonFormatException extends MarlboroException {

	private static final long serialVersionUID = -3754291717655177706L;

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
