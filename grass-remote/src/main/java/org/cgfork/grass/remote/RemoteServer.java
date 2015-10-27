package org.cgfork.grass.remote;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public interface RemoteServer {
    
    boolean isBound();
    
    Collection<Channel> getChannels();
    
    Channel getChannel(InetSocketAddress remoteAddress);
    
    ChannelHandler getChannelHandler();
    
    InetSocketAddress getLocalAddress();
    
    void send(Object message) throws RemoteException;
    
    void send(Object message, boolean sent) throws RemoteException;
    
    void close();
    
    void close(int timeout);
    
    void close(boolean immediately);
    
    boolean isClosed();
}
