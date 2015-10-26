/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

/**
 * 
 */
public interface RemoteClient extends Channel {
    void reconnect() throws RemoteException;


    ChannelHandler getChannelHandler();
}
