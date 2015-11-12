package org.cgfork.grass.rpc.generic;

import org.cgfork.grass.remote.RemoteException;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericChannel {

    void write(Object message) throws RemoteException;

    boolean isConnected();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    CloseFuture close();

    boolean isClosed();
}
