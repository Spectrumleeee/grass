/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.remote.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelBuffer;
import org.cgfork.grass.remote.Codec;

/**
 * 
 */
public class NettyCodec extends ByteToMessageCodec<Object> {
    
    private final Codec codec;
    
    private final Channel channel;
    
    public NettyCodec(Codec codec, Channel channel) {
        this.codec = codec;
        this.channel = channel;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception {
        ChannelBuffer outBuf = new NettyChannelBuffer(out);
        // TODO: more useful channel buffer
        try {
            codec.encode(channel, outBuf, msg);
        } finally {
            
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        ChannelBuffer inBuf = new NettyChannelBuffer(in);
        
        int readerIndex = in.readerIndex();
        if (!codec.decode(channel, inBuf, out)) {
            in.readerIndex(readerIndex);
        }
    }

}