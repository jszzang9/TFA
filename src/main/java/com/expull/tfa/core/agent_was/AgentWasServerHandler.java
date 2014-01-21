package com.expull.tfa.core.agent_was;

import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Set;

import org.apache.log4j.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;

import com.expull.tfa.core.agent_was.protocol.UrlProtocols;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;

/**
 * Agent WAS 서버를 핸들링하는 클래스이다.
 * 
 * @author delta829
 */
public class AgentWasServerHandler extends SimpleChannelUpstreamHandler {

	private HttpRequest request;
	private boolean readingChunks;
	/** Buffer that stores the response content */
	private final StringBuilder buf = new StringBuilder();
	private final UrlProtocols urlProtocols = new UrlProtocols();
	private String contentBody;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//		QueuedLogger.push(Level.INFO, "[Agent WAS] messageReceived, ["+e.getMessage()+"]");
		if (!readingChunks) {
			contentBody = "";
			HttpRequest request = this.request = (HttpRequest) e.getMessage();

			if (is100ContinueExpected(request)) {
				send100Continue(e);
			}

			if (request.isChunked()) {
				readingChunks = true;
			} else {
				ChannelBuffer content = request.getContent();
				if (content.readable()) {
					contentBody = content.toString(CharsetUtil.UTF_8);
				}

				String uri = request.getUri();
				String uri_request = (uri.indexOf("?") > 0 ? uri.substring(0, uri.indexOf("?")):uri);
				String methodName = uri_request.split("/")[1];
				String response = "";
				try {
					response = (String)urlProtocols.getClass().getMethod(methodName, new Class[] {HttpRequest.class, String.class}).invoke(urlProtocols, request, contentBody);
				} catch(NoSuchMethodException t) {
					
				} catch(Throwable t) {
					t.printStackTrace();
				}

				buf.setLength(0);
				buf.append(response);
				writeResponse(e);
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				readingChunks = false;

				String uri = request.getUri();
				String uri_request = (uri.indexOf("?") > 0 ? uri.substring(0, uri.indexOf("?")):uri);
				String methodName = uri_request.split("/")[1];
				String response = "";
				try {
					response = (String)urlProtocols.getClass().getMethod(methodName, new Class[] {HttpRequest.class, String.class}).invoke(urlProtocols, request);
				} catch(Throwable t) {
					t.printStackTrace();
				}

				buf.setLength(0);
				buf.append(response);
				writeResponse(e);
			} else {
				contentBody += chunk.getContent().toString(CharsetUtil.UTF_8);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		QueuedLogger.push(Level.FATAL, "[Agent WAS] Unexpected exception.", e.getCause());
		
		e.getChannel().close();
	}

	/**
	 * 응답을 쓴다.
	 * 
	 * @param message 응답 메시지.
	 */
	private void writeResponse(MessageEvent message) {
		// Decide whether to close the connection or not.
		boolean keepAlive = isKeepAlive(request);

		// Build the response object.
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		response.setContent(ChannelBuffers.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
		response.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");

		if (keepAlive) {
			// Add 'Content-Length' header only for a keep-alive connection.
			response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
			// Add keep alive header as per:
			// - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
			response.setHeader(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}

		// Encode the cookie.
		String cookieString = request.getHeader(COOKIE);
		if (cookieString != null) {
			CookieDecoder cookieDecoder = new CookieDecoder();
			Set<Cookie> cookies = cookieDecoder.decode(cookieString);
			if (!cookies.isEmpty()) {
				// Reset the cookies if necessary.
				CookieEncoder cookieEncoder = new CookieEncoder(true);
				for (Cookie cookie : cookies) {
					cookieEncoder.addCookie(cookie);
					response.addHeader(SET_COOKIE, cookieEncoder.encode());
				}
			}
		} else {
			// Browser sent no cookie.  Add some.
			CookieEncoder cookieEncoder = new CookieEncoder(true);
			cookieEncoder.addCookie("key1", "value1");
			response.addHeader(SET_COOKIE, cookieEncoder.encode());
			cookieEncoder.addCookie("key2", "value2");
			response.addHeader(SET_COOKIE, cookieEncoder.encode());
		}

		// Write the response.
		ChannelFuture future = message.getChannel().write(response);

		// Close the non-keep-alive connection after the write operation is done.
		if (!keepAlive) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	/**
	 * HTTP Continue (100)을 보낸다.
	 * 
	 * @param message 응답 메시지.
	 */
	private static void send100Continue(MessageEvent message) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
		message.getChannel().write(response);
	}
}
