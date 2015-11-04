package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteClient;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.RemoteServer;
import org.cgfork.grass.remote.Transporter;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyTransporter implements Transporter {

    @Override
    public RemoteClient connect(RemoteLocator locator, ChannelHandler handler) 
            throws RemoteException {
        return new NettyClient(locator, handler);
    }

    @Override
    public RemoteServer bind(RemoteLocator locator, ChannelHandler handler)
            throws RemoteException {
        return new NettyServer(locator, handler);
    }

}
