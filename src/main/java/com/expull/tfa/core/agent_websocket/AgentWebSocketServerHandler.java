package com.expull.tfa.core.agent_websocket;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.jboss.netty.channel.Channel;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import com.expull.tfa.control.SessionController;
import com.expull.tfa.net.WebSocketConnectionChannel;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;

public class AgentWebSocketServerHandler {
	private static Map<WebSocketConnection, Channel> channelConnectionMap = new HashMap<WebSocketConnection, Channel>();

	static class WebSocketHandler extends BaseWebSocketHandler {
		AgentWebSocketServer server;
		private boolean waitingForPcId = true;
		public WebSocketHandler(AgentWebSocketServer s) {
			this.server = s;
		}

		@Override
		public void onOpen(WebSocketConnection connection) throws Exception {
			QueuedLogger.push(Level.INFO, "onOpen : "+connection);
			waitingForPcId = true;
		}

		@Override
		public void onMessage(WebSocketConnection connection, String msg) {
			QueuedLogger.push(Level.INFO, "onMessage : [" + msg + "]");
			if(waitingForPcId) {
				waitingForPcId=false;
				String pcId = msg;
				Channel ch = new WebSocketConnectionChannel(connection);
				SessionController.getInstance().bindWebSocketChannel(ch, pcId);
				channelConnectionMap.put(connection, ch);
			}
		}

		@Override
		public void onMessage(WebSocketConnection connection, byte[] msg) {
			onMessage(connection, new String(msg));
		}

		@Override
		public void onClose(WebSocketConnection connection) throws Exception {
			QueuedLogger.push(Level.INFO, "onClose : "+connection);
			if (!channelConnectionMap.containsKey(connection)) return;
			Channel ch = channelConnectionMap.get(connection);
			SessionController.getInstance().unbindWebSocketChannel(ch);
			channelConnectionMap.remove(connection);
			ch.close();
		}
	}
}

