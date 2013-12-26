package com.expull.tfa.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * Json 생성 도우미 클래스이다.
 *  
 * @author delta829
 */
public class JsonGenerator {
	
	/**
	 * Json 형식에 맞는지 검사한다.
	 * 
	 * @param value 문자열.
	 * @return Json 형식에 맞으면 true, 틀리면 false.
	 */
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
	
	/**
	 * Json 배열 형식에 맞는지 검사한다.
	 * 
	 * @param value 문자열.
	 * @return Json 형식에 맞으면 true, 틀리면 false.
	 */
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
	
	/**
	 * Key/Value를 Json 형식의 문자열로 생성한다.
	 * 
	 * @param keyvalues Key/Value
	 * @return Json 형식의 문자열.
	 */
	public static String make(String ... keyvalues) {
		Map<String, String> bodyMap = new HashMap<String, String>();
		for (int i = 0; i < keyvalues.length; i += 2)
			bodyMap.put(keyvalues[i], keyvalues[i + 1]);
		
		return JSONObject.fromObject(bodyMap).toString();
	}
}
