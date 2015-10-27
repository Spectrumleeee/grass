package org.cgfork.grass.common.future;

import java.util.LinkedList;
import java.util.List;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 *
 * Implementing {@link #addListener(Listener)} and {@link #removeListener(Listener)}
 * for {@link Future} interface.
 */
public abstract class ListenerFuture<T> extends AbstractFuture<T> {
    
    private List<Listener<? extends Future<? super T>>> listeners;
    
    public ListenerFuture() {
        listeners = new LinkedList<>();
    }

    @Override
    public Future<T> addListener(Listener<? extends Future<? super T>> listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        
        if (isDone()) {
            notifyListener(listener);
            return this;
        }
        
        synchronized(this) {
            if (!isDone()) {
                listeners.add(listener);
                return this;
            }
        }
        notifyListener(listener);
        return this;
    }

    @Override
    public Future<T> removeListener(
            Listener<? extends Future<? super T>> listener) {
        listeners.remove(listener);
        return this;
    }

    public boolean trySuccess(T value) {
        if (setValue0(value)) {
            notifyListeners();
            return true;
        }
        return false;
    }

    public boolean tryFailure(Throwable cause) {
        if (setFailure0(cause)) {
            notifyListeners();
            return true;
        }
        return false;
    }
    
    protected void notifyListeners() {
        for (Listener<? extends Future<? super T>> l : listeners) {
            notifyListener(l, this);
        }
    }
    
    protected void notifyListener(
            final Listener<? extends Future<? super T>> listener) {
        notifyListener(listener, this);
    }

    protected abstract void notifyListener(
            final Listener<? extends Future<? super T>> listener,
            final Future<T> future);

}
