package com.marlboro.core.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.marlboro.util.QueuedLogger.QueuedLogger;


public class Server {
	public void run(int port) {
		QueuedLogger.push(Level.INFO, "DCP starting...");

		boolean isReady = false;
		if (run_model()) {
			if (run_server(port))
				isReady = true;
		}

		if (isReady) {
			QueuedLogger.push(Level.INFO, "DCP is ready.");
		}
		else {
			QueuedLogger.push(Level.FATAL, "DCP failed.");
			QueuedLogger.shutdown();
		}
	}

	private boolean run_server(int port) {
		QueuedLogger.push(Level.INFO, "DCP-Server is preparing...");

		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ServerPipelineFactory());
		bootstrap.setOption("child.tcpNodelay", true);
		bootstrap.setOption("child.keepAlive", false);

		try {
			bootstrap.bind(new InetSocketAddress(port));

			QueuedLogger.push(Level.INFO, "DCP-Server Port : " + port);
			QueuedLogger.push(Level.INFO, "DCP-Server is ready.");
		} 
		catch (ChannelException e) {
			QueuedLogger.push(Level.FATAL, "It's maybe already used port : " + port);
			QueuedLogger.push(Level.FATAL, "DCP-Server failed.");

			return false;
		}
		
		return true;
	}

	private boolean run_model() {
		QueuedLogger.push(Level.INFO, "DCP-Model is perparing...");

		try {
			QueuedLogger.push(Level.INFO, "DCP-Model is ready.");
		} 
		catch (Exception e) {
			QueuedLogger.push(Level.FATAL, "It's maybe not running dbms.");
			QueuedLogger.push(Level.FATAL, "DCP-Model failed.");
			return false;
		}
		return true;
	}
}
