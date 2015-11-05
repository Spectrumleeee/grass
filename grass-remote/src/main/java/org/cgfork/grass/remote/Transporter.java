package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Transporter {
    Client connect(Locator locator, ChannelHandler handler)
        throws RemoteException;
    
    Server bind(Locator locator, ChannelHandler handler)
        throws RemoteException;
}
