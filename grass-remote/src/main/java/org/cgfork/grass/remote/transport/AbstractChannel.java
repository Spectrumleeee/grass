package org.cgfork.grass.remote.transport;


import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;

import static org.cgfork.grass.remote.ChannelOption.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public abstract class AbstractChannel implements Channel {

    private volatile RemoteLocator locator;
    
    private volatile boolean ensureWritten;
    
    private volatile boolean closed;
    
    public AbstractChannel(RemoteLocator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator is null");
        }
        this.locator = locator;
        this.closed = false;
        this.ensureWritten = ensureWritten(locator);
    }
    
    public void setLocator(RemoteLocator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator is null");
        }
        this.locator = locator;
        this.ensureWritten = ensureWritten(locator);
    }
    
    public RemoteLocator getRemoteLocator() {
        return locator;
    }
    
    protected void close0() {
        closed = true;
    }
    
    @Override
    public boolean isClosed() {
        return closed;
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
