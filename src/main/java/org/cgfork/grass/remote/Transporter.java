/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;


/**
 * This interface provides an view for creating client and server.
 */
public interface Transporter {
    RemoteClient connect(RemoteLocator locator, ChannelHandler handler)
        throws RemoteException;
    
    RemoteServer bind(RemoteLocator locator, ChannelHandler handler)
        throws RemoteException;
}
