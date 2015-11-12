package org.cgfork.grass.rpc.generic;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericServer{
    Collection<GenericChannel> channels();

    GenericChannel channel(InetSocketAddress remoteAddress);

    GenericHandler handler();

    InetSocketAddress localAddress();

    void shutdown() throws InterruptedException;
}
