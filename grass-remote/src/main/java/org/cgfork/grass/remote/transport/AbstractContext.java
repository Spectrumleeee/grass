package org.cgfork.grass.remote.transport;

import java.net.SocketAddress;

import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public abstract class AbstractContext implements ChannelContext {
    
    @Override
    public boolean isConnected() {
        return channel().isConnected();
    }

    @Override
    public SocketAddress localAddress() {
        return channel().localAddress();
    }
    
    @Override
    public SocketAddress remoteAddress() {
        return channel().remoteAddress();
    }

    @Override
    public Location location() {
        return channel().location();
    }

    
    @Override
    public void write(Object message) throws RemoteException {
        channel().write(message);
    }
    
    @Override
    public void write(Object message, boolean ensureWritten) throws RemoteException {
        channel().write(message, ensureWritten);
    }
    
    @Override
    public void close() {
        channel().close();
    }
    
    @Override
    public void close(long timeoutMillis) {
        channel().close(timeoutMillis);
    }
    
    @Override
    public void close(boolean immediately) {
        channel().close(immediately);
    }
    
    @Override
    public boolean isClosed() {
        return channel().isClosed();
    }
    
    @Override
    public void onConnected() throws RemoteException {
        channelHandler().onConnected(this);
    }
    
    @Override
    public void onDisconnected() throws RemoteException {
        channelHandler().onDisconnected(this);
    }

    @Override
    public void onWritten(Object message) throws RemoteException {
        channelHandler().onWritten(this, message);
    }
    
    @Override
    public void onRead(Object message) throws RemoteException {
        channelHandler().onRead(this, message);
    }
    
    @Override
    public void onCaught(Throwable cause) throws RemoteException {
        channelHandler().onCaught(this, cause);
    }
}
