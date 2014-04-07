package com.expull.tfa.common;

import java.util.Arrays;

/**
 * 프로토콜 공통 클래스이다.
 * 
 * @author delta829
 */
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
	
	public static final String RESULT_CODE_AUTH_TIMEOVER = "401";
	public static final String RESULT_CODE_AUTH_FAIL = "402";
	public static final String RESULT_CODE_NO_PERMISSION = "403";
	public static final String RESULT_CODE_EMERGENCY_MODE_TIMEOVER = "404";
	public static final String RESULT_CODE_NO_ROWEMID = "411";
	public static final String RESULT_CODE_NO_SERVICEID = "412";
	public static final String RESULT_CODE_NO_PCID = "413";
	public static final String RESULT_CODE_NO_PACKETID = "414";
	public static final String RESULT_CODE_INVALID_PASSWORD_OR_KEY = "415";
	public static final String RESULT_CODE_DUPLICATED_CREATION = "416";
	public static final String RESULT_CODE_DUPLICATED_PERMISSION = "417";
	public static final String RESULT_CODE_NO_DATA = "421";
	public static final String RESULT_CODE_ACCESS_LOCKED_DATA = "422";
	public static final String RESULT_CODE_REQUEST_PUSH_FAIL = "431";
	public static final String RESULT_CODE_NO_BINDING_PC_AGENT = "432";
	
	public static final String RESULT_MESSAGE_SUCCESS = "success";
	public static final String RESULT_MESSAGE_MISSING_PARAMETER = "missing parameter";
	public static final String RESULT_MESSAGE_INVALID_JSON_FORMAT = "invalid json format";
	public static final String RESULT_MESSAGE_UNSUPPORTED_TELEGRAM_NUMBER = "unsupported telegram number";
	public static final String RESULT_MESSAGE_SERVER_INTERNAL_ERROR = "server internal error";
	
	public static final String RESULT_MESSAGE_AUTH_TIMEOVER = "auth timeover";
	public static final String RESULT_MESSAGE_AUTH_FAIL = "auth fail";
	public static final String RESULT_MESSAGE_NO_PERMISSION = "no permission";
	public static final String RESULT_MESSAGE_EMERGENCY_MODE_TIMEOVER = "emergency mode timeover";
	public static final String RESULT_MESSAGE_NO_ROWEMID = "no rowemid";
	public static final String RESULT_MESSAGE_NO_SERVICEID = "no serviceid";
	public static final String RESULT_MESSAGE_NO_PCID = "no pcid";
	public static final String RESULT_MESSAGE_NO_PACKETID = "no packetid";
	public static final String RESULT_MESSAGE_INVALID_PASSWORD_OR_KEY = "invalid password or key";
	public static final String RESULT_MESSAGE_DUPLICATED_CREATION = "duplicated creation";
	public static final String RESULT_MESSAGE_DUPLICATED_PERMISSION = "duplicated permission";
	public static final String RESULT_MESSAGE_NO_DATA = "no data";
	public static final String RESULT_MESSAGE_ACCESS_LOCKED_DATA = "access locked data";
	public static final String RESULT_MESSAGE_REQUEST_PUSH_FAIL = "request push fail";
	public static final String RESULT_MESSAGE_NO_BINDING_PC_AGENT = "no binding pc agent";

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
	
	/**
	 * pid, lid 를 받아 채널 id 를 생성한다.
	 * */
	public static String buildChannelIDFor(String pid, String lid) {
		return (pid+"@"+lid).trim();
	}
}
