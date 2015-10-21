/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * 
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
