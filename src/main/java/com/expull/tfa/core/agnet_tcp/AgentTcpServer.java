package com.expull.tfa.core.agnet_tcp;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.expull.tfa.util.QueuedLogger.QueuedLogger;

/**
 * Agent TCP 서버 클래스이다.
 * 
 * @author delta829
 */
public class AgentTcpServer {
	private final int port;
	TCPWatchDog watchdog;
	
	/**
	 * {@link AgentTcpServer} 생성자.
	 * 
	 * @param port 포트.
	 */
	public AgentTcpServer(int port) {
		this.port = port;
		watchdog = new TCPWatchDog();
	}

	/**
	 * Agent TCP 서버를 시작한다.
	 */
	public void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new AgentTcpServerHandler());
			}
		});
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		
		try {
			bootstrap.bind(new InetSocketAddress(port));

			QueuedLogger.push(Level.INFO, "TFA Agent TCP Server Port : " + port);
			QueuedLogger.push(Level.INFO, "TFA Agent TCP Server is ready.");
		}
		catch (ChannelException e) {
			QueuedLogger.push(Level.FATAL, "TFA Agent TCP Server : It's maybe already used port : " + port);
			QueuedLogger.push(Level.FATAL, "TFA Agent TCP Server failed.");
			QueuedLogger.shutdown();
		}
		
//		watchdog.start();
	}
}
