package org.cgfork.grass.remote.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.*;
import org.cgfork.grass.common.utils.NetUtils;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public abstract class AbstractClient extends AbstractChannel implements Client {

    private long timeoutMillis;

    private long connectTimeoutMillis;

    private final Lock connectLock = new ReentrantLock();

    public AbstractClient(Location location, ChannelHandler handler) throws RemoteException {
        super(location, handler);
        Checker.Arg.notNull(handler, "handler is null");
        timeoutMillis = ChannelOption.timeoutMillis(location);
        connectTimeoutMillis = ChannelOption.connectTimeoutMillis(location);
        try {
            doOpen();
            connect();
        } catch (Throwable e) {
            close();
            throw new RemoteException("Failed to connect " + location.toInetSocketAddress(), e);
        }
    }

    public long timeoutMillis() {
        return timeoutMillis;
    }

    public long connectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void write(Object message, boolean forceWritten) throws RemoteException {
        if (!isConnected()) {
            connect();
        }

        Channel channel = getChannel();
        if (channel == null || !channel.isConnected()) {
            throw new RemoteException(channel, "channel is closed.");
        }
        channel.write(message, forceWritten);
    }

    protected void connect() throws RemoteException {
        connectLock.lock();
        try {
            if (isConnected()) {
                return;
            }

            doConnect();

            if (!isConnected()) {
                throw new RemoteException(String.format("Failed to connect %s", remoteAddress()));
            }
        } finally {
            connectLock.unlock();
        }
    }

    protected void disconnect() {
        connectLock.lock();
        try {
            Channel channel = getChannel();
            if (channel != null) {
                channel.close();
            }

            doDisconnect();
        } catch (Exception e) {
            // TODO:
        } finally {
            connectLock.unlock();
        }
    }

    @Override
    public void reconnect() throws RemoteException {
        disconnect();
        connect();
    }

    @Override
    public void close() {
        disconnect();
        doClose();
    }

    @Override
    public void close(long timeoutMillis) {
        close();
        // TODO: await for channel in netty
    }

    @Override
    public void close(boolean immediately) {
        close();
        // TODO: close immediately in mina
    }

    @Override
    public boolean isConnected() {
        Channel channel = getChannel();
        return channel != null && channel.isConnected();
    }

    @Override
    public SocketAddress localAddress() {
        Channel channel = getChannel();
        if (channel == null) {
            return InetSocketAddress.createUnresolved(NetUtils.getLocalHost(), 0);
        }
        return channel.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        Channel channel = getChannel();
        if (channel == null) {
            return getSocketAddress();
        }
        return channel.remoteAddress();
    }

    protected InetSocketAddress getSocketAddress() {
        Location location = locator();
        if (location == null) {
            return null;
        }

        return location.toInetSocketAddress();
    }

    protected abstract void doOpen() throws RemoteException;

    protected abstract void doClose();

    protected abstract void doConnect() throws RemoteException;

    protected abstract void doDisconnect() throws RemoteException;

    protected abstract Channel getChannel();
}
