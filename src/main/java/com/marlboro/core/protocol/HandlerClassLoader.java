package com.marlboro.core.protocol;

import java.lang.reflect.Constructor;

import com.marlboro.core.protocol.handler.Handler;
import com.marlboro.core.protocol.request.Request;
import com.marlboro.core.protocol.response.Response;
import com.marlboro.exception.ServerInternalErrorException;



public class HandlerClassLoader {

	/**
	 * 입력된 전문번호로 해당 핸들러 객체를 생성하여 리턴한다.
	 * 
	 * @param telegramNumber 전문번호.
	 * @return 핸들러 객체.
	 * @throws ServerInternalErrorException
	 */
	@SuppressWarnings("unchecked")
	public static Handler<Request, Response> loadFrom(String telegramNumber) throws ServerInternalErrorException {
		Handler<Request, Response> handler = null;
		
		try {
			Class<?> cls = Class.forName(Handler.class.getPackage().getName() + ".Handler" + telegramNumber);
			Constructor<?> con = cls.getConstructor();
			Object obj = con.newInstance();
			
			if (obj instanceof Handler == false)
				throw new ServerInternalErrorException(telegramNumber);
			
			handler = (Handler<Request, Response>) obj;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerInternalErrorException(telegramNumber);
		}

		return handler;
	}
}
