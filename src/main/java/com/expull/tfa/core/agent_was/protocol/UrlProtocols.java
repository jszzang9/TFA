package com.expull.tfa.core.agent_was.protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;
import com.expull.tfa.core.protocol.model.TempManager;
import com.expull.tfa.util.JsonGenerator;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;

/**
 * Agent WAS Server 에서 URL 프로토콜에 대한 비즈니스 핸들링을 하는 클래스이다.
 * 
 * @author delta829
 */
public class UrlProtocols {
    private static final String RESULT_SUCCESS = "0000";
	private static final String RESULT_UID_REQUIRED = "3100";
	private static final String RESULT_PCID_REQUIRED = "3200";
	private static final String RESULT_HAS_NOCONNECTION = "4000";

	public static String makeBody(String ... keyvalues) {
		Map<String, String> bodyMap = new HashMap<String, String>();
		for (int i = 0; i < keyvalues.length; i += 2)
			bodyMap.put(keyvalues[i], keyvalues[i + 1]);
		
		return JSONObject.fromObject(bodyMap).toString();
	}
    
	class TFAException extends Exception {
		private static final long serialVersionUID = -8445293431431833423L;
		private final String code;
		public TFAException(String code) {
			this.code = code;
		}
		public String getCode() {return code;}
	}
	
	public String auth(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		
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
		return makeBody("result", resultCode, "request", json.toString());
	}
	
	private boolean hasConnectionFor(String pid, String lid) {
		return ChannelChannelIdBinder.getInstance()
				.isBind(ProtocolCommon.buildChannelIDFor(pid,lid));
	}

	/**
	 * Key/Value 쌍으로 된 내용을 Json 형식으로 변환한다.
	 * 
	 * @param content
	 *            내용.
	 * @return Json 형식의 문자열.
	 * @throws UnsupportedEncodingException
	 */
	private String convertKeyValuePairToJSON(String content) {
		try {
			content = URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			QueuedLogger.push(Level.INFO, e1);
		}
		if (content.startsWith("{"))
			return content;

		JSONObject json = new JSONObject();
		try {
			int beginIndex = 0;
			int endIndex = 0;
			while (true) {
				beginIndex = content.indexOf("{", endIndex);
				endIndex = content.indexOf("}", beginIndex);
				if (beginIndex < 0 || endIndex < 0)
					break;

				String jsonValue = content.substring(beginIndex, endIndex + 1);
				String convertJsonValue = jsonValue.replace("&",
						"-Never-Never-Duplication-Words-");
				content = content.replace(jsonValue, convertJsonValue);
			}

			QueryStringDecoder decoder = new QueryStringDecoder(content, false);
			for (String key : decoder.getParameters().keySet()) {
				StringBuilder builder = new StringBuilder();
				for (String string : decoder.getParameters().get(key))
					builder.append(string.replace(
							"-Never-Never-Duplication-Words-", "&"));

				Object obj = JsonGenerator.mayBeJSON(builder.toString()) ? JSONObject
						.fromObject(builder.toString()) : builder.toString();
				json.put(key, obj.toString());
			}
		} catch (Exception e2) {
			QueuedLogger.push(Level.INFO, e2);
		}

		return json.toString();
	}
}
