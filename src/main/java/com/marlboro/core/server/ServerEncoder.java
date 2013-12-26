package com.marlboro.core.server;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.marlboro.core.protocol.response.Response;
import com.marlboro.util.QueuedLogger.QueuedLogger;
import com.marlboro.util.QueuedLogger.QueuedTransactionLogs;



public class ServerEncoder extends OneToOneEncoder{

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,	Object msg) throws UnsupportedEncodingException {
		if (msg instanceof Response == false)
			return msg;

		Response response = (Response)msg;

		byte[] bodyData = response.getBodyData().toString().getBytes("UTF-8");
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(bodyData.length);
		buffer.writeBytes(bodyData);

		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + " ================== Outgoing Info ================== ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Connection : " + channel.getRemoteAddress() + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Telegram Number : " + response.getTelegramNumber() + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Body Data : " + response.getBodyData().toString() + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + " =================================================== ");
		QueuedLogger.push(logs);

		return buffer;
	}

}
