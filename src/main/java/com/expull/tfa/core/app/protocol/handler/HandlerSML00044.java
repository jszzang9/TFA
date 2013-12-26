package com.expull.tfa.core.app.protocol.handler;


/**
 * PC Agent 등록 제어
 * 
 * @author delta829
 */
public class HandlerSML00044 extends TransportHandler {
	
//	@Override
//	public void handle() throws MrsException {
//		if ("n".equals(getRequest().get("cntl").toLowerCase())) {
//			String channelId = extractChannelIdFromRequest(getRequest().getData());
//			Channel channel = ChannelPacketIdBinder.getInstance().getChannelByPacketId(channelId);
//			if (channel != null) {
//				QueuedLogger.push(Level.INFO, "[App] Force to unbind agent tcp with channelId: " + channelId);
//				
//				channel.close();
//				ChannelPacketIdBinder.getInstance().unbind(channelId);
//			}
//		}
//		else {
//			super.handle();
//		}
//	}
}
