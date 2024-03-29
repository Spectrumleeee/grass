package org.cgfork.grass.common.future;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface Listener <F extends Future<?>> {
    void operationComplete(F future) throws Exception;
}