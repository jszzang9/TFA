package com.expull.tfa.core.app.protocol.request;

import net.sf.json.JSONObject;

import com.expull.tfa.core.app.protocol.handler.HandlerSML00044;
import com.expull.tfa.exception.MissingParameterException;

/**
 * {@link HandlerSML00044}에서 사용하는 요청 클래스이다.
 * 
 * @author delta829
 */
public class RequestSML00044 extends SimpleRequest {
	
	@Override
	protected void checkParameter(JSONObject bodyData) throws MissingParameterException {
		if (bodyData.containsKey("packetid") == false)
			throw new MissingParameterException(getTelegramNumber(), "packetid");
		if (bodyData.containsKey("pcid") == false)
			throw new MissingParameterException(getTelegramNumber(), "pcid");
		if (bodyData.containsKey("p2") == false)
			throw new MissingParameterException(getTelegramNumber(), "p2");
		if (bodyData.containsKey("cntl") == false)
			throw new MissingParameterException(getTelegramNumber(), "cntl");
	}
}
