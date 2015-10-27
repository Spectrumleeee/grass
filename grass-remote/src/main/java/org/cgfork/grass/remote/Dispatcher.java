package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Dispatcher {
    ChannelHandler dispatch(ChannelHandler handler, RemoteLocator locator);
}
