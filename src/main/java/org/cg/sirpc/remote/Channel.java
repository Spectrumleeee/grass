/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cg.sirpc.remote;

import java.net.SocketAddress;

/**
 * 
 */
public interface Channel {
    Object attach();
    
    void attach(Object o);
    
    boolean isConnected();
    
    ChannelHandler getChannelHandler();
    
    SocketAddress getLocalAddress();
    
    SocketAddress getRemoteAddress();
    
    void write(Object message) throws RemoteException;
    
    void write(Object message, boolean ensureWritten) throws RemoteException;
    
    void close();
    
    void close(int timeout);
    
    void close(boolean immediately);
    
    boolean isClosed();
}
