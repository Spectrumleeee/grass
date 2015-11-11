package org.cgfork.grass.rpc.direct;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericChannel {

    GenericFuture invoke(Object request) throws GenericException;

    GenericFuture invoke(Object request, int timeout) throws GenericException;

    GenericHandler handler();

    boolean isConnected();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    CloseFuture close();

    boolean isClosed();
}
