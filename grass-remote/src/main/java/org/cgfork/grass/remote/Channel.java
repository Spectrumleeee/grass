package org.cgfork.grass.remote;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface Channel {
    boolean isConnected();

    SocketAddress localAddress();
    
    SocketAddress remoteAddress();
    
    void write(Object message) throws RemoteException;
    
    void write(Object message, boolean forceWritten) throws RemoteException;
    
    void close();
    
    void close(boolean immediately);
    
    void close(long timeoutMillis);
    
    boolean isClosed();

    Location location();
}
