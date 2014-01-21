package com.expull.tfa.core.agent_websocket;

import org.apache.log4j.Level;
import org.webbitserver.WebServer;
import org.webbitserver.handler.HttpToWebSocketHandler;
import org.webbitserver.handler.exceptions.PrintStackTraceExceptionHandler;
import org.webbitserver.netty.NettyWebServer;

import com.expull.tfa.core.agent_websocket.AgentWebSocketServerHandler.WebSocketHandler;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;

public class AgentWebSocketServer {
	private final int port;

	/**
	 * {@link AgentWebSocketServer} 생성자.
	 * 
	 * @param port 포트.
	 */
	public AgentWebSocketServer(int port) {
		this.port = port;
	}
	/**
	 * Agent Web 서버를 시작한다.
	 */
	public void run() {
			if(port <= 0) return;
			WebServer webServer = new NettyWebServer(port);
	        webServer.add(new HttpToWebSocketHandler(new WebSocketHandler(this))).connectionExceptionHandler(new PrintStackTraceExceptionHandler());
	        try {
				webServer.start().get();
				QueuedLogger.push(Level.INFO, "TFA Agent WebSocket Server Port : " + port);
				QueuedLogger.push(Level.INFO, "TFA Agent WebSocket Server is ready.");
			} catch (Throwable e) {
				QueuedLogger.push(Level.FATAL, "TFA Agent WebSocket Server : It's maybe already used port : " + port);
				QueuedLogger.push(Level.FATAL, "TFA Agent WebSocket Server failed.");
				QueuedLogger.shutdown();
			} 
	}
}		
