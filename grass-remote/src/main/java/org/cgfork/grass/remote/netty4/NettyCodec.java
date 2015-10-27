package org.cgfork.grass.remote.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

import org.cgfork.grass.remote.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public class NettyCodec extends ByteToMessageCodec<Object> {
    
    private final Codec codec;

    private final ChannelHandler handler;
    private final RemoteLocator locator;

    public NettyCodec(Codec codec, ChannelHandler handler, RemoteLocator locator) {
        this.codec = codec;
        this.handler = handler;
        this.locator = locator;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception {
        io.netty.channel.Channel nettyChannel = ctx.channel();
        try {
            Channel channel = NettyContext.getContext(nettyChannel, handler, locator);
            ChannelBuffer outBuf = new NettyChannelBuffer(out);
            codec.encode(channel, outBuf, msg);
        } finally {
            NettyContext.removeContextIfDisconnected(nettyChannel);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        io.netty.channel.Channel nettyChannel = ctx.channel();
        try {
            Channel channel = NettyContext.getContext(nettyChannel, handler, locator);
            ChannelBuffer inBuf = new NettyChannelBuffer(in);
            int readerIndex = in.readerIndex();
            if (!codec.decode(channel, inBuf, out)) {
                in.readerIndex(readerIndex);
            }
        } finally {
            NettyContext.removeContextIfDisconnected(nettyChannel);
        }
    }

}
