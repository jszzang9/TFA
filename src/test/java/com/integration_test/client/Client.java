package com.integration_test.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.marlboro.util.JsonGenerator;



public class Client {
	private final String host;
    private final int port;

    private ClientBootstrap bootstrap = null;
    ClientHandler handler = new ClientHandler();
    private ChannelFuture future;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
    	if (bootstrap != null)
    		return;

        bootstrap = new ClientBootstrap(
        		new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(handler);
            }
        });

        future = bootstrap.connect(new InetSocketAddress(host, port));
        future.awaitUninterruptibly();
    }

    public void disconnect() {
    	if (bootstrap == null)
			return;

    	future.getChannel().close();
    	bootstrap.releaseExternalResources();
		bootstrap = null;
    }

	public JSONObject send(String telegramNumber, String bodyData) throws InterruptedException {
		JSONObject received = null;

		if (bootstrap == null)
			return received;

		try {
			byte[] telegramNumberBytes = telegramNumber.getBytes("UTF-8");
			byte[] bodyDataBytes = bodyData.getBytes("UTF-8");
			int bodyDataLength = bodyDataBytes.length;

			ChannelBuffer buffer = ChannelBuffers.buffer(4 + telegramNumberBytes.length + bodyDataBytes.length);
			buffer.writeInt(bodyDataLength);
			buffer.writeBytes(telegramNumberBytes);
			buffer.writeBytes(bodyDataBytes);

			future.getChannel().write(buffer).awaitUninterruptibly();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			ChannelBuffer buffer = handler.read(10, TimeUnit.SECONDS);
			int bodyLength = buffer.readableBytes();
			byte[] bodyDataBytes = new byte[bodyLength];
			buffer.readBytes(bodyDataBytes);

			String bodyDataString = new String(bodyDataBytes, "UTF-8");
			if (JsonGenerator.mayBeJSON(bodyDataString) == false)
				return received;

			received = JSONObject.fromObject(bodyDataString);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return received;
	}
}
