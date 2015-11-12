package org.cgfork.grass.remote.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.cgfork.grass.remote.*;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.transport.AbstractServer;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class NettyServer extends AbstractServer implements ChannelHandler {

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private io.netty.channel.Channel channel;

    private Map<String, Channel> channels = new HashMap<>();

    public NettyServer(Location location, ChannelHandler handler) throws RemoteException {
        super(location, handler);
    }

    @Override
    protected void doOpen() throws RemoteException {
        if (bossGroup == null)
            bossGroup = new NioEventLoopGroup();
        if (workerGroup == null)
            workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyCodec(codec(),
                                channelHandler(), location()));
                        pipeline.addLast(new NettyInboundHandler(NettyServer.this, location()));
                        pipeline.addLast(new NettyOutboundHandler(NettyServer.this, location()));
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture future = bootstrap.bind(bindAddress()).sync();
            channel = future.channel();
        } catch (Exception e) {
            throw new RemoteException("Failed to bind " + bindAddress(), e);
        }
    }

    @Override
    public Collection<Channel> channels() {
        Collection<Channel> collection = new HashSet<>();
        for (Channel ch : channels.values()) {
            if (ch.isConnected()) {
                collection.add(ch);
            } else {
                channels.remove(ch.remoteAddress().toString());
            }
        }
        return collection;
    }

    @Override
    public Channel channel(InetSocketAddress remoteAddress) {
        return channels.get(remoteAddress.toString());
    }

    @Override
    public void accept(Channel channel) throws RemoteException {
        if (channels.size() > maxAcceptedConnections()) {
            channel.close();
            return;
        }
        channels.put(channel.remoteAddress().toString(), channel);
    }

    @Override
    public void remove(Channel channel) throws RemoteException {
        channels.remove(channel.remoteAddress().toString());
    }

    @Override
    public void shutdown() throws InterruptedException {
        try {
            if (channel != null) {
                channel.close().sync();
            }

            Collection<Channel> channels = channels();

            if (channels != null && channels.size() > 0) {
                for (Channel ch : channels) {
                    try {
                        ch.close();
                    } catch (Exception e) {
                        //TODO: logger
                    }
                }
                channels.clear();
            }

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void onConnected(ChannelContext ctx) throws RemoteException {
        accept(ctx.channel());
        channelHandler().onConnected(ctx);
    }

    @Override
    public void onDisconnected(ChannelContext ctx) throws RemoteException {
        remove(ctx.channel());
        channelHandler().onDisconnected(ctx);
    }

    @Override
    public void onWritten(ChannelContext ctx, Object message) throws RemoteException {
        channelHandler().onWritten(ctx, message);
    }

    @Override
    public void onRead(ChannelContext ctx, Object message) throws RemoteException {
        channelHandler().onRead(ctx, message);
    }

    @Override
    public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {
        channelHandler().onCaught(ctx, cause);
    }
}
