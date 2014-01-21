package com.expull.tfa.core.agnet_tcp;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.expull.tfa.core.binder.ChannelChannelIdBinder;

public class TCPWatchDog extends Thread {
	private static final long DEFAULT_TERM = 3000;
	private boolean keepGoing;
	private long term;
	
	public TCPWatchDog() {
		term = DEFAULT_TERM;
	}
	
	@Override
	public void run() {
		keepGoing = true;
		ChannelChannelIdBinder binder = ChannelChannelIdBinder.getInstance();
		ChannelBuffer buffer = new BigEndianHeapChannelBuffer("0".getBytes());
		while(keepGoing) {
			Collection<Channel> channels = new ArrayList<Channel>(binder.allChannels());
			for(Channel c : channels) {
				if(!c.isConnected() || !c.isOpen() || !c.isReadable() || !c.isWritable()) {
					binder.unbind(c);
					continue;
				}
				try {
					c.write(buffer);
				} catch(Throwable t) {
					binder.unbind(c);
				}
			}
			try { Thread.sleep(term); } catch(Throwable t) {}
		}
	}
	
	public void safeStop() {
		keepGoing = false;
	}
	
	public void setTerm(long term) { this.term = term; }
}
