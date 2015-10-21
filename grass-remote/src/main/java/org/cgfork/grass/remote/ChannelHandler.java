/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

/**
 * 
 */
public interface ChannelHandler {
    void onConnected(ChannelContext ctx) throws RemoteException;
    
    void onDisconnected(ChannelContext ctx) throws RemoteException;
    
    void onWritten(ChannelContext ctx, Object message) throws RemoteException;
    
    void onRead(ChannelContext ctx, Object message) throws RemoteException;
    
    void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException;
}
