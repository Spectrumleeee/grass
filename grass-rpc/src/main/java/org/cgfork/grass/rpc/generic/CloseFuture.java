package org.cgfork.grass.rpc.generic;

import org.cgfork.grass.common.future.Future;
import org.cgfork.grass.common.future.FutureException;
import org.cgfork.grass.common.future.Listener;
import org.cgfork.grass.common.future.TimeoutException;

import java.util.concurrent.TimeUnit;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface CloseFuture extends Future<Void> {
    @Override
    CloseFuture addListener(Listener<? extends
            Future<? super Void>> listener);

    @Override
    CloseFuture removeListener(
            Listener<? extends Future<? super Void>> listener);

    @Override
    Void get() throws InterruptedException, FutureException;

    @Override
    Void getNow();

    @Override
    Void get(long timeout, TimeUnit unit)
            throws InterruptedException, FutureException, TimeoutException;

    @Override
    Void get(long timeoutMillis)
            throws InterruptedException, FutureException, TimeoutException;

    @Override
    CloseFuture setFailure(Throwable cause);

    @Override
    CloseFuture setValue(Void value);

    @Override
    CloseFuture sync() throws InterruptedException;

    @Override
    CloseFuture syncUninterruptibly();

    @Override
    CloseFuture await() throws InterruptedException;

    @Override
    CloseFuture awaitUninterruptibly();
}
