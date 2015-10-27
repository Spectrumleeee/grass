package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public interface Transporter {
    RemoteClient connect(RemoteLocator locator, ChannelHandler handler)
        throws RemoteException;
    
    RemoteServer bind(RemoteLocator locator, ChannelHandler handler)
        throws RemoteException;
}
