package com.expull.tfa.control;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jboss.netty.channel.Channel;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.agnet_tcp.AgentTcpServerHandler;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;
import com.expull.tfa.core.protocol.model.TempManager;
import com.expull.tfa.core.protocol.model.dto.MasterData;

public class SessionController {
	private static SessionController singleton = new SessionController();
	private SessionController() {}
	public static SessionController getInstance() { return singleton ;}
	
    private static final String RESULT_SUCCESS = "0000";
	private static final String RESULT_UID_REQUIRED = "3100";
	private static final String RESULT_PCID_REQUIRED = "3200";
	private static final String RESULT_HAS_NOCONNECTION = "4000";
	private static final String RESULT_FAIL = "9000";
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
	
	public String bindPhone(Channel channel, String pid, String lid) {
		String json = "{func:\"detectPhone\", arg:\"{pid:\\\""+pid+"\\\"}\"}";
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
	
	public String findLidByMac(String string) {
		return TempManager.getInstance().getLidByMac(string);
	}
	
	public JSONObject loadMasters() {
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		
		MasterData[] masters = TempManager.getInstance().getMasters();
		for(int i=0;masters!=null && i<masters.length;i++) {
			MasterData master = masters[i];
			JSONObject obj = new JSONObject();
			obj.put("token", master.getToken());
			obj.put("userid", master.getUserid());
			obj.put("contact", master.getContact());
			obj.put("auth", master.getAuth());
			obj.put("exception", master.getException());
			obj.put("approval", master.getApproval());
			obj.put("pc", master.getPc());
			obj.put("pc_limin", master.getPc_limit());
			obj.put("location", master.getLocation());
			obj.put("location_limit", master.getLocation_limit());
			obj.put("status", master.getStatus());
			array.add(obj);
		}
		result.put("list", array);

		return result;
	}
	public String isConnected(JSONObject json) {
		String resultCode = RESULT_SUCCESS;
		try {
			if(!json.has("uid")) throw new TFAException(RESULT_UID_REQUIRED);
			if(!json.has("pcid")) throw new TFAException(RESULT_PCID_REQUIRED);

			String uid = json.getString("uid");
			String pcid = json.getString("pcid");

//			String pid = TempManager.getInstance().getPidOf(uid);
//			String lid = TempManager.getInstance().getLidByPcid(pcid);

			MasterData master = TempManager.getInstance().getMasterForUid(uid);
			resultCode = master.getStatus();
		} catch(TFAException e) {
			resultCode = e.getCode();
		}
		return resultCode;
	}
	public String registToken(JSONObject json) {
		String token = json.getString("token");
		String contact = json.getString("contact");
		TempManager.getInstance().createMasterWitTokenAndContact(token, contact);
		return RESULT_SUCCESS;
	}
}
