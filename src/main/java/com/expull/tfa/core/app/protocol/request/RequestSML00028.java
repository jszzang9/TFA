package com.expull.tfa.core.app.protocol.request;

import net.sf.json.JSONObject;

import com.expull.tfa.core.app.protocol.handler.HandlerSML00028;
import com.expull.tfa.exception.MissingParameterException;

/**
 * {@link HandlerSML00028}에서 사용하는 요청 클래스이다.
 * 
 * @author delta829
 */
public class RequestSML00028 extends SimpleRequest {

	@Override
	protected void checkParameter(JSONObject bodyData) throws MissingParameterException {
		if (bodyData.containsKey("packetid") == false)
			throw new MissingParameterException(getTelegramNumber(), "packetid");
		if (bodyData.containsKey("hidden") == false)
			throw new MissingParameterException(getTelegramNumber(), "hidden");
		if (bodyData.containsKey("view") == false)
			throw new MissingParameterException(getTelegramNumber(), "view");
	}
}
