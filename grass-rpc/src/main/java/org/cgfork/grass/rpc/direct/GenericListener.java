package org.cgfork.grass.rpc.direct;

import org.cgfork.grass.common.future.Listener;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericListener extends Listener<ResponseFuture> {
    void operationComplete(ResponseFuture future) throws Exception;
}
