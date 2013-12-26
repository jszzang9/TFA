package com.expull.tfa.core.app.protocol.handler;

import com.expull.tfa.core.app.protocol.request.RequestCMN00020;
import com.expull.tfa.core.app.protocol.response.ResponseCMN00020;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;

/**
 * PC Agent 연결상태 요청
 * 
 * @author delta829
 */
public class HandlerCMN00020 extends Handler<RequestCMN00020, ResponseCMN00020> {

	@Override
	public void handle() throws Exception {
		String packetid = getRequest().getData().getString("packetid");
		String status = ChannelChannelIdBinder.getInstance().isBind(packetid) ? "on" : "off";
		
		getResponse().setStatus(status);
		getResponse().setPacketid(packetid);
	}
}
