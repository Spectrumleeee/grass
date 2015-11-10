package org.cgfork.grass.remote;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface Transporter {
    Client connect(Location location, ChannelHandler handler)
        throws RemoteException;
    
    Server bind(Location location, ChannelHandler handler)
        throws RemoteException;
}
