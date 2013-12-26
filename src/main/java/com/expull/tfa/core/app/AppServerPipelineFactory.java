package com.expull.tfa.core.app;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * 채널 파이프라인 생성 클래스이다.
 *
 * @author delta829
 */
public class AppServerPipelineFactory implements ChannelPipelineFactory {
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		pipeline.addLast("decoder", new AppServerDecoder());
		pipeline.addLast("encoder", new AppServerEncoder());
		pipeline.addLast("handler", new AppServerHandler());

		return pipeline;
	}
}
