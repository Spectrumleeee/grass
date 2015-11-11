package org.cgfork.grass.rpc.direct;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericServer{
    Collection<GenericChannel> channels();

    GenericChannel channel(InetSocketAddress remoteAddress);

    GenericChannel handler();

    InetSocketAddress localAddress();

    void shutdown() throws InterruptedException;
}
