package com.expull.tfa.core.app.protocol.request;

import net.sf.json.JSONObject;

import com.expull.tfa.exception.MissingParameterException;

public class RequestWSS00010 extends SimpleRequest{

	@Override
	protected void checkParameter(JSONObject bodyData)
			throws MissingParameterException {
		if (bodyData.containsKey("pcid") == false)
			throw new MissingParameterException(getTelegramNumber(), "pcid");
	}
}
