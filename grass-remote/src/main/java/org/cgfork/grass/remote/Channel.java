package org.cgfork.grass.remote;

import java.net.SocketAddress;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Channel {
    boolean isConnected();

    SocketAddress getLocalAddress();
    
    SocketAddress getRemoteAddress();
    
    void write(Object message) throws RemoteException;
    
    void write(Object message, boolean ensureWritten) throws RemoteException;
    
    void close();
    
    void close(boolean immediately);
    
    void close(long timeoutMillis);
    
    boolean isClosed();
}
