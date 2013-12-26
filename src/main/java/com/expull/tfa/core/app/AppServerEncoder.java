package com.expull.tfa.core.app;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.expull.tfa.core.app.protocol.response.Response;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;
import com.expull.tfa.util.QueuedLogger.QueuedTransactionLogs;

/**
 * 응답 객체를 바이트로 엔코딩하여 출력 스트림으로 보내는 클래스이다.
 *
 * @author delta829
 */
public class AppServerEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws UnsupportedEncodingException {
		if (msg instanceof Response == false)
			return msg;

		Response response = (Response)msg;

		byte[] bodyData = response.getBodyData().toString().getBytes("UTF-8");
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(bodyData.length);
		buffer.writeBytes(bodyData);

		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.INFO, "[App] ================== Outgoing Info ================== ");
		logs.add(Level.INFO, "[App]  > Connection : " + channel.getRemoteAddress() + " ");
		logs.add(Level.INFO, "[App]  > Telegram Number : " + response.getTelegramNumber() + " ");
		logs.add(Level.INFO, "[App]  > Body Data : " + response.getBodyData().toString() + " ");
		logs.add(Level.INFO, "[App] =================================================== ");
		QueuedLogger.push(logs);

		return buffer;
	}
}
