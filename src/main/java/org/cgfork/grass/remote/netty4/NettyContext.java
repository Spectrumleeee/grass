/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.remote.netty4;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.transport.AbstractContext;

/**
 * 
 */
public class NettyContext extends AbstractContext {
    
    private static final ConcurrentMap<io.netty.channel.Channel, NettyContext>
                ctxMap = new ConcurrentHashMap<>();
    
    private final Channel channel;
    
    private final ChannelHandler handler;
    
    private NettyContext(Channel channel, ChannelHandler handler) {
        this.channel = channel;
        this.handler = handler;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }
    
    public static NettyContext getContext(io.netty.channel.Channel channel, 
            ChannelHandler handler, RemoteLocator locator) {
        if (channel == null) {
            throw new IllegalArgumentException("channel is null");
        }
        
        NettyContext nettyContext = ctxMap.get(channel);
        if (nettyContext == null) {
            NettyChannel nc = new NettyChannel(channel, locator);
            if (nc.isConnected()) {
                nettyContext = ctxMap.putIfAbsent(channel,new NettyContext(nc, handler));
            }
            if (nettyContext == null) {
                nettyContext = new NettyContext(nc, handler);
            }
        }
        return nettyContext;
    }
    
    public static void removeContextIfDisconnected(io.netty.channel.Channel channel) {
        if (channel != null && !channel.isActive()) {
            ctxMap.remove(channel);
        }
    }
}
