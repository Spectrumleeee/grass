package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.*;
import org.cgfork.grass.remote.Client;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyTransporter implements Transporter {

    @Override
    public Client connect(Locator locator, ChannelHandler handler)
            throws RemoteException {
        return new NettyClient(locator, handler);
    }

    @Override
    public Server bind(Locator locator, ChannelHandler handler)
            throws RemoteException {
        return new NettyServer(locator, handler);
    }

}
