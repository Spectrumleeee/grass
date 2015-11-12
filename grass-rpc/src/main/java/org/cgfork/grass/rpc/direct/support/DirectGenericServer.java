package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.Server;
import org.cgfork.grass.rpc.direct.GenericChannel;
import org.cgfork.grass.rpc.direct.GenericHandler;
import org.cgfork.grass.rpc.direct.GenericServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericServer implements GenericServer {

    private final Server server;

    private final GenericHandler handler;

    public DirectGenericServer(Server server, GenericHandler handler) {
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
            DirectGenericContext context = DirectGenericContext.getContext(ch, handler);
            collection.add(context.channel());
        }
        return collection;
    }

    @Override
    public GenericChannel channel(InetSocketAddress remoteAddress) {
        Channel ch = server.channel(remoteAddress);
        DirectGenericContext context = DirectGenericContext.getContext(ch, handler);
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
        //TODO:
    }
}
