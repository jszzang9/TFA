package com.expull.tfa.core.app.protocol;

import java.lang.reflect.Constructor;

import com.expull.tfa.core.app.protocol.response.Response;
import com.expull.tfa.exception.ServerInternalErrorException;

/**
 * Java Reflection 을 사용하여 적절한 응답 객체를 로드하는 클래스이다.
 *
 * @author delta829
 */
public class ResponseClassLoader {

	/**
	 * 입력된 전문번호로 해당 응답객체를 생성하여 리턴한다.
	 * 
	 * @param telegramNumber 전문번호.
	 * @return 응답객체.
	 * @throws ResponseNotFoundException
	 */
	public static Response loadFrom(String telegramNumber) throws ServerInternalErrorException {
		Response response = null;
		
		try {
			Class<?> cls = Class.forName(Response.class.getPackage().getName() + ".Response" + telegramNumber);
			Constructor<?> con = cls.getConstructor();
			Object obj = con.newInstance();
			
			if (obj instanceof Response)
				response = (Response)obj;
			else
				throw new ServerInternalErrorException(telegramNumber);
			
			response = (Response)obj;
			response.setTelegramNumber(telegramNumber);
			
		} catch (Exception e) {
			throw new ServerInternalErrorException(telegramNumber);
		} 

		return response;
	}
}
