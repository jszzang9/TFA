package com.expull.tfa.core.app.protocol.handler;

import net.sf.json.JSONObject;

public class WebSocketTransportHandler extends TransportHandler {
	
	@Override
	protected String extractChannelKeyFromRequest(JSONObject json) {
		return (json.containsKey("pcid") ? json.getString("pcid") : "");
	}
}
