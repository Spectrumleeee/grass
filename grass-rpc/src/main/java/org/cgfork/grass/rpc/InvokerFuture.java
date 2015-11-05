package org.cgfork.grass.rpc;

import org.cgfork.grass.common.future.Future;
import org.cgfork.grass.common.future.Listener;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface InvokerFuture extends Future<Object> {

    @Override
    InvokerFuture addListener(Listener<? extends Future<? super Object>> listener);

    @Override
    InvokerFuture removeListener(
            Listener<? extends Future<? super Object>> listener);

    @Override
    InvokerFuture setFailure(Throwable cause);

    @Override
    InvokerFuture setValue(Object value);

    @Override
    InvokerFuture sync() throws InterruptedException;

    @Override
    InvokerFuture syncUninterruptibly();

    @Override
    InvokerFuture await() throws InterruptedException;

    @Override
    InvokerFuture awaitUninterruptibly();
}
