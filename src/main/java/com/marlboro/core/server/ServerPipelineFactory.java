package com.marlboro.core.server;


import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ServerPipelineFactory implements ChannelPipelineFactory{

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		
		pipeline.addLast("decoder", new ServerDecoder());
		pipeline.addLast("encoder", new ServerEncoder());
		pipeline.addLast("handler", new ServerHandler());
		return pipeline;
	}
}
