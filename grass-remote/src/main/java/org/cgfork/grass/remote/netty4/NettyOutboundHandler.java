package org.cgfork.grass.remote.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteLocator;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyOutboundHandler extends ChannelOutboundHandlerAdapter {

    private final ChannelHandler handler;

    private final RemoteLocator locator;

    public NettyOutboundHandler(ChannelHandler handler, RemoteLocator locator) {
        this.handler = handler;
        this.locator = locator;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, locator);
        super.write(ctx, msg, promise);
        try {
            context.onWritten(msg);
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }

}
