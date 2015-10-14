/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cg.sirpc.remote.netty4;

import org.cg.sirpc.remote.ChannelHandler;
import org.cg.sirpc.remote.RemoteClient;
import org.cg.sirpc.remote.RemoteException;
import org.cg.sirpc.remote.RemoteLocator;
import org.cg.sirpc.remote.RemoteServer;
import org.cg.sirpc.remote.Transporter;

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
