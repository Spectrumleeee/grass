/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-10
 */
package org.cgfork.grass.remote.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.transport.AbstractClient;

/**
 * 
 */
public class NettyClient extends AbstractClient {
    
    private static final EventLoopGroup group;
    
    static {
        group = new NioEventLoopGroup();
    }

    private Object attachment;
    
    private Bootstrap bootstrap;
    
    private volatile Channel channel;

    public NettyClient(ChannelHandler handler, RemoteLocator locator) throws RemoteException {
        super(handler, locator);
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
    protected void doOpen() throws RemoteException {
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyCodec(getCodec(),
                                NettyClient.this));
                        pipeline.addLast(new NettyInboundHandler(getLocator(), 
                                getChannelHandler()));
                        pipeline.addLast(new NettyOutboundHandler(getLocator(), 
                                getChannelHandler()));
                    }
                });
    }

    @Override
    protected void doClose() {
        //ignore
    }

    @Override
    protected void doConnect() throws RemoteException {
        ChannelFuture future = bootstrap.connect(getSocketAddress());
        try {
            boolean ok = future.awaitUninterruptibly(getConnectTimeoutMillis());
            if (ok && future.isSuccess()) {
                Channel newChannel = future.channel();
                try {
                    Channel oldChannel = this.channel;
                    if (oldChannel != null) {
                        try {
                            oldChannel.close();
                        } finally {
                            NettyChannel.removeChannelIfDisconnected(oldChannel);
                        }
                    }
                } finally {
                    if (isClosed()) {
                        try {
                            newChannel.close();
                        } finally {
                            this.channel = null;
                            NettyChannel.removeChannelIfDisconnected(newChannel);
                        }
                    } else {
                        this.channel = newChannel;
                    }
                }
            } else if (future.cause() != null) {
                throw new RemoteException(String.format("Failed to open channel [%s->%s]",
                        getLocalAddress(), getRemoteAddress()), future.cause());
            } else {
                throw new RemoteException(String.format("Failed to open channel [%s->%s], cause by %s",
                        getLocalAddress(), getRemoteAddress(), "timeout")); 
            }
        } finally {
            if (!isConnected()) {
                future.cancel(false);
            }
        }
    }

    @Override
    protected void doDisconnect() throws RemoteException {
        NettyChannel.removeChannelIfDisconnected(channel);
    }

    @Override
    protected org.cgfork.grass.remote.Channel getChannel() {
        Channel ch = channel;
        if (ch == null || !ch.isActive()) {
            return null;
        }
            
        return NettyChannel.getOrCreateChannel(ch, getChannelHandler(), getLocator());
    }

}
