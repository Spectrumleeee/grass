package org.cgfork.grass.remote;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface Server {

    Collection<Channel> getChannels();
    
    Channel getChannel(InetSocketAddress remoteAddress);
    
    ChannelHandler channelHandler();
    
    InetSocketAddress localAddress();
    
    void shutdown() throws InterruptedException;
}
