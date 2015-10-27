package org.cgfork.grass.common.future;

import java.util.concurrent.TimeUnit;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface Future<T> {
    Future<T> addListener(Listener<? extends Future<? super T>> listener);

    Future<T> removeListener(
            Listener<? extends Future<? super T>> listener);

    Throwable cause();
    
    boolean isDone();

    boolean isCancelled();
    
    T get() throws InterruptedException, FutureException;
    
    T getNow();
    
    T get(long timeout, TimeUnit unit) throws InterruptedException, FutureException, TimeoutException;
    
    T get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException;
    
    Future<T> setFailure(Throwable cause);
    
    Future<T> setValue(T value);
    
    Future<T> sync() throws InterruptedException;
    
    Future<T> syncUninterruptibly();
    
    Future<T> await() throws InterruptedException;
    
    Future<T> awaitUninterruptibly();
    
    boolean await(long timeout, TimeUnit unit) throws InterruptedException;
    
    boolean await(long timeoutMillis) throws InterruptedException;

    boolean cancel(boolean mayInterruptIfRunning);
}
