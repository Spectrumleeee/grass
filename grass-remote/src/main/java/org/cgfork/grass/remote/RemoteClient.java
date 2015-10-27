package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface RemoteClient extends Channel {
    void reconnect() throws RemoteException;


    ChannelHandler getChannelHandler();
}
