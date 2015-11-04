package org.cgfork.grass.remote.transport;

import org.cgfork.grass.common.addon.support.AbstractLoader;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.common.utils.NetUtils;
import org.cgfork.grass.remote.*;

import java.net.InetSocketAddress;
import java.net.URL;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public abstract class AbstractServer extends AbstractPeer implements RemoteServer {

    private InetSocketAddress localAddress;

    private InetSocketAddress bindAddress;

    private int maxAcceptedConnections;

    private int idleTimeout;

    public AbstractServer(RemoteLocator locator, ChannelHandler handler) throws RemoteException {
        super(locator, handler);
        localAddress = locator.toInetSocketAddress();
        bindAddress = getBindAddress(locator);
        maxAcceptedConnections = ChannelOption.maxAcceptedConnections(locator);
        idleTimeout = ChannelOption.idleTimeout(locator);

        try {
            doOpen();
        } catch (Throwable e) {
            throw new RemoteException("Failed to open server on " + bindAddress, e);
        }
    }

    protected abstract void doOpen() throws RemoteException;

    public InetSocketAddress localAddress() {
        return localAddress;
    }

    public InetSocketAddress bindAddress() {
        return bindAddress;
    }

    public int maxAcceptedConnections() {
        return maxAcceptedConnections;
    }

    public int idleTimeoutMs() {
        return idleTimeout;
    }

    private static InetSocketAddress getBindAddress(RemoteLocator locator) {
        URL url = locator.getUrl();

        if (NetUtils.isValidLocalHost(url.getHost())) {
            return new InetSocketAddress(NetUtils.ANYHOST, url.getPort());
        }
        return new InetSocketAddress(url.getHost(), url.getPort());
    }
}
