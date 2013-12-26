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
import com.expull.tfa.core.agent_was.dcp.DCPProxy;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;
import com.expull.tfa.util.JsonGenerator;
import com.expull.tfa.util.JsonResponse;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;
import com.marlboro.core.model.manager.TempManager;

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
	 * RESTful /common
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String common(HttpRequest request, String content) {
		return evaluateMethodForFullUrl(request, content);
	}

	/**
	 * RESTful /common/service/agentlogin
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String common_service_agentlogin(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		String channelId = extractChannelIdFromRequest(json);
		if (!ChannelChannelIdBinder.getInstance().isBind(channelId))
			return new JsonResponse(
					ProtocolCommon.RESULT_CODE_NO_BINDING_PC_AGENT,
					ProtocolCommon.RESULT_MESSAGE_NO_BINDING_PC_AGENT + " ["
							+ channelId + "]").toString();

		String result = "";
		try {
			result = DCPProxy.send("SML00051", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * RESTful /common/service/agentlogout
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String common_service_agentlogout(HttpRequest request, String content) {
		String result = "";
		try {
			result = DCPProxy.send("SML00052", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * RESTful /common/service/agentregister
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String common_service_agentregister(HttpRequest request, String content) {
		String result = "";
		try {
			result = DCPProxy.send("SML00023", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * /common/service/agentregistermd20
	 * @param request
	 * @param content
	 * @return
	 */
	public String  common_service_agentregistermd20(HttpRequest request, String content) {
		String result = "";
		try {
			result = DCPProxy.send("SML00025", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * /common/service/requestdrmkey
	 * @param request
	 * @param content
	 * @return
	 */
	public String  common_service_requestdrmkey(HttpRequest request, String content) {
		String result = "";
		try {
			result = DCPProxy.send("SML00062", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * RESTful /ml2_service_login
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String ml2_service_login(HttpRequest request,
			String content) {
		Map<String, String> params = makeParams(content);
		String packetid = params.get("packetid");
		String message = params.get("message");
		ChannelChannelIdBinder.getInstance().getChannelByChannelId(packetid).write(message);
		return new JsonResponse().toString();
	}

	private Map<String, String> makeParams(String content) {
		String[] pairs = content.split("&");
		Map<String, String> result = new HashMap<String, String>();
		for(String p : pairs) {
			String[] args = p.split("=");
			result.put(args[0], args[1]);
		}
		return result;
	}

	/**
	 * RESTful /serviceml
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String serviceml(HttpRequest request, String content) {
//		JSONObject json = JSONObject
//				.fromObject(convertKeyValuePairToJSON(content));
//		String channelId = extractChannelIdFromRequest(json);
//		if (!ChannelChannelIdBinder.getInstance().isBind(channelId))
//			return new JsonResponse(
//					ProtocolCommon.RESULT_CODE_NO_BINDING_PC_AGENT,
//					ProtocolCommon.RESULT_MESSAGE_NO_BINDING_PC_AGENT + " ["
//							+ channelId + "]").toString();

		return evaluateMethodForFullUrl(request, content);
	}
	
	/**
	 * RESTful /serviceml/ui/changepassword
	 * 
	 * @param request
	 *  		요청 객체.
	 * @param content
	 * 			요청 내용.
	 * @return 
	 * 			요청 결과.
	 */
	public String serviceml_ui_changepassword(HttpRequest request, String content) {
		String response = "";
		try {
			response = DCPProxy.send("PWS00060", content);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * RESTful /serviceml/ui/localstoragesave
	 * 
	 * @param request
	 *  		요청 객체.
	 * @param content
	 * 			요청 내용.
	 * @return 
	 * 			요청 결과.
	 */
	public String serviceml_ui_localstoragesave(HttpRequest request, String content) {
		String response = "";
		try {
			response = DCPProxy.send("CMN00060", content);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	 * RESTful /test
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String test(HttpRequest request, String content) {
		return evaluateMethodForFullUrl(request, content);
	}
	/**
	 * RESTful /serviceml_agent_sendsitedata
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String serviceml_agent_sendsitedata(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("SML00024", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * RESTful /test/createdata
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String test_createdata(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("BAD00001", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	

	/**
	 * RESTful /test/removedata
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String test_removedata(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("BAD00002", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	
	/**
	 * RESTful /test/bindingmrs
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String test_bindingmrs(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("CMN00031", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	
	/**
	 * RESTful /test/bindingmps
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String test_bindingmps(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("CMN00032", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	
	/**
	 * RESTful /pcid
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String pcid(HttpRequest request, String content) {
		return evaluateMethodForFullUrl(request, content);
	}
	
	/**
	 * RESTful /pcid/bindingmrs
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String pcid_bindingmrs(HttpRequest request, String content) {
		String response = "";
		try {
			response = DCPProxy.send("CMN00033", content);
		} catch (Exception e) {
			e.printStackTrace();
			response = e.toString();
		}
		return response;
	}
	
	/**
	 * RESTful /pcid/bindingmps
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String pcid_bindingmps(HttpRequest request, String content) {
		String response = "";
		try {
			response = DCPProxy.send("CMN00034", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	
	/**
	 * RESTful /serviceml/agent/getsitedata
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String serviceml_agent_getsitedata(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("PWS00020", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * RESTful /servicemd
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String servicemd(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		String channelID = extractChannelIdFromRequest(json);
		if (!ChannelChannelIdBinder.getInstance().isBind(channelID))
			return new JsonResponse(
					ProtocolCommon.RESULT_CODE_NO_BINDING_PC_AGENT,
					ProtocolCommon.RESULT_MESSAGE_NO_BINDING_PC_AGENT + " ["
							+ channelID + "]").toString();

		return evaluateMethodForFullUrl(request, content);
	}

	/**
	 * RESTful /servicemd/service/resdirectory
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String servicemd_service_resdirectory(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("SML00030", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * RESTful /servicemd/service/agentchangedata
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	public String servicemd_service_agentchangedata(HttpRequest request,
			String content) {
		String response = "";
		try {
			response = DCPProxy.send("SML00027", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	

	/**
	 * 요청한 Url에서 해당 메소드를 추출하여 요청한다.
	 * 
	 * @param request
	 *            요청 객체.
	 * @param content
	 *            요청 내용.
	 * @return 요청 결과.
	 */
	private String evaluateMethodForFullUrl(HttpRequest request, String content) {
		Object handler = this;
		String uri = request.getUri();
		String uri_request = (uri.indexOf("?") > 0 ? uri.substring(0,
				uri.indexOf("?")) : uri);
		String methodName = uri_request.replace("/", "_").replace("__", "_");
		if (methodName.startsWith("_"))
			methodName = methodName.substring(1);

		QueuedLogger.push(Level.INFO, "[Agent WAS] Request : " + uri + ", ["
				+ content + "]-> invoke " + methodName);
		String response = "";

		try {
			String jsonContent = convertKeyValuePairToJSON(content);
			response = (String) handler
					.getClass()
					.getMethod(methodName,
							new Class[] { HttpRequest.class, String.class })
					.invoke(handler, request, jsonContent);
		} catch (Throwable t) {
			QueuedLogger.push(Level.INFO, t);
			response = new JsonResponse(
					ProtocolCommon.RESULT_CODE_UNSUPPORTED_TELEGRAM_NUMBER,
					ProtocolCommon.RESULT_MESSAGE_UNSUPPORTED_TELEGRAM_NUMBER)
					.toString();
		}

		QueuedLogger.push(Level.INFO, "[Agent WAS] Response : " + response);

		return response;
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

	/**
	 * Json 객체에서 채널아이디 값을 추출한다. (pcid or packetid)
	 * 
	 * @param json
	 * 
	 * @return channelId 
	 */
	@Deprecated
	private String extractChannelIdFromRequest(JSONObject json) {
		return (json.containsKey("pcid") ? json.getString("pcid") : json.getString("packetid"));
	}
}
