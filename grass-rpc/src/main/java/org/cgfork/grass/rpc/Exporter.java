package org.cgfork.grass.rpc;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Exporter<T> {

    Invoker<T> getInvoker();

    void unExport();
}