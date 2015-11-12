package org.cgfork.grass.rpc.direct;

import org.cgfork.grass.common.future.Future;
import org.cgfork.grass.common.future.FutureException;
import org.cgfork.grass.common.future.Listener;
import org.cgfork.grass.common.future.TimeoutException;

import java.util.concurrent.TimeUnit;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericFuture extends Future<GenericResponse> {

    @Override
    GenericFuture addListener(Listener<? extends Future<? super GenericResponse>> listener);

    @Override
    GenericFuture removeListener(
            Listener<? extends Future<? super GenericResponse>> listener);

    @Override
    GenericResponse get() throws InterruptedException, FutureException;

    @Override
    GenericResponse getNow();

    @Override
    GenericResponse get(long timeout, TimeUnit unit) throws InterruptedException, FutureException, TimeoutException;

    @Override
    GenericResponse get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException;

    @Override
    GenericFuture setFailure(Throwable cause);

    @Override
    GenericFuture setValue(GenericResponse value);

    @Override
    GenericFuture sync() throws InterruptedException;

    @Override
    GenericFuture syncUninterruptibly();

    @Override
    GenericFuture await() throws InterruptedException;

    @Override
    GenericFuture awaitUninterruptibly();
}
