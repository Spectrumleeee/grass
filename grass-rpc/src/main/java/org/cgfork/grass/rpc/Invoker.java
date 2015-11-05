package org.cgfork.grass.rpc;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Invoker<T> extends Peer {
    Class<T> getInterface();

    InvokerFuture<T> invoke() throws RpcException;
}
