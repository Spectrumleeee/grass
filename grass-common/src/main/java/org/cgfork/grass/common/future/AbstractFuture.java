/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.common.future;

import io.netty.util.Signal;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.TimeUnit;

import org.cgfork.grass.common.utils.Latch;

/**
 * 
 */
public abstract class AbstractFuture<T> implements Future<T> {
    
    private static final Signal SUCCESS = Signal.valueOf(AbstractFuture.class
            .getName() + ".SUCCESS");

    private final Latch latch;
    
    private volatile Object value;
    
    // create a future and lock the latch.
    public AbstractFuture() {
        latch = new Latch();
    }
    
    @Override
    public T get() throws InterruptedException, FutureException, TimeoutException {
        await();

        Throwable cause = cause();
        if (cause == null) {
            return getNow();
        }
        if (cause instanceof TimeoutException) {
            throw (TimeoutException) cause;
        }
        throw new FutureException(cause);
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, FutureException, TimeoutException {
        if (await(timeout, unit)) {
            Throwable cause = cause();
            if (cause == null) {
                return getNow();
            }
            
            if (cause instanceof TimeoutException) {
                throw (TimeoutException) cause;
            }
            throw new FutureException(cause);
        }
        throw new TimeoutException();
    }
    
    @Override
    public boolean isDone() {
        return latch.isDone();
    }
    
    /**
     * getNow will return null if the value is a exception or SUCCESS.
     */
    @Override 
    @SuppressWarnings("unchecked")
    public T getNow() {
        Object value = this.value;
        if (value instanceof ThrowableHolder || value == SUCCESS) {
            return null;
        }

        return (T) value;
    }
    
    @Override
    public Throwable cause() {
        Object value = this.value;
        if (value instanceof ThrowableHolder) {
            return ((ThrowableHolder) value).cause;
        }
        return null;
    }
    
    @Override
    public Future<T> sync() throws InterruptedException {
        await();
        rethrowIfFailed();
        return this;
    }
    
    @Override
    public Future<T> syncUninterruptibly() {
        awaitUninterruptibly();
        rethrowIfFailed();
        return this;
    }
    
    @Override
    public Future<T> await() throws InterruptedException {
        if (isDone()) {
            return this;
        }

        if (Thread.interrupted()) {
            throw new InterruptedException(toString());
        }

        latch.await();
        return this;
    }
    
    @Override
    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        if (isDone()) {
            return true;
        }

        if (Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        return await(timeout, unit);
    }
    
    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        if (isDone()) {
            return true;
        }

        if (Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        return await(timeoutMillis);
    }
    
    @Override
    public Future<T> awaitUninterruptibly() {
        if (isDone()) {
            return this;
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }
    
    /**
     *  set a value and unlock the latch.
     */
    protected boolean setValue0(T value) {
        if (isDone()) {
            return false;
        }
        
        synchronized (this) {
            if (isDone()) {
                return false;
            }
            if (value == null) {
                this.value = SUCCESS;
            } else {
                this.value = value;
            }
            latch.countDown();
        }
        return true;
    }
    
    /**
     * set a exception and unlock the latch.
     */
    protected boolean setFailure0(Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        
        if (isDone()) {
            return false;
        }
        
        synchronized (this) {
            if (isDone()) {
                return false;
            }
            this.value = new ThrowableHolder(cause);
            latch.countDown();
        }
        return true;
    }

    private void rethrowIfFailed() {
        Throwable cause = cause();
        if (cause == null) {
            return;
        }

        PlatformDependent.throwException(cause);
    }

    private static final class ThrowableHolder {
        final Throwable cause;
        ThrowableHolder(Throwable cause) {
            this.cause = cause;
        }
    }
}
