package com.expull.tfa.core.app.protocol.handler;

import com.expull.tfa.core.app.protocol.request.Request;
import com.expull.tfa.core.app.protocol.response.Response;

/**
 * 비즈니스를 처리하는 핸들러 추상 클래스이다.
 *
 * @author delta829
 *
 * @param <I> {@link Request}를 상속받은 요청 클래스
 * @param <O> {@link Response}를 상속받은 응답 클래스
 */
public abstract class Handler <I extends Request, O extends Response> {
	private I request;
	private O response;

	/**
	 * 요청 객체를 가져온다.
	 * 
	 * @return 요청 객체.
	 */
	public I getRequest() {
		return request;
	}

	/**
	 * 요청 객체를 설정한다.
	 * 
	 * @param request 요청 객체.
	 */
	public void setRequest(I request) {
		this.request = request;
	}

	/**
	 * 응답 객체를 가져온다.
	 * 
	 * @return 응답 객체.
	 */
	public O getResponse() {
		return response;
	}

	/**
	 * 응답 객체를 설정한다.
	 * 
	 * @param response 응답 객체.
	 */
	public void setResponse(O response) {
		this.response = response;
	}

	/**
	 * 비즈니스를 핸들링한다.
	 * 
	 * @throws Exception
	 */
	public abstract void handle() throws Exception;
}
