package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.direct.GenericContext;
import org.cgfork.grass.rpc.direct.GenericHandler;
import org.cgfork.grass.rpc.direct.GenericRequest;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericServerHandler implements ChannelHandler {

    private final GenericHandler handler;

    public DirectGenericServerHandler(GenericHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onConnected(ChannelContext ctx) throws RemoteException {
        GenericContext context = DirectGenericContext.getContext(ctx.channel(), handler);

        try {
            context.handleConnected();
        } catch (Exception e) {
            throw new RemoteException("Failed to handle connected event", e);
        } finally {
            DirectGenericContext.removeContextIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void onDisconnected(ChannelContext ctx) throws RemoteException {
        GenericContext context = DirectGenericContext.getContext(ctx.channel(), handler);

        try {
            context.handleDisconnected();
        } catch (Exception e) {
            throw new RemoteException("Failed to handle disconnected event", e);
        } finally {
            DirectGenericContext.removeContextIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void onWritten(ChannelContext ctx, Object message) throws RemoteException {
        // ignore
    }

    @Override
    public void onRead(ChannelContext ctx, Object message) throws RemoteException {
        GenericContext context = DirectGenericContext.getContext(ctx.channel(), handler);

        try {
            if (message instanceof GenericRequest) {
                context.handleRequest((GenericRequest)message);
            }
            //TODO:
        } catch (Exception e) {
            throw new RemoteException("Failed handle request", e);
        } finally {
            DirectGenericContext.removeContextIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {
        GenericContext context = DirectGenericContext.getContext(ctx.channel(), handler);

        try {
            if (cause instanceof RemoteException) {
                context.handleException(cause.getCause());
            } else {
                context.handleException(cause);
            }
        } catch (Exception e) {
            throw new RemoteException("Failed to handle cause event", e);
        } finally {
            DirectGenericContext.removeContextIfDisconnected(ctx.channel());
        }
    }
}
