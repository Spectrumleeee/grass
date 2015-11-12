package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.direct.*;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericContext implements GenericContext {

    private static final ConcurrentMap<Channel, DirectGenericContext> ctxMap = new ConcurrentHashMap<>();

    private final GenericChannel channel;

    private final GenericHandler handler;

    private DirectGenericContext(GenericChannel channel, GenericHandler handler) {
        this.channel = channel;
        this.handler = handler;
    }

    public static DirectGenericContext getContext(Channel channel, GenericHandler handler) {
        Checker.Arg.notNull(channel, "channel is null");

        DirectGenericContext context = ctxMap.get(channel);
        if (context == null) {
            DirectGenericChannel newChannel = new DirectGenericChannel(channel);
            if (newChannel.isConnected()) {
                context = ctxMap.putIfAbsent(channel, new DirectGenericContext(newChannel, handler));
            }

            if (context == null) {
                context = new DirectGenericContext(newChannel, handler);
            }
        }
        return context;
    }

    public static void removeContextIfDisconnected(Channel channel) {
        if (channel != null && !channel.isConnected()) {
            ctxMap.remove(channel);
        }
    }

    @Override
    public GenericChannel channel() {
        return channel;
    }

    @Override
    public void handleConnected() throws Exception {
        handler.handleConnected(this);
    }

    @Override
    public void handleRequest(GenericRequest request) throws Exception {
        handler.handleRequest(this, request);
    }

    @Override
    public void handleDisconnected() throws Exception {
        handler.handleDisconnected(this);
    }

    @Override
    public void handleException(Throwable cause) throws Exception {
        handler.handleException(this, cause);
    }

    @Override
    public void write(Object message) throws RemoteException {
        channel.write(message);
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
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
    public CloseFuture close() {
        return channel.close();
    }

    @Override
    public boolean isClosed() {
        return channel.isClosed();
    }
}
