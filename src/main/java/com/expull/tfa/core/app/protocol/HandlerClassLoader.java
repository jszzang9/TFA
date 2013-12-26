package com.expull.tfa.core.app.protocol;

import java.lang.reflect.Constructor;

import com.expull.tfa.core.app.protocol.handler.Handler;
import com.expull.tfa.core.app.protocol.request.Request;
import com.expull.tfa.core.app.protocol.response.Response;
import com.expull.tfa.exception.ServerInternalErrorException;

/**
 * Java Reflection 을 사용하여 적절한 핸들러 객체를 로드하는 클래스이다.
 *
 * @author delta829
 */
public class HandlerClassLoader {

	/**
	 * 입력된 전문번호로 해당 핸들러 객체를 생성하여 리턴한다.
	 * 
	 * @param telegramNumber 전문번호.
	 * @return 핸들러 객체.
	 * @throws HandlerNotFoundException
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
			throw new ServerInternalErrorException(telegramNumber);
		}

		return handler;
	}
}
