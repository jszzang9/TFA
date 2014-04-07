package com.expull.tfa.core.agent_was.protocol;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.expull.tfa.control.SessionController;
import com.expull.tfa.util.JsonGenerator;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;

/**
 * Agent WAS Server 에서 URL 프로토콜에 대한 비즈니스 핸들링을 하는 클래스이다.
 * 
 * @author delta829
 */
public class UrlProtocols {
	private static final String PATH_WEBROOT = "/Users/shining/Desktop/TFA/src/main/resources/webroot";

	public static String makeBody(String ... keyvalues) {
		Map<String, String> bodyMap = new HashMap<String, String>();
		for (int i = 0; i < keyvalues.length; i += 2)
			bodyMap.put(keyvalues[i], keyvalues[i + 1]);
		
		return JSONObject.fromObject(bodyMap).toString();
	}
    

	public String framecontent(HttpRequest request, String content) {	
		return "<html>"                                                                                                
				+"<head> "
				+"<meta http-equiv=\"Content-type\" content=\"text/html;charset=UTF-8\"><script src=\"http://code.jquery.com/jquery.min.js\"></script> "
				+ "<script src=\"http://ec000.expull.com/sites/tfa/js/frame-ws.js\"></script> </head>"               
				+"<body> </body></html>";
	}
	
	private static String readAllFromReader(Reader reader) throws IOException {
		String result = "";
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			result += line + "\n";
		}
		reader.close();
		return result;
	}
	
	public String admin(HttpRequest request, String content) {
		String result = "";
		String path = request.getUri().replace("/admin/", "");
		try {
			result = readAllFromReader(new FileReader(PATH_WEBROOT+"/"+path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String loadMaster(HttpRequest request, String content) {
		JSONObject json = new JSONObject();
		JSONObject management = new JSONObject();
		JSONObject masters = SessionController.getInstance().loadMasters();
		management.put("list", masters.get("list"));
		json.put("management", management);
		return json.toString();
	}
	
	public String auth(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		/*
		return makeBody("result", SessionController.getInstance().findConnection(json),
				"request", json.toString());
*/
		return makeBody("result", SessionController.getInstance().isConnected(json),
				"request", json.toString());
	}

	public String createMaster(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		return makeBody("result", SessionController.getInstance().registToken(json));
	}
	
	public String bindphone(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		SessionController.getInstance().bindPhone(null, json.getString("pid"), json.getString("lid"));
		return makeBody("result", "0000"," request", json.toString());
	}
	
	public String expireSession(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		SessionController.getInstance().expireSession(json.getString("pid"));
		return makeBody("result", "0000"," request", json.toString());
	}
	
	public String discoverBluetooth(HttpRequest request, String content) {
		JSONObject json = JSONObject
				.fromObject(convertKeyValuePairToJSON(content));
		String lid = SessionController.getInstance().findLidByMac(json.getString("mac"));
		if(lid == null || lid.length() == 0) return makeBody("result", "9999");
		return makeBody("result", "0000", "lid", lid);
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
