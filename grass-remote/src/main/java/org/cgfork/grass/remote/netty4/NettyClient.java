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
import io.netty.util.concurrent.Future;

import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.transport.AbstractClient;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyClient extends AbstractClient {
    
    private static final EventLoopGroup group = new NioEventLoopGroup();

    private Bootstrap bootstrap;
    
    private volatile Channel channel;
    
    private final ChannelHandler handler;

    public NettyClient(ChannelHandler handler, RemoteLocator locator) throws RemoteException {
        super(locator);
        this.handler = handler;
        super.open();
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
                                getChannelHandler(), getRemoteLocator()));
                        pipeline.addLast(new NettyInboundHandler(getChannelHandler(),
                                getRemoteLocator()));
                        pipeline.addLast(new NettyOutboundHandler());
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
                            NettyContext.removeContextIfDisconnected(oldChannel);
                        }
                    }
                } finally {
                    if (isClosed()) {
                        try {
                            newChannel.close();
                        } finally {
                            this.channel = null;
                            NettyContext.removeContextIfDisconnected(newChannel);
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
        NettyContext.removeContextIfDisconnected(channel);
    }

    @Override
    protected org.cgfork.grass.remote.Channel getChannel() {
        NettyContext context = getContext();
        if (context == null) {
            return null;
        }
        return context.getChannel();
    }
    
    public NettyContext getContext() {
        Channel ch = channel;
        if (ch == null) {
            return null;
        }
            
        return NettyContext.getContext(ch, handler, getRemoteLocator());
    }



    public static Future<?> shutdownGracefully() {
            return group.shutdownGracefully();
    }

    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }
}
