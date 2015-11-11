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
public interface GenericFuture extends Future<Response> {

    @Override
    GenericFuture addListener(Listener<? extends Future<? super Response>> listener);

    @Override
    GenericFuture removeListener(
            Listener<? extends Future<? super Response>> listener);

    @Override
    Response get() throws InterruptedException, FutureException;

    @Override
    Response getNow();

    @Override
    Response get(long timeout, TimeUnit unit) throws InterruptedException, FutureException, TimeoutException;

    @Override
    Response get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException;

    @Override
    GenericFuture setFailure(Throwable cause);

    @Override
    GenericFuture setValue(Response value);

    @Override
    GenericFuture sync() throws InterruptedException;

    @Override
    GenericFuture syncUninterruptibly();

    @Override
    GenericFuture await() throws InterruptedException;

    @Override
    GenericFuture awaitUninterruptibly();
}
