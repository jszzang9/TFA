package com.expull.tfa.core.app.protocol;

import java.lang.reflect.Constructor;

import com.expull.tfa.core.app.protocol.request.Request;
import com.expull.tfa.exception.ServerInternalErrorException;
import com.expull.tfa.exception.UnsupportedTelegramNumberException;

/**
 * Java Reflection 을 사용하여 적절한 요청 객체를 로드하는 클래스이다.
 *
 * @author delta829
 */
public class RequestClassLoader {

	/**
	 * 입력된 전문번호로 해당 요청 객체를 생성하여 리턴한다.
	 * 
	 * @param telegramNumber 전문번호.
	 * @return 요청 객체.
	 * @throws RequestNotFoundException
	 */
	public static Request loadFrom(String telegramNumber) throws UnsupportedTelegramNumberException {
		Request request = null;
		
		try {
			Class<?> cls = Class.forName(Request.class.getPackage().getName() + ".Request" + telegramNumber);
			Constructor<?> con = cls.getConstructor();
			Object obj = con.newInstance();
			
			if (obj instanceof Request == false)
				throw new ServerInternalErrorException(telegramNumber);
			
			request = (Request)obj;
			request.setTelegramNumber(telegramNumber);
			
		} catch (Exception e) {
			throw new UnsupportedTelegramNumberException(telegramNumber);
		}

		return request;
	}
}
