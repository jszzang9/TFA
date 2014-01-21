package com.expull.tfa.core.binder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

/**
 * Agent TCP 소켓 채널과 Packet ID를 바인딩하는 클래스이다.
 * 
 * @author delta829
 */
public class ChannelChannelIdBinder {
	private static ChannelChannelIdBinder channelIdBinder;
	public static ChannelChannelIdBinder getInstance() {
		if (channelIdBinder == null)
			channelIdBinder = new ChannelChannelIdBinder();
		
		return channelIdBinder;
	}
	
	private final Map<Channel, String> channelMap = new HashMap<Channel, String>();
	private final Map<String, Channel> channelIdMap = new HashMap<String, Channel>();
	
	/**
	 * {@link ChannelChannelIdBinder} 생성자.
	 */
	private ChannelChannelIdBinder() {
	}
	
	/**
	 * 채널과 channel ID를 묶는다.
	 * 
	 * @param channel 채널.
	 * @param channelId Packet ID.
	 */
	public void bind(Channel channel, String channelId) {
		channelMap.put(channel, channelId);
		channelIdMap.put(channelId, channel);
	}
	
	/**
	 * 묶여있는 채널을 해제한다.
	 * 
	 * @param channel 채널.
	 */
	public void unbind(Channel channel) {
		String packetId = channelMap.get(channel);
		if (packetId == null)
			return;
		
		channelMap.remove(channel);
		channelIdMap.remove(packetId);
	}
	
	/**
	 * 묶여있는 channel ID를 해제한다.
	 * 
	 * @param channelId Packet ID.
	 */
	public void unbind(String channelId) {
		Channel channel = channelIdMap.get(channelId);
		if (channel == null)
			return;
		
		channelMap.remove(channel);
		channelIdMap.remove(channelId);
	}
	
	/**
	 * channel ID로 묶여있는 채널을 가져온다.
	 * 
	 * @param chanelId Packet ID.
	 * @return 채널. 없으면 null.
	 */
	public Channel getChannelByChannelId(String chanelId) {
		return channelIdMap.get(chanelId);
	}
	
	/**
	 * 채널로 묶여있는 channel ID를 가져온다.
	 * 
	 * @param channel 채널.
	 * @return channel ID. 없으면 null. 
	 */
	public String getChannelIdByChannel(Channel channel) {
		return channelMap.get(channel);
	}
	
	/**
	 * channel ID가 묶여있는지 확인한다.
	 * 
	 * @param channelId Packet ID.
	 * @return 묶여있는지 여부.
	 */
	public boolean isBind(String channelId) {
		return channelIdMap.containsKey(channelId);
	}
	
	/**
	 * 채널이 묶여있는지 확인한다.
	 * @param channel 채널.
	 * @return 묶여있는지 여부.
	 */
	public boolean isBind(Channel channel) {
		return channelMap.containsKey(channel);
	}
	
	public Collection<Channel> allChannels() {
		return channelIdMap.values();
	}
}
