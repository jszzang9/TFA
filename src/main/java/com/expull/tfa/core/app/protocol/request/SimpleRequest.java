package com.expull.tfa.core.app.protocol.request;

import net.sf.json.JSONObject;

import com.expull.tfa.exception.MissingParameterException;

/**
 * 단순 요청 추상 클래스이다.
 * <br>요청 파라미터를 통채로 가지고 있다.
 *  
 * @author delta829
 */
public abstract class SimpleRequest extends Request {
	private JSONObject data;
	
	/**
	 * 키에 해당하는 값을 가져온다.
	 * 
	 * @param key 키.
	 * @return 값.
	 */
	public String get(String key) {
		return data.getString(key);
	}

	/**
	 * 요청 데이터를 가져온다.
	 * 
	 * @return 요청 데이터.
	 */
	public JSONObject getData() {
		return this.data;
	}

	@Override
	public void setBodyData(JSONObject bodyData) throws MissingParameterException {
		checkParameter(bodyData);
		
		this.data = bodyData;
	}
	
	/**
	 * 파라미터를 체크한다.
	 * 
	 * @param bodyData 요청 데이터.
	 * @throws MissingParameterException
	 */
	protected abstract void checkParameter(JSONObject bodyData) throws MissingParameterException;
}
