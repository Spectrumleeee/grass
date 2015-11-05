package org.cgfork.grass.rpc;

import org.cgfork.grass.remote.Locator;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Peer {

    Locator getLocator();

    void destroy();
}
