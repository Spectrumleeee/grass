/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-10
 */
package org.cgfork.grass.remote.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.cgfork.grass.common.addon.AbstractLoader;
import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelOption;
import org.cgfork.grass.remote.Codec;
import org.cgfork.grass.remote.RemoteClient;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.utils.NetUtils;
/**
 * 
 */
public abstract class AbstractClient extends AbstractChannel implements RemoteClient {

    private Codec codec;
    
    private long timeoutMillis;
    
    private long connectTimeoutMillis;
    
    private final Lock connectLock = new ReentrantLock();
    
    public AbstractClient(RemoteLocator locator) throws RemoteException {
        super(locator);
        codec = getCodec(locator);
        timeoutMillis = ChannelOption.getTimeoutMillis(locator);
        connectTimeoutMillis = ChannelOption.getConnectTimeoutMillis(locator);
    }
    
    protected void open() throws RemoteException {
        try {
            doOpen();
            connect();
        } catch (RemoteException e) {
            close();
            throw e;
        }
    }

    public Codec getCodec() {
        return codec;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public long getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }
    
    @Override
    public void setLocator(final RemoteLocator locator) {
        super.setLocator(locator);
        timeoutMillis = ChannelOption.getTimeoutMillis(locator);
        connectTimeoutMillis = ChannelOption.getConnectTimeoutMillis(locator);
    }
    
    public void write(Object message, boolean ensureWritten) throws RemoteException {
        if (!isConnected()) {
            connect();
        }
        
        Channel channel = getChannel();
        if (channel == null || !channel.isConnected()) {
            throw new RemoteException(channel, "channel is closed.");
        }
        channel.write(message, ensureWritten);
    }
    
    protected void connect() throws RemoteException {
        connectLock.lock();
        try {
            if (isConnected()) {
                return;
            }
            
            doConnect();
            
            if (!isConnected()) {
                throw new RemoteException(String.format("Failed to connect %s", getRemoteAddress()));
            }
        } finally {
            connectLock.unlock();
        }
    }
    
    protected void disconnect() {
        connectLock.lock();
        try {
            Channel channel = getChannel();
            if (channel != null) {
                channel.close();
            }
            
            doDisconnect();
        } catch (Exception e) {
            // TODO:
        } finally {
            connectLock.unlock();
        }
    }
    
    @Override
    public void reconnect() throws RemoteException {
        disconnect();
        connect();
    }
    
    @Override
    public void close() {
        disconnect();
        doClose();
    }
    
    @Override
    public void close(long timeoutMillis){
        close();
        // TODO: await for channel in netty
    }

    @Override
    public void close(boolean immediately) {
        close();
        // TODO: close immediately in mina
    }

    @Override
    public boolean isConnected() {
        Channel channel = getChannel();
        if (channel == null) {
            return false;
        }
        return channel.isConnected();
    }
    
    @Override
    public SocketAddress getLocalAddress() {
        Channel channel = getChannel();
        if (channel == null) {
            return InetSocketAddress.createUnresolved(NetUtils.getLocalHost(), 0);
        }
        return channel.getLocalAddress();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        Channel channel = getChannel();
        if (channel == null) {
            return getSocketAddress();
        }
        return channel.getRemoteAddress();
    }

    protected InetSocketAddress getSocketAddress() {
        RemoteLocator locator = getRemoteLocator();
        if (locator == null) {
            return null;
        }
        
        return locator.toInetSocketAddress();
    }
    
    protected abstract void doOpen() throws RemoteException;
    
    protected abstract void doClose();
    
    protected abstract void doConnect() throws RemoteException;
    
    protected abstract void doDisconnect() throws RemoteException;
    
    protected abstract Channel getChannel();

    protected static Codec getCodec(RemoteLocator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator is null");
        }

        AddonLoader<Codec> loader = null;
        try {
            loader = AbstractLoader.getAddonLoader(Codec.class);
        } catch (Exception e) {
            // TODO:
        }
        if (loader == null) {
            // TODO: throw not found Exception
        }
        
        String codecClass = locator.getParameter("codecClass");
        if (codecClass != null) {
            try {
                Class<?> clazz = Class.forName(codecClass);
                return loader.getAddon(clazz);
            } catch (ClassNotFoundException e) {
                // TODO: ignore
            }
        }
        
        String codec = locator.getParameter("codec");
        if (codec == null) {
            return null;
        }
        
        return loader.getAddon(codec);
    }
}
