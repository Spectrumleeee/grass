/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-9
 */
package org.cgfork.grass.remote.transport;


import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;

import static org.cgfork.grass.remote.ChannelOption.*;


/**
 * 
 */
public abstract class AbstractChannel implements Channel {

    private final ChannelHandler handler;
    
    private volatile RemoteLocator locator;
    
    private volatile boolean ensureWritten;
    
    public AbstractChannel(ChannelHandler handler, RemoteLocator locator) {
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        this.handler = handler;
        if (locator == null) {
            throw new IllegalArgumentException("locator is null");
        }
        this.locator = locator;
        this.ensureWritten = ensureWritten(locator);
    }
    
    public void setLocator(RemoteLocator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator is null");
        }
        this.locator = locator;
        this.ensureWritten = ensureWritten(locator);
    }
    
    public RemoteLocator getLocator() {
        return locator;
    }
    
    @Override
    public ChannelHandler getChannelHandler() {
        return handler;
    }
    
    @Override 
    public void write(Object message) throws RemoteException {
        if (isClosed()) {
            throw new RemoteException(this, "channel is closed");
        }

        write(message, ensureWritten);
    }
    
    @Override
    public String toString() {
        return String.format("Channel [local:%s, remote:%s]",
                getLocalAddress(), getRemoteAddress());
    }
}
