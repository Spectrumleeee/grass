package org.cgfork.grass.rpc.generic.support;

import org.cgfork.grass.common.cache.limit.RejectedException;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Client;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.rpc.Constants;
import org.cgfork.grass.rpc.generic.*;

import java.net.SocketAddress;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultGenericClient implements GenericClient {

    private final Client client;

    private final GenericChannel channel;

    private final int timeoutMillis;

    public DefaultGenericClient(Client client) {
        Checker.Arg.notNull(client);
        this.client = client;
        this.channel = new DefaultGenericChannel(client);
        this.timeoutMillis = client.location().getParameter(Constants.REQ_TIMEOUT_KEY,
                Constants.DEFAULT_REQ_TIMEOUT_MS);
    }

    @Override
    public void reconnect() throws RemoteException {
        client.reconnect();
    }

    @Override
    public GenericFuture invoke(Object request) throws RemoteException, RejectedException {
        return invoke(request, timeoutMillis);
    }

    @Override
    public GenericFuture invoke(Object request, int timeoutMillis) throws RemoteException, RejectedException {
        if (isClosed()) {
            throw new RemoteException(client, "channel is closed");
        }
        // init flag
        Flag flag = new Flag(Flag.FLAG_REQ);
        GenericRequest newReq = new GenericRequest();
        newReq.setFlag(flag);
        newReq.setData(request);
        newReq.setVersion("1.0.0");
        DefaultFuture future = new DefaultFuture(client, newReq, timeoutMillis);
        write(newReq);
        return future;
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
