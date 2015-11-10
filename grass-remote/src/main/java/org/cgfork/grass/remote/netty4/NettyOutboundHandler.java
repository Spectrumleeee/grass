package org.cgfork.grass.remote.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class NettyOutboundHandler extends ChannelOutboundHandlerAdapter {

    private final ChannelHandler handler;

    private final Location location;

    public NettyOutboundHandler(ChannelHandler handler, Location location) {
        this.handler = handler;
        this.location = location;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, location);
        super.write(ctx, msg, promise);
        try {
            context.onWritten(msg);
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
}
