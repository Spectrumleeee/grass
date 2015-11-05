package org.cgfork.grass.rpc;

import org.cgfork.grass.remote.Locator;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Transporter<T> {

    Exporter<T> export(Invoker<T> invoker) throws RpcException;

    Invoker<T> refer(Class<T> clazz, Locator locator) throws RpcException;

    void destroy();
}
