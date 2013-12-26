package com.integration_test.client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.queue.BlockingReadHandler;



public class ClientHandler extends BlockingReadHandler<ChannelBuffer> {

	public ClientHandler() {
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    }
    
    @Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	}
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    	super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    	e.getChannel().close();
    }
}