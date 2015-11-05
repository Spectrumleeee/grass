package org.cgfork.grass.rpc;

import org.cgfork.grass.common.future.Future;
import org.cgfork.grass.common.future.Listener;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface InvokerFuture<T> extends Future<T> {

    @Override
    InvokerFuture<T> addListener(Listener<? extends Future<? super T>> listener);

    @Override
    InvokerFuture<T> removeListener(
            Listener<? extends Future<? super T>> listener);

    @Override
    InvokerFuture<T> setFailure(Throwable cause);

    @Override
    InvokerFuture<T> setValue(T value);

    @Override
    InvokerFuture<T> sync() throws InterruptedException;

    @Override
    InvokerFuture<T> syncUninterruptibly();

    @Override
    InvokerFuture<T> await() throws InterruptedException;

    @Override
    InvokerFuture<T> awaitUninterruptibly();
}
