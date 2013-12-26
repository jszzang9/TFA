package com.marlboro.common;

import java.util.Arrays;

public class ProtocolCommon {
	public static final int BODY_LENGTH_SIZE = 4;
	public static final int TELEGRAM_NUMBER_SIZE = 8;
	
	public static final String RESULT_CODE_KEY = "resultcode";
	public static final String RESULT_MESSAGE_KEY = "resultmessage";

	public static final String RESULT_CODE_SUCCESS = "0";
	public static final String RESULT_CODE_MISSING_PARAMETER = "1";
	public static final String RESULT_CODE_INVALID_JSON_FORMAT = "2";
	public static final String RESULT_CODE_UNSUPPORTED_TELEGRAM_NUMBER = "3";
	public static final String RESULT_CODE_SEVER_INTERNAL_ERROR = "9";
	public static final String RESULT_CODE_NO_DATA = "10";
	
	public static final String RESULT_MESSAGE_SUCCESS = "success";
	public static final String RESULT_MESSAGE_MISSING_PARAMETER = "missing parameter";
	public static final String RESULT_MESSAGE_INVALID_JSON_FORMAT = "invalid json format";
	public static final String RESULT_MESSAGE_UNSUPPORTED_TELEGRAM_NUMBER = "unsupported telegram number";
	public static final String RESULT_MESSAGE_SERVER_INTERNAL_ERROR = "server internal error";
	public static final String RESULT_MESSAGE_NO_DATA = "No Data";
	
	/**
	 * 해당 길이 만큼 공백을 포함한 문자열을 생성한다.
	 * 
	 * @param length 길이.
	 * @return 공백을 포함한 문자열.
	 */
	public static String createWhiteSpaceString(int length) {
		char[] empty = new char[length];
		Arrays.fill(empty, ' ');

		return new String(empty);
	}
}
