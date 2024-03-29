package org.cgfork.grass.remote.netty4;

import io.netty.channel.ChannelFuture;

import java.net.SocketAddress;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.ChannelOption;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.transport.AbstractChannel;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class NettyChannel extends AbstractChannel {

    private final io.netty.channel.Channel channel;

    private volatile long timeoutMillis;

    public NettyChannel(io.netty.channel.Channel channel, Location location, ChannelHandler handler) {
        super(location, handler);
        Checker.Arg.notNull(location, "channel is null");
        this.channel = channel;
        this.timeoutMillis = ChannelOption.timeoutMillis(location);
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public SocketAddress localAddress() {
        return channel.localAddress();
    }

    @Override
    public SocketAddress remoteAddress() {
        return channel.remoteAddress();
    }

    @Override
    public void write(Object message, boolean ensureWritten) throws RemoteException {
        if (isClosed()) {
            throw new RemoteException(this, "channel is closed");
        }

        boolean success = true;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            if (ensureWritten) {
                long timeout = timeoutMillis;
                success = future.await(timeout);
            }
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable cause) {
            throw new RemoteException("failed to send message " + message
                    + " to "+ remoteAddress(), cause);
        }
        
        if (!success) {
            throw new RemoteException("failed to send message " + message
                    + " to "+ remoteAddress() + ", cause by timeout");
        }
    }

    @Override
    public void close() {
        NettyContext.removeContextIfDisconnected(channel);
        
        if (isClosed()) {
            return;
        }
        close0();
        channel.close();
    }

    @Override
    public void close(long timeoutMillis){
        close();
        // TODO: await for channel
    }

    @Override
    public void close(boolean immediately) {
        close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NettyChannel that = (NettyChannel) o;

        return channel.equals(that.channel);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + channel.hashCode();
        return result;
    }
}
