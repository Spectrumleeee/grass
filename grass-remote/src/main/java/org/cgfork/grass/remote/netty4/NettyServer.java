package org.cgfork.grass.remote.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.transport.AbstractServer;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class NettyServer extends AbstractServer {

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private io.netty.channel.Channel channel;

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
                        pipeline.addLast(new NettyInboundHandler(channelHandler(),
                                location()));
                        pipeline.addLast(new NettyOutboundHandler(channelHandler(),
                                location()));
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
    public Collection<Channel> getChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return null;
    }

    @Override
    public void shutdown() throws InterruptedException {
        try {
            if (channel != null) {
                channel.close().sync();
            }

            Collection<Channel> channels = getChannels();

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
}
