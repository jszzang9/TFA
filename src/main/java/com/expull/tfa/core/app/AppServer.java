package com.expull.tfa.core.app;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.expull.tfa.util.QueuedLogger.QueuedLogger;

/**
 * App 서버 클래스이다.
 * 
 * @author delta829
 */
public class AppServer {
	private final int port;

	/**
	 * {@link AppServer} 생성자.
	 * 
	 * @param port 포트.
	 */
	public AppServer(int port) {
		this.port = port;
	}

	/**
	 * App 서버를 시작한다.
	 */
	public void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new AppServerPipelineFactory());
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", false);
		
		try {
			bootstrap.bind(new InetSocketAddress(port));

			QueuedLogger.push(Level.INFO, "TFA App Server Port : " + port);
			QueuedLogger.push(Level.INFO, "TFA App Server is ready.");
		}
		catch (ChannelException e) {
			QueuedLogger.push(Level.FATAL, "TFA App Server : It's maybe already used port : " + port);
			QueuedLogger.push(Level.FATAL, "TFA App Server failed.");
			QueuedLogger.shutdown();
		}
	}
}
