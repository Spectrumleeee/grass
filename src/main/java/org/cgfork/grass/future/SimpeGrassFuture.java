/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.future;

import java.util.LinkedList;
import java.util.List;


/**
 * 
 */
public abstract class SimpeGrassFuture<T> extends AbstractFuture<T> {
    
    private List<Listener<? extends Future<? super T>>> listeners;
    
    public SimpeGrassFuture() {
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
        return null;
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected static void notifyListener0(Listener listener, Future future) {
        try {
            listener.operationComplete(future);
        } catch (Exception e){
            // TODO: logger
        }
    }
}
