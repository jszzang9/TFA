package com.marlboro.core.protocol;

import java.lang.reflect.Constructor;

import com.marlboro.core.protocol.request.Request;
import com.marlboro.exception.ServerInternalErrorException;
import com.marlboro.exception.UnsupportedTelegramNumberException;





public class RequestClassLoader {

	/**
	 * 입력된 전문번호로 해당 요청 객체를 생성하여 리턴한다.
	 * 
	 * @param telegramNumber 전문번호.
	 * @return 요청 객체.
	 * @throws ServerInternalErrorException
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
