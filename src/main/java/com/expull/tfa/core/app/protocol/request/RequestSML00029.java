package com.expull.tfa.core.app.protocol.request;

import net.sf.json.JSONObject;

import com.expull.tfa.core.app.protocol.handler.HandlerSML00029;
import com.expull.tfa.exception.MissingParameterException;

/**
 * {@link HandlerSML00029}에서 사용하는 요청 클래스이다.
 * 
 * @author delta829
 */
public class RequestSML00029 extends SimpleRequest {

	@Override
	protected void checkParameter(JSONObject bodyData) throws MissingParameterException {
		if (bodyData.containsKey("packetid") == false)
			throw new MissingParameterException(getTelegramNumber(), "packetid");
		if (bodyData.containsKey("upfolder") == false)
			throw new MissingParameterException(getTelegramNumber(), "upfolder");
	}
}
