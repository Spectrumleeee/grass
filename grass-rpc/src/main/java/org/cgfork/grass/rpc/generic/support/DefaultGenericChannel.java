package org.cgfork.grass.rpc.generic.support;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.generic.*;
import static org.cgfork.grass.rpc.Constants.*;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultGenericChannel implements GenericChannel {

    private final Channel channel;

    private volatile boolean closed = false;

    public DefaultGenericChannel(Channel channel) {
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
        final CloseFuture future = new DefaultCloseFuture();
        if (closed) {
            future.setValue(null);
            return future;
        }
        closed = true;

        final int timeout = channel.location()
                .getParameter(REQ_TIMEOUT_KEY, DEFAULT_REQ_TIMEOUT_MS);
        if (timeout > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    while (DefaultFuture.contains(channel) &&
                            System.currentTimeMillis() - start < timeout) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            //TODO:
                        }
                    }
                    channel.close();
                }
            }).start();
        }
        return future;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
