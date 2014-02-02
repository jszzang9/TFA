package com.expull.tfa.control;

import net.sf.json.JSONObject;

import org.jboss.netty.channel.Channel;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.agnet_tcp.AgentTcpServerHandler;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;
import com.expull.tfa.core.protocol.model.TempManager;

public class SessionController {
	private static SessionController singleton = new SessionController();
	private SessionController() {}
	public static SessionController getInstance() { return singleton ;}
	
    private static final String RESULT_SUCCESS = "0000";
	private static final String RESULT_UID_REQUIRED = "3100";
	private static final String RESULT_PCID_REQUIRED = "3200";
	private static final String RESULT_HAS_NOCONNECTION = "4000";
	class TFAException extends Exception {
		private static final long serialVersionUID = -8445293431431833423L;
		private final String code;
		public TFAException(String code) {
			this.code = code;
		}
		public String getCode() {return code;}
	}
	
	public String findConnection(JSONObject json) {
		String resultCode = RESULT_SUCCESS;
		try {
			if(!json.has("uid")) throw new TFAException(RESULT_UID_REQUIRED);
			if(!json.has("pcid")) throw new TFAException(RESULT_PCID_REQUIRED);

			String uid = json.getString("uid");
			String pcid = json.getString("pcid");
			
			String pid = TempManager.getInstance().getPidOf(uid);
			String lid = TempManager.getInstance().getLidByPcid(pcid);
			
			if(!hasConnectionFor(pid, lid))
				throw new TFAException(RESULT_HAS_NOCONNECTION);
		} catch(TFAException e) {
			resultCode = e.getCode();
		}
		return resultCode;
	}
	
	private boolean hasConnectionFor(String pid, String lid) {
		return ChannelChannelIdBinder.getInstance()
				.isBind(ProtocolCommon.buildChannelIDFor(pid,lid));
	}

	public void bindWebSocketChannel(Channel ch, String pcId) {
		ChannelChannelIdBinder.getInstance().bind(ch, pcId);
	}

	public void unbindWebSocketChannel(Channel ch) {
		ChannelChannelIdBinder.getInstance().unbind(ch);
	}
	
	public String bindPhone(Channel channel, String pid, String mac) {
		String json = "{func:\"detectPhone\", arg:\"{pid:\\\""+pid+"\\\"}\"}";
		String lid = TempManager.getInstance().getLidByMac(mac);
		String channelId = ProtocolCommon.buildChannelIDFor(pid, lid);

		if(!unbindLastLocation(pid, lid)) return channelId;
		
		ChannelChannelIdBinder.getInstance().bind(channel, channelId);
		String[] pcIds = TempManager.getInstance().getPcidsFor(lid);
		for(String pcid : pcIds) {
			Channel ch = ChannelChannelIdBinder.getInstance().getChannelByChannelId(pcid);
			if(ch == null) continue;
			
			AgentTcpServerHandler.writeToChannel(ch, json);
		}
		return channelId;
	}

	private boolean unbindLastLocation(String pid, String lid) {
		String json = "{func:\"lostPhone\", arg:\"{pid:\\\""+pid+"\\\"}\"}";

		String lastId = ChannelChannelIdBinder.getInstance().findChannelIdStartsWith(pid);
		if(lastId == null) return true;
		
		String channelId = ProtocolCommon.buildChannelIDFor(pid, lid);
		if(lastId.equals(channelId)) return false;
		
		if(lastId.split("@").length < 2) return true;
		String lastLid = lastId.split("@")[1];
		ChannelChannelIdBinder.getInstance().unbind(lastId);
		String[] pcIds = TempManager.getInstance().getPcidsFor(lastLid);
		for(String pcid : pcIds) {
			Channel ch = ChannelChannelIdBinder.getInstance().getChannelByChannelId(pcid);
			if(ch == null) continue;
			
			AgentTcpServerHandler.writeToChannel(ch, json);
		}
		
		return true;
	}
	
	public void unbindLocation(String channel) {
		String pid = channel.split("@")[0];
		String json = "{func:\"lostPhone\", arg:\"{pid:\\\""+pid+"\\\"}\"}";

		String lastId = ChannelChannelIdBinder.getInstance().findChannelIdStartsWith(pid);
		if(lastId == null) return;
		if(lastId.split("@").length < 2) return;
		String lastLid = lastId.split("@")[1];
		ChannelChannelIdBinder.getInstance().unbind(lastId);
		String[] pcIds = TempManager.getInstance().getPcidsFor(lastLid);
		for(String pcid : pcIds) {
			Channel ch = ChannelChannelIdBinder.getInstance().getChannelByChannelId(pcid);
			if(ch == null) continue;
			
			AgentTcpServerHandler.writeToChannel(ch, json);
		}
	}
	
	public void expireSession(String pid) {
		String json = "{func:\"expireSession\", arg:\"{pid:\\\""+pid+"\\\"}\"}";

		String lastId = ChannelChannelIdBinder.getInstance().findChannelIdStartsWith(pid);
		if(lastId == null) return;

		if(lastId.split("@").length < 2) return;
		String lastLid = lastId.split("@")[1];
		ChannelChannelIdBinder.getInstance().unbind(lastId);
		String[] pcIds = TempManager.getInstance().getPcidsFor(lastLid);
		for(String pcid : pcIds) {
			Channel ch = ChannelChannelIdBinder.getInstance().getChannelByChannelId(pcid);
			if(ch == null) continue;
			
			AgentTcpServerHandler.writeToChannel(ch, json);
		}
	}
}
