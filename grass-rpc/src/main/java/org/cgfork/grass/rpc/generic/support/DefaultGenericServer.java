package org.cgfork.grass.rpc.generic.support;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.Server;
import org.cgfork.grass.rpc.generic.GenericChannel;
import org.cgfork.grass.rpc.generic.GenericHandler;
import org.cgfork.grass.rpc.generic.GenericServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultGenericServer implements GenericServer {

    private final Server server;

    private final GenericHandler handler;

    public DefaultGenericServer(Server server, GenericHandler handler) {
        Checker.Arg.notNull(server);
        Checker.Arg.notNull(handler);
        this.server = server;
        this.handler = handler;
    }

    @Override
    public Collection<GenericChannel> channels() {
        Collection<GenericChannel> collection = new ArrayList<>();
        Collection<Channel> channels = server.channels();
        for (Channel ch : channels) {
            DefaultGenericContext context = DefaultGenericContext.getContext(ch, handler);
            collection.add(context.channel());
        }
        return collection;
    }

    @Override
    public GenericChannel channel(InetSocketAddress remoteAddress) {
        Channel ch = server.channel(remoteAddress);
        DefaultGenericContext context = DefaultGenericContext.getContext(ch, handler);
        return context.channel();
    }

    @Override
    public GenericHandler handler() {
        return handler;
    }

    @Override
    public InetSocketAddress localAddress() {
        return server.localAddress();
    }

    @Override
    public void shutdown() throws InterruptedException {
        server.shutdown();
    }
}
