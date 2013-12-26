package com.expull.tfa.core.app.protocol.handler;

import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.jboss.netty.channel.Channel;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.agnet_tcp.AgentTcpServerHandler;
import com.expull.tfa.core.app.protocol.request.SimpleRequest;
import com.expull.tfa.core.app.protocol.response.Response;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;
import com.expull.tfa.exception.MrsException;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;
import com.expull.tfa.util.QueuedLogger.QueuedTransactionLogs;

/**
 * 패킷 아이디로 바인딩 된 소켓 채널에 Bypass 핸들링을 하는 클래스이다.
 * 
 * @author delta829
 */
public class TransportHandler extends Handler<SimpleRequest, Response> {
	
	/**
	 * 요청 데이터에서 패킷 아이디를 추출한다.
	 * 
	 * @param json 요청 데이터.
	 * @return
	 */
	protected String extractChannelKeyFromRequest(JSONObject json) {
		return (json.containsKey("packetid") ? json.getString("packetid") : "");
	}
	
	@Override
	public void handle() throws MrsException {
		String channelId = extractChannelKeyFromRequest(getRequest().getData());
		
		if (ChannelChannelIdBinder.getInstance().isBind(channelId)) {
			JSONObject data = getRequest().getData();
			data.put("telegramnumber", getRequest().getTelegramNumber());
			
			Channel ch = ChannelChannelIdBinder.getInstance().getChannelByChannelId(channelId);
			AgentTcpServerHandler.writeToChannel(ch, data.toString());
			
			QueuedTransactionLogs logs = new QueuedTransactionLogs();
			logs.add(Level.INFO, " ================== Send To PC Agent ================== ");
			logs.add(Level.INFO, "  > Telegram Number : " + getRequest().getTelegramNumber());
			logs.add(Level.INFO, "  > Body Data : " + data.toString());
			logs.add(Level.INFO, "  > OK");
			logs.add(Level.INFO, " ================================================= ");
			QueuedLogger.push(logs);
			
			getResponse().setResultCode(ProtocolCommon.RESULT_CODE_SUCCESS);
			getResponse().setResultMessage(ProtocolCommon.RESULT_MESSAGE_SUCCESS);
		}
		else {
			getResponse().setResultCode(ProtocolCommon.RESULT_CODE_NO_BINDING_PC_AGENT);
			getResponse().setResultMessage(ProtocolCommon.RESULT_MESSAGE_NO_BINDING_PC_AGENT + " ["+channelId+"]");
		}
	}
}
