package com.marlboro.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

public class JsonGenerator {
	
	public static boolean mayBeJSON(String value) {
		if (JSONUtils.mayBeJSON(value)) {
			try {
				JSONObject.fromObject(value);
			}
			catch (JSONException e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public static boolean mayBeJSONArray(String value) {
		if (JSONUtils.mayBeJSON(value)) {
			try {
				JSONArray.fromObject(value);
			}
			catch (JSONException e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public static String make(String ... keyvalues) {
		Map<String, String> bodyMap = new HashMap<String, String>();
		for (int i = 0; i < keyvalues.length; i += 2)
			bodyMap.put(keyvalues[i], keyvalues[i + 1]);
		
		return JSONObject.fromObject(bodyMap).toString();
	}
}
