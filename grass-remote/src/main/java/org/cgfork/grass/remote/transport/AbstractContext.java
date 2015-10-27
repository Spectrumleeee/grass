package org.cgfork.grass.remote.transport;

import java.net.SocketAddress;

import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.RemoteException;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public abstract class AbstractContext implements ChannelContext {
    
    @Override
    public boolean isConnected() {
        return getChannel().isConnected();
    }

    @Override
    public SocketAddress getLocalAddress() {
        return getChannel().getLocalAddress();
    }
    
    @Override
    public SocketAddress getRemoteAddress() {
        return getChannel().getRemoteAddress();
    }
    
    @Override
    public void write(Object message) throws RemoteException {
        getChannel().write(message);
    }
    
    @Override
    public void write(Object message, boolean ensureWritten) throws RemoteException {
        getChannel().write(message, ensureWritten);
    }
    
    @Override
    public void close() {
        getChannel().close();
    }
    
    @Override
    public void close(long timeoutMillis) {
        getChannel().close(timeoutMillis);
    }
    
    @Override
    public void close(boolean immediately) {
        getChannel().close(immediately);
    }
    
    @Override
    public boolean isClosed() {
        return getChannel().isClosed();
    }
    
    @Override
    public void onConnected() throws RemoteException {
        getChannelHandler().onConnected(this);
    }
    
    @Override
    public void onDisconnected() throws RemoteException {
        getChannelHandler().onDisconnected(this);
    }
    
    @Override
    public void onWritten(Object message) throws RemoteException {
        getChannelHandler().onWritten(this, message);
    }
    
    @Override
    public void onRead(Object message) throws RemoteException {
        getChannelHandler().onRead(this, message);
    }
    
    @Override
    public void onCaught(Throwable cause) throws RemoteException {
        getChannelHandler().onCaught(this, cause);
    }
}
