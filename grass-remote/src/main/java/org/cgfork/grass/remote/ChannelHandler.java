package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface ChannelHandler {
    void onConnected(ChannelContext ctx) throws RemoteException;
    
    void onDisconnected(ChannelContext ctx) throws RemoteException;

    void onWritten(ChannelContext ctx, Object message) throws RemoteException;

    void onRead(ChannelContext ctx, Object message) throws RemoteException;
    
    void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException;
}
