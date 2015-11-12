package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.direct.*;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericChannel implements GenericChannel {

    private final Channel channel;

    private volatile boolean closed = false;

    public DirectGenericChannel(Channel channel) {
        Checker.Arg.notNull(channel, "channel is null");
        this.channel = channel;
    }

    @Override
    public void write(Object message) throws RemoteException {
        channel.write(message);
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
        closed = true;
        //TODO:
        return null;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
