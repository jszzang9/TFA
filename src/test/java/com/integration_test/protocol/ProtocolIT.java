package com.integration_test.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

public class ProtocolIT{
	 public static String makeRandomString() {
			return UUID.randomUUID().toString().replace("-", "");
		}
	    
	    public static String makeRandomSmallLetters() {
			return UUID.randomUUID().toString().replace("-", "").replaceAll("[0-9]", "").toLowerCase();
		}
	    
	    public static String makeBody(String ... keyvalues) {
			Map<String, String> bodyMap = new HashMap<String, String>();
			for (int i = 0; i < keyvalues.length; i += 2)
				bodyMap.put(keyvalues[i], keyvalues[i + 1]);
			
			return JSONObject.fromObject(bodyMap).toString();
		}
}
