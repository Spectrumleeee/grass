package org.cgfork.grass.common.future;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 *
 * AbstractFuture is a implementation for {@link #await()}, {@link #setValue0(Object)}
 * and {@link #setFailure0(Throwable)}
 */
public abstract class AbstractFuture<T> implements Future<T> {
    
    private static final Void SUCCESS = Void.newInstance("success");

    private static final ThrowableHolder CANCELLED_HOLDER = new ThrowableHolder(new CancellationException());

    private short waiters;

    // object lock
    private final Object lock;
    
    private volatile Object value;

    public AbstractFuture() {
        lock = this;
    }
    
    @Override
    public T get() throws InterruptedException, FutureException {
        await();

        Throwable cause = cause();
        if (cause == null) {
            return getNow();
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

            throw new FutureException(cause);
        }
        throw new TimeoutException();
    }

    @Override
    public T get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException {
        if (await(timeoutMillis)) {
            Throwable cause = cause();
            if (cause == null) {
                return getNow();
            }

            throw new FutureException(cause);
        }
        throw new TimeoutException();
    }
    
    @Override
    public boolean isDone() {
        Object o = value;
        return o != null;
    }
    
    /**
     * getNow will return null if the value is a exception or SUCCESS.
     */
    @Override 
    public T getNow() {
        Object value = this.value;
        if (value == null) {
            return null;
        }

        if (value instanceof ThrowableHolder || value == SUCCESS) {
            return null;
        }

        return getTypeClass().cast(value);
    }
    
    @Override
    public Throwable cause() {
        Object value = this.value;
        if (value instanceof ThrowableHolder) {
            ThrowableHolder holder = (ThrowableHolder)value;
            return holder.cause;
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

        synchronized (lock) {
            while (!isDone()) {
                checkDeadLock();
                incWaiters();
                try {
                    lock.wait();
                } finally {
                    decWaiters();
                }
            }
        }
        return this;
    }
    
    @Override
    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return await0(unit.toNanos(timeout), true);
    }
    
    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }
    
    @Override
    public Future<T> awaitUninterruptibly() {
        if (isDone()) {
            return this;
        }

        boolean interrupted = false;
        synchronized (this) {
            while (!isDone()) {
                checkDeadLock();
                incWaiters();
                try {
                    wait();
                } catch (InterruptedException e) {
                    // Interrupted while waiting.
                    interrupted = true;
                } finally {
                    decWaiters();
                }
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        return this;
    }

    private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
        if (isDone()) {
            return true;
        }

        if (timeoutNanos <= 0) {
            return isDone();
        }

        if (interruptable && Thread.interrupted()) {
            throw new InterruptedException(toString());
        }

        long startTime = System.nanoTime();
        long waitTime = timeoutNanos;
        boolean interrupted = false;

        try {
            synchronized (this) {
                if (isDone()) {
                    return true;
                }

                if (waitTime <= 0) {
                    return isDone();
                }

                checkDeadLock();
                incWaiters();
                try {
                    for (;;) {
                        try {
                            wait(waitTime / 1000000, (int) (waitTime % 1000000));
                        } catch (InterruptedException e) {
                            if (interruptable) {
                                throw e;
                            } else {
                                interrupted = true;
                            }
                        }

                        if (isDone()) {
                            return true;
                        } else {
                            waitTime = timeoutNanos - (System.nanoTime() - startTime);
                            if (waitTime <= 0) {
                                return isDone();
                            }
                        }
                    }
                } finally {
                    decWaiters();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public boolean isCancelled() {
        Object value = this.value;
        return value != null
                && value instanceof ThrowableHolder
                && ((ThrowableHolder) value).cause instanceof CancellationException;
    }

    protected boolean cancel0(boolean mayInterruptIfRunning) {
        if (isDone()) {
            return false;
        }

        synchronized (lock) {
            if (isDone() ) {
                return false;
            }

            this.value = CANCELLED_HOLDER;
            if (hasWaiters()) {
                lock.notifyAll();
            }
        }

        return true;
    }

    /**
     *  avoid calling {@link #await()} recursively.
     *
     *  e.g.
     *      A future will be done at the thread called one,
     *      but it may cause dead lock when you call {@link #await()} in this thread.
     */
    protected abstract void checkDeadLock();

    /**
     * Return {@link Class} of T, be used for casting value to T.
     */
    protected abstract Class<T> getTypeClass();
    
    /**
     *  set a value and call {@link #notifyAll()}.
     */
    protected boolean setValue0(T value) {
        if (isDone()) {
            return false;
        }
        
        synchronized (lock) {
            if (isDone()) {
                return false;
            }
            if (value == null) {
                this.value = SUCCESS;
            } else {
                this.value = value;
            }
            if (hasWaiters()) {
                lock.notifyAll();
            }
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
        
        synchronized (lock) {
            if (isDone()) {
                return false;
            }
            this.value = new ThrowableHolder(cause);
            if (hasWaiters()) {
                lock.notifyAll();
            }
        }
        return true;
    }

    private void rethrowIfFailed() {
        Throwable cause = cause();
        if (cause == null) {
            return;
        }

        throw new RuntimeException(cause);
    }

    private boolean hasWaiters() {
        return waiters > 0;
    }

    private void incWaiters() {
        if (waiters == Short.MAX_VALUE) {
            throw new IllegalStateException("too many waiters: " + this);
        }
        waiters++;
    }

    private void decWaiters() {
        waiters--;
    }

    private static class ThrowableHolder {
        final Throwable cause;
        
        ThrowableHolder(Throwable cause) {
            this.cause = cause;
        }
    }

    private static final class Void {

        final String status;

        Void(String status) {
            this.status = status;
        }

        static Void newInstance(String status) {
            return new Void(status);
        }
    }
}
