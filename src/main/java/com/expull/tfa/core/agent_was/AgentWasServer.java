package com.expull.tfa.core.agent_was;

import static org.jboss.netty.channel.Channels.pipeline;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.expull.tfa.util.QueuedLogger.QueuedLogger;

/**
 * Agent WAS 서버 클래스이다.
 * 
 * @author delta829
 */
public class AgentWasServer {
	private final int port;

	/**
	 * {@link AgentWasServer} 생성자.
	 * 
	 * @param port 포트.
	 */
	public AgentWasServer(int port) {
		this.port = port;
	}

	/**
	 * Agent WAS 서버를 시작한다.
	 */
	public void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = pipeline();
				pipeline.addLast("decoder", new HttpRequestDecoder());
				pipeline.addLast("encoder", new HttpResponseEncoder());
				pipeline.addLast("deflater", new HttpContentCompressor());
				pipeline.addLast("handler", new AgentWasServerHandler());
				return pipeline;
			}
		});
		
		try {
			bootstrap.bind(new InetSocketAddress(port));

			QueuedLogger.push(Level.INFO, "TFA Agent WAS Server Port : " + port);
			QueuedLogger.push(Level.INFO, "TFA Agent WAS Server is ready.");
		}
		catch (ChannelException e) {
			QueuedLogger.push(Level.FATAL, "TFA Agent WAS Server : It's maybe already used port : " + port);
			QueuedLogger.push(Level.FATAL, "TFA Agent WAS Server failed.");
			QueuedLogger.shutdown();
		}
	}
}
