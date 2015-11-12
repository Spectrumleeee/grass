package org.cgfork.grass.rpc.generic.support;

import org.cgfork.grass.common.future.*;
import org.cgfork.grass.rpc.generic.CloseFuture;
import org.cgfork.grass.rpc.generic.CloseListener;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultCloseFuture extends ListenerFuture<Void> implements CloseFuture {

    @Override
    public CloseFuture addListener(Listener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public CloseFuture removeListener(Listener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    protected void notifyListener(Listener<? extends Future<? super Void>> listener) {
        CloseListener listener1 = (CloseListener) listener;
        try {
            listener1.operationComplete(this);
        } catch (Exception e) {
            //TODO:
        }
    }

    @Override
    protected void checkDeadLock() {
        //TODO:
    }

    @Override
    protected Class<Void> getTypeClass() {
        return Void.class;
    }

    @Override
    public Void get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException {
        return super.get(timeoutMillis);
    }

    @Override
    public CloseFuture setFailure(Throwable cause) {
        if (tryFailure(cause)) {
            return this;
        }
        throw new IllegalStateException("Already done failure");
    }

    @Override
    public CloseFuture setValue(Void value) {
        if (trySuccess(value)) {
            return this;
        }
        throw new IllegalStateException("Already done success");
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (super.cancel0(mayInterruptIfRunning)) {
            notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public CloseFuture sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public CloseFuture syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public CloseFuture await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public CloseFuture awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }
}
