package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.cache.limit.RejectedException;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.Constants;
import org.cgfork.grass.rpc.direct.*;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericChannel implements GenericChannel {

    private final Channel channel;

    private final GenericHandler handler;

    private final int timeoutMillis;

    public DirectGenericChannel(Channel channel, GenericHandler handler) {
        Checker.Arg.notNull(channel, "channel is null");
        Checker.Arg.notNull(handler, "handler is null");
        this.channel = channel;
        this.handler = handler;
        this.timeoutMillis = channel.location().getParameter(Constants.REQ_TIMEOUT_KEY,
                Constants.DEFAULT_REQ_TIMEOUT_MS);
    }

    @Override
    public GenericFuture invoke(Object request) throws GenericException {
        return invoke(request, timeoutMillis);
    }

    @Override
    public GenericFuture invoke(Object request, int timeoutMillis) throws GenericException {
        if (isClosed()) {
            //TODO
            throw new GenericException();
        }
        // init flag
        Flag flag = new Flag(Flag.FLAG_REQ & Flag.FLAG_OK);
        Request newReq = new Request();
        newReq.setFlag(flag);
        newReq.setData(request);
        newReq.setVersion("1.0.0");
        try {
            DefaultFuture future = new DefaultFuture(channel, newReq, timeoutMillis);
            channel.write(newReq);
            return future;
        } catch (RejectedException e) {
            // TODO:
            throw new GenericException();
        } catch (RemoteException e) {
            // TODO:
            throw new GenericException();
        }
    }

    @Override
    public GenericHandler handler() {
        return handler;
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public CloseFuture close() {
        //TODO:
        return null;
    }

    @Override
    public boolean isClosed() {
        return channel.isClosed();
    }
}
