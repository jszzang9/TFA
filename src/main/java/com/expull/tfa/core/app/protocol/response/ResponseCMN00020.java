package com.expull.tfa.core.app.protocol.response;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.app.protocol.handler.HandlerCMN00020;

/**
 * {@link HandlerCMN00020}에서 사용하는 응답 클래스이다.
 * 
 * @author delta829
 */

public class ResponseCMN00020 extends Response {
	private String status = "";
	private String packetid = "";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPacketid() {
		return packetid;
	}

	public void setPacketid(String packetid) {
		this.packetid = packetid;
	}

	@Override
	public JSONObject getBodyData() {
		Map<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(ProtocolCommon.RESULT_CODE_KEY, getResultCode());
		bodyMap.put("status", getStatus());
		bodyMap.put("packetid", getPacketid());

		return JSONObject.fromObject(bodyMap);
	}
}
