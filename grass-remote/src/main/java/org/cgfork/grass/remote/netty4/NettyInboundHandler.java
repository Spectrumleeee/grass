package org.cgfork.grass.remote.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyInboundHandler extends ChannelInboundHandlerAdapter  {
    
    private final ChannelHandler handler;
    
    private final Location location;
    
    public NettyInboundHandler(ChannelHandler handler, Location location) {
        this.handler = handler;
        this.location = location;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, location);
        
        try {
            context.onConnected();
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, location);
        
        try {
            context.onDisconnected();
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, location);

        try {
            context.onRead(msg);
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, location);
        try {
            context.onCaught(cause);
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
}
