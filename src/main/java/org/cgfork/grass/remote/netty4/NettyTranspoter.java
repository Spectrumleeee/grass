/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteClient;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.RemoteServer;
import org.cgfork.grass.remote.Transporter;

/**
 * 
 */
public class NettyTranspoter implements Transporter {

    @Override
    public RemoteClient connect(RemoteLocator locator, ChannelHandler handler) 
            throws RemoteException {
        return new NettyClient(handler, locator);
    }

    @Override
    public RemoteServer bind(RemoteLocator locator, ChannelHandler handler)
            throws RemoteException {
        return null;
    }

}
