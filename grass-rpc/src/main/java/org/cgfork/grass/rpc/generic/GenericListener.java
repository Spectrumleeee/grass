package org.cgfork.grass.rpc.generic;

import org.cgfork.grass.common.future.Listener;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericListener extends Listener<GenericFuture> {
    void operationComplete(GenericFuture future) throws Exception;
}
