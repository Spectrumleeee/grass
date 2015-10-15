/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-10
 */
package org.cgfork.grass.remote;

/**
 * 
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
