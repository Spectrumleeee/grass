package org.cgfork.grass.remote;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface Server {

    Collection<Channel> channels();
    
    Channel channel(InetSocketAddress remoteAddress);
    
    ChannelHandler channelHandler();
    
    InetSocketAddress localAddress();

    void accept(Channel channel) throws RemoteException;

    void remove(Channel channel) throws RemoteException;
    
    void shutdown() throws InterruptedException;
}
