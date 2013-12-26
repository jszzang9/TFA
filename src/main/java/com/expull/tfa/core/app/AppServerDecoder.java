package com.expull.tfa.core.app;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.app.protocol.RequestClassLoader;
import com.expull.tfa.core.app.protocol.request.Request;
import com.expull.tfa.exception.InvalidJsonFormatException;
import com.expull.tfa.exception.MrsException;
import com.expull.tfa.util.JsonGenerator;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;
import com.expull.tfa.util.QueuedLogger.QueuedTransactionLogs;


/**
 * 입력 스트림의 데이터를 적절한 요청 객체를 디코딩하는 클래스이다.
 *
 * @author delta829
 */
public class AppServerDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws UnsupportedEncodingException, MrsException {
		if (buffer.readableBytes() < ProtocolCommon.TELEGRAM_NUMBER_SIZE)
			return null;

		buffer.markReaderIndex();

		byte[] telegramNumber = new byte[ProtocolCommon.TELEGRAM_NUMBER_SIZE];
		buffer.readBytes(telegramNumber);

		int bodyLength = buffer.readableBytes();
		byte[] bodyData = new byte[bodyLength];
		buffer.readBytes(bodyData);

		String telegramNumberString = new String(telegramNumber, "UTF-8").toUpperCase();
		String bodyDataString = new String(bodyData, "UTF-8");

		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.INFO, "[App] ================== Incoming Info ================== ");
		logs.add(Level.INFO, "[App]  > Connection : " + channel.getRemoteAddress() + " ");
		logs.add(Level.INFO, "[App]  > Telegram Number : " + telegramNumberString + " ");
		logs.add(Level.INFO, "[App]  > Body Data : " + bodyDataString + " ");
		logs.add(Level.INFO, "[App] =================================================== ");
		QueuedLogger.push(logs);

		Request request = RequestClassLoader.loadFrom(telegramNumberString);
		if (JsonGenerator.mayBeJSON(bodyDataString) == false)
			throw new InvalidJsonFormatException(telegramNumberString);
		try {
			request.setBodyData(JSONObject.fromObject(bodyDataString));
		} catch (JSONException e) {
			throw new InvalidJsonFormatException(telegramNumberString);
		}

		return request;
	}
}
