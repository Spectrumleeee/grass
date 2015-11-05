package org.cgfork.grass.remote.transport;


import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Locator;
import org.cgfork.grass.remote.RemoteException;

import static org.cgfork.grass.remote.ChannelOption.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public abstract class AbstractChannel extends AbstractPeer implements Channel {

    private volatile boolean forceWritten;
    
    private volatile boolean closed;
    
    public AbstractChannel(Locator locator, ChannelHandler handler) {
        super(locator, handler);
        Checker.Arg.notNull(locator, "locator is not null");
        this.closed = false;
        this.forceWritten = forceWritten(locator);
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
