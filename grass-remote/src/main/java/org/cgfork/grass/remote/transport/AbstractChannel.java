package org.cgfork.grass.remote.transport;


import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;

import static org.cgfork.grass.remote.ChannelOption.*;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public abstract class AbstractChannel extends AbstractPeer implements Channel {

    private volatile boolean forceWritten;
    
    private volatile boolean closed;
    
    public AbstractChannel(Location location, ChannelHandler handler) {
        super(location, handler);
        Checker.Arg.notNull(location, "locator is not null");
        this.closed = false;
        this.forceWritten = forceWritten(location);
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

        write(message, forceWritten);
    }

    @Override
    public String toString() {
        return String.format("Channel [local:%s, remote:%s]",
                localAddress(), remoteAddress());
    }
}
