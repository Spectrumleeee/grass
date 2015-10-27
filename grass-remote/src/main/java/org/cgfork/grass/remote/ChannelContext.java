package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface ChannelContext extends Channel {
    
    Channel getChannel();
    
    ChannelHandler getChannelHandler();
    
    void onConnected() throws RemoteException;
    
    void onDisconnected() throws RemoteException;
    
    void onWritten(Object message) throws RemoteException;
    
    void onRead(Object message) throws RemoteException;
    
    void onCaught(Throwable cause) throws RemoteException;
}
