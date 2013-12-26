package com.expull.tfa.net;

import java.net.SocketAddress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.webbitserver.WebSocketConnection;

public class WebSocketConnectionChannel implements Channel {
	private WebSocketConnection connection;
	
	public WebSocketConnectionChannel(WebSocketConnection connection) {
		this.connection = connection;
	}

	@Override
	public ChannelFuture write(Object message) {
		String response = message instanceof ChannelBuffer
				? new String(ChannelBuffer.class.cast(message).array())
		: message.toString();
		connection.send(response);
		return null;
	}
	
	@Override
	public ChannelFuture close() {
		connection.close();
		return null;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////
	@Override
	public int compareTo(Channel arg0) {
		return 0;
	}
	
	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public ChannelFactory getFactory() {
		return null;
	}

	@Override
	public Channel getParent() {
		return null;
	}

	@Override
	public ChannelConfig getConfig() {
		return null;
	}

	@Override
	public ChannelPipeline getPipeline() {
		return null;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public boolean isBound() {
		return false;
	}

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return null;
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return null;
	}


	@Override
	public ChannelFuture write(Object message, SocketAddress remoteAddress) {
		return null;
	}

	@Override
	public ChannelFuture bind(SocketAddress localAddress) {
		return null;
	}

	@Override
	public ChannelFuture connect(SocketAddress remoteAddress) {
		return null;
	}

	@Override
	public ChannelFuture disconnect() {
		return null;
	}

	@Override
	public ChannelFuture unbind() {
		return null;
	}

	@Override
	public ChannelFuture getCloseFuture() {
		return null;
	}

	@Override
	public int getInterestOps() {
		return 0;
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public boolean isWritable() {
		return false;
	}

	@Override
	public ChannelFuture setInterestOps(int interestOps) {
		return null;
	}

	@Override
	public ChannelFuture setReadable(boolean readable) {
		return null;
	}

	public Object getAttachment() {
		return null;
	}

	public void setAttachment(Object attachment) {

	}
}
