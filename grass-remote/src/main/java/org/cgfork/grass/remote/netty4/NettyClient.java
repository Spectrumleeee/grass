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
import org.cgfork.grass.remote.Locator;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.transport.AbstractClient;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyClient extends AbstractClient {
    
    private static final EventLoopGroup group = new NioEventLoopGroup();

    private Bootstrap bootstrap;
    
    private volatile Channel channel;

    public NettyClient(Locator locator, ChannelHandler handler) throws RemoteException {
        super(locator, handler);
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
                        pipeline.addLast(new NettyCodec(codec(),
                                channelHandler(), locator()));
                        pipeline.addLast(new NettyInboundHandler(channelHandler(),
                                locator()));
                        pipeline.addLast(new NettyOutboundHandler(channelHandler(),
                                locator()));
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
            boolean ok = future.awaitUninterruptibly(connectTimeoutMillis());
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
                        localAddress(), remoteAddress()), future.cause());
            } else {
                throw new RemoteException(String.format("Failed to open channel [%s->%s], cause by %s",
                        localAddress(), remoteAddress(), "timeout"));
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
        return context.channel();
    }
    
    public NettyContext getContext() {
        Channel ch = channel;
        if (ch == null) {
            return null;
        }
            
        return NettyContext.getContext(ch, channelHandler(), locator());
    }

    public static Future<?> shutdownGracefully() {
            return group.shutdownGracefully();
    }

}
