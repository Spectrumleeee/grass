/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-9
 */
package org.cg.sirpc.remote.netty4;

import io.netty.channel.ChannelFuture;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cg.sirpc.remote.ChannelHandler;
import org.cg.sirpc.remote.RemoteException;
import org.cg.sirpc.remote.RemoteLocator;
import org.cg.sirpc.remote.transport.AbstractChannel;
import static org.cg.sirpc.remote.ChannelOption.*;
import static org.cg.sirpc.remote.Constants.*;
/**
 * 
 */
public class NettyChannel extends AbstractChannel {
 
    private static final ConcurrentMap<io.netty.channel.Channel, NettyChannel>
                channelMap = new ConcurrentHashMap<>();
                
    private final io.netty.channel.Channel channel;

    private Object attachment;
    
    private volatile long timeoutMillis;
    
    private volatile boolean closed;
    
    private NettyChannel(io.netty.channel.Channel channel, ChannelHandler handler, RemoteLocator locator) {
        super(handler, locator);
        if (channel == null) {
            throw new IllegalArgumentException("channel is null");
        }
        this.closed = false;
        this.channel = channel;
    }
    
    public static NettyChannel getOrCreateChannel(io.netty.channel.Channel channel, 
            ChannelHandler handler, RemoteLocator locator) {
        if (channel == null) {
            throw new IllegalArgumentException("channel is null");
        }
        
        NettyChannel nettyChannel = channelMap.get(channel);
        if (nettyChannel == null) {
            NettyChannel nc = new NettyChannel(channel, handler, locator);
            if (nc.isConnected()) {
                nettyChannel = channelMap.putIfAbsent(channel, nc);
            }
            if (nettyChannel == null) {
                nettyChannel = nc;
            }
        }
        return nettyChannel;
    }
    
    public static void removeChannelIfDisconnected(io.netty.channel.Channel channel) {
        if (channel != null && !channel.isActive()) {
            channelMap.remove(channel);
        }
    }

    @Override
    public Object attach() {
        return attachment;
    }

    @Override
    public void attach(Object o) {
        attachment = o;
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public SocketAddress getLocalAddress() {
        return channel.localAddress();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return channel.remoteAddress();
    }
    
    @Override 
    public void setLocator(final RemoteLocator locator) {
        super.setLocator(locator);
        this.timeoutMillis = getOption(TIMEOUT_MS, DEFAULT_TIMEOUT, locator);
    }

    @Override
    public void write(Object message, boolean ensureWritten) throws RemoteException {
        if (isClosed()) {
            throw new RemoteException(this, "channel is closed");
        }
        boolean success = true;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            if (ensureWritten) {
                long timeout = timeoutMillis;
                success = future.await(timeout);
            }
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable cause) {
            throw new RemoteException("failed to send message " + message
                    + " to "+ getRemoteAddress(), cause);
        }
        
        if (!success) {
            throw new RemoteException("failed to send message " + message
                    + " to "+ getRemoteAddress() + ", cause by timeout");
        }
    }

    @Override
    public void close() {
        if (!close0()) {
            return;
        }
        channel.close();
    }

    @Override
    public void close(int timeoutMillis){
        close();
        // TODO: await for channel
    }

    @Override
    public void close(boolean immediately) {
        close();
    }
    
    private boolean close0() {
        removeChannelIfDisconnected(channel);
        
        if (isClosed()) {
            return false;
        }
        closed = true;
        return true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

}
