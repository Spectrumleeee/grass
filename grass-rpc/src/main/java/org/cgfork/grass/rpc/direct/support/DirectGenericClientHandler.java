package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.direct.GenericResponse;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericClientHandler implements ChannelHandler {

    @Override
    public void onConnected(ChannelContext ctx) throws RemoteException {
        //TODO: logger or monitor
    }

    @Override
    public void onDisconnected(ChannelContext ctx) throws RemoteException {
        //TODO: logger or monitor and remove all future.
    }

    @Override
    public void onWritten(ChannelContext ctx, Object message) throws RemoteException {
        //TODO: logger or monitor
    }

    @Override
    public void onRead(ChannelContext ctx, Object message) throws RemoteException {
        if (message != null && message instanceof GenericResponse) {
            DefaultFuture.finishRequested(ctx.channel(), (GenericResponse)message);
        }
        //TODO: logger and warning
    }

    @Override
    public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {

    }
}
