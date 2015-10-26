/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

import java.net.SocketAddress;

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
