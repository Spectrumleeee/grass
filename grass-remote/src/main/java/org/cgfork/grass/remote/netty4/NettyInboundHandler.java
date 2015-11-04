package org.cgfork.grass.remote.netty4;

import io.netty.channel.ChannelDuplexHandler;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteLocator;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyInboundHandler extends ChannelInboundHandlerAdapter  {
    
    private final ChannelHandler handler;
    
    private final RemoteLocator locator;
    
    public NettyInboundHandler(ChannelHandler handler, RemoteLocator locator) {
        this.handler = handler;
        this.locator = locator;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, locator);
        
        try {
            context.onConnected();
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, locator);
        
        try {
            context.onDisconnected();
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, locator);

        try {
            context.onRead(msg);
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        NettyContext context = NettyContext.getContext(ctx.channel(), handler, locator);
        
        try {
            context.onCaught(cause);
        } finally {
            NettyContext.removeContextIfDisconnected(ctx.channel());
        }
    }
}
