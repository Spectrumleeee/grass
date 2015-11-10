package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.*;
import org.cgfork.grass.remote.Client;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyTransporter implements Transporter {

    @Override
    public Client connect(Location location, ChannelHandler handler)
            throws RemoteException {
        return new NettyClient(location, handler);
    }

    @Override
    public Server bind(Location location, ChannelHandler handler)
            throws RemoteException {
        return new NettyServer(location, handler);
    }

}
