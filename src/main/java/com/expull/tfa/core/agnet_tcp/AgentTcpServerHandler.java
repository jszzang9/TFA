package com.expull.tfa.core.agnet_tcp;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.expull.tfa.common.ProtocolCommon;
import com.expull.tfa.core.binder.ChannelChannelIdBinder;
import com.expull.tfa.core.protocol.model.TempManager;
import com.expull.tfa.util.JsonGenerator;
import com.expull.tfa.util.JsonResponse;
import com.expull.tfa.util.QueuedLogger.QueuedLogger;
import com.expull.tfa.util.QueuedLogger.QueuedTransactionLogs;

/**
 * Agent TCP 서버를 핸들링 하는 클래스이다.
 * 
 * @author delta829
 */
public class AgentTcpServerHandler extends SimpleChannelHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);

		QueuedLogger.push(Level.INFO, "[Agent TCP] CONNECTED: " + e.getChannel().getRemoteAddress());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);

		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.INFO, "[Agent TCP] DISCONNECTED: " + e.getChannel().getRemoteAddress());
		if (ChannelChannelIdBinder.getInstance().isBind(e.getChannel()))
			logs.add(Level.INFO, "[Agent TCP] Unbind agent tcp with channelId: " + ChannelChannelIdBinder.getInstance().getChannelIdByChannel(e.getChannel()));
		QueuedLogger.push(logs);

		ChannelChannelIdBinder.getInstance().unbind(e.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws UnsupportedEncodingException {
		BigEndianHeapChannelBuffer buf = (BigEndianHeapChannelBuffer) e.getMessage();
		if (buf.readableBytes() < ProtocolCommon.TELEGRAM_NUMBER_SIZE + 1)
			return;

		byte[] bytes = new byte[buf.capacity()];
		buf.readBytes(bytes);
		buf.setIndex(0, bytes.length);

		String msg = new String(bytes);
		QueuedLogger.push(Level.INFO, "[Agent TCP] incoming raw data : " + msg);

		String bodyData = msg;
		if (JsonGenerator.mayBeJSON(bodyData) == false) {
			writeToChannel(e.getChannel(), new JsonResponse(ProtocolCommon.RESULT_CODE_INVALID_JSON_FORMAT, ProtocolCommon.RESULT_MESSAGE_INVALID_JSON_FORMAT).toString());
			return;
		}

		JSONObject object = JSONObject.fromObject(bodyData);
		if (object.containsKey("pid") == false && object.containsKey("mac") == false) {
			writeToChannel(e.getChannel(), new JsonResponse(ProtocolCommon.RESULT_CODE_MISSING_PARAMETER, ProtocolCommon.RESULT_MESSAGE_MISSING_PARAMETER).toString());
			return;
		}
		
		String pid = object.getString("pid");
		String mac = object.getString("mac").toUpperCase();
		String lid = TempManager.getInstance().getLidByMac(mac);
		String channelId = ProtocolCommon.buildChannelIDFor(pid, lid);
		ChannelChannelIdBinder.getInstance().bind(e.getChannel(), channelId);
		//db add
		writeToChannel(e.getChannel(), JsonGenerator.make("resultcode", ProtocolCommon.RESULT_CODE_SUCCESS, "resultmessage", ProtocolCommon.RESULT_MESSAGE_SUCCESS));

		QueuedLogger.push(Level.INFO, "[Agent TCP] Bind agent tcp with ChannelId : " + channelId);
		return;

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.FATAL, "[Agent TCP] Unexpected exception.", e.getCause());
		if (ChannelChannelIdBinder.getInstance().isBind(e.getChannel()))
			logs.add(Level.INFO, "[Agent TCP] Unbind agent tcp with channelId: " + ChannelChannelIdBinder.getInstance().getChannelIdByChannel(e.getChannel()));
		QueuedLogger.push(logs);

		e.getChannel().close();
		ChannelChannelIdBinder.getInstance().unbind(e.getChannel());
	}

	/**
	 * 특정 채널에 데이터를 쓴다.
	 * 
	 * @param channel 채널.
	 * @param data 데이터.
	 */
	public static void writeToChannel(Channel channel, String data) {
		channel.write(new BigEndianHeapChannelBuffer(data.getBytes()));
	}
}
