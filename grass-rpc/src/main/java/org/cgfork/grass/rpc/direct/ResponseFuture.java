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
public interface ResponseFuture extends Future<Response> {

    @Override
    ResponseFuture addListener(Listener<? extends Future<? super Response>> listener);

    @Override
    ResponseFuture removeListener(
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
    ResponseFuture setFailure(Throwable cause);

    @Override
    ResponseFuture setValue(Response value);

    @Override
    ResponseFuture sync() throws InterruptedException;

    @Override
    ResponseFuture syncUninterruptibly();

    @Override
    ResponseFuture await() throws InterruptedException;

    @Override
    ResponseFuture awaitUninterruptibly();
}
