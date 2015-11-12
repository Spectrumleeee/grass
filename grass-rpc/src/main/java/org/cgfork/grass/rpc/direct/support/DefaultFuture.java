package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.cache.limit.LimitedCache;
import org.cgfork.grass.common.cache.limit.RejectedException;
import org.cgfork.grass.common.cache.limit.map.ConcurrentLimitedCache;
import org.cgfork.grass.common.future.*;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.rpc.direct.GenericFuture;
import org.cgfork.grass.rpc.direct.GenericListener;
import org.cgfork.grass.rpc.direct.GenericRequest;
import org.cgfork.grass.rpc.direct.GenericResponse;

import java.util.TimerTask;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultFuture extends ListenerFuture<GenericResponse> implements GenericFuture {

    private static final LimitedCache<Long, DefaultFuture> futureCache = new ConcurrentLimitedCache<>(10000);

    private static final LimitedCache<Long, Channel> channelCache = new ConcurrentLimitedCache<>(10000);

    private final long id;

    private final Channel channel;

    private final GenericRequest request;

    private final int timeoutMillis;

    public DefaultFuture(Channel channel, GenericRequest request, int timeoutMillis) throws RejectedException {
        this.channel = channel;
        this.request = request;
        this.timeoutMillis = timeoutMillis;
        this.id = request.getId();
        futureCache.put(id, this);
        channelCache.put(id, channel);
    }

    public long getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public GenericRequest getRequest() {
        return request;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    @Override
    public GenericFuture addListener(Listener<? extends Future<? super GenericResponse>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public GenericFuture removeListener(Listener<? extends Future<? super GenericResponse>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    protected void notifyListener(Listener<? extends Future<? super GenericResponse>> listener) {
        GenericListener genericListener = (GenericListener) listener;
        try {
            genericListener.operationComplete(this);
        } catch (Exception e) {
            //TODO:
        }
    }

    @Override
    protected void checkDeadLock() {
        //TODO:
    }

    @Override
    protected Class<GenericResponse> getTypeClass() {
        return GenericResponse.class;
    }

    @Override
    public GenericResponse get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException {
        return super.get(timeoutMillis);
    }

    @Override
    public GenericFuture setFailure(Throwable cause) {
        if (tryFailure(cause)) {
            removeFuture(id);
            return this;
        }
        throw new IllegalStateException("Already done failure");
    }

    @Override
    public GenericFuture setValue(GenericResponse value) {
        if (trySuccess(value)) {
            removeFuture(id);
            return this;
        }
        throw new IllegalStateException("Already done success");
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (super.cancel0(mayInterruptIfRunning)) {
            removeFuture(id);
            notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public GenericFuture sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public GenericFuture syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public GenericFuture await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public GenericFuture awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    private static void removeFuture(long id) {
        futureCache.remove(id);
        channelCache.remove(id);
    }

    public static DefaultFuture getFuture(long id) {
        return futureCache.get(id);
    }

    public static boolean contains(Channel channel) {
        return channelCache.containsValue(channel);
    }

    public static void finishRequested(Channel channel, GenericResponse response) {
        try {
            DefaultFuture future = getFuture(response.getId());
            if (future != null) {
                future.setValue(response);
            } else {
                // TODO: logger
            }
        } finally {
            removeFuture(response.getId());
        }
    }

    public static void caughtRequested(Channel channel, GenericRequest request, Throwable cause) {
        try {
            DefaultFuture future = getFuture(request.getId());
            if (future != null) {
                future.setFailure(cause);
            } else {
                // TODO: logger
            }
        } finally {
            removeFuture(request.getId());
        }
    }

    private static class TimeoutMonitorTask extends TimerTask {
        @Override
        public void run() {
            for (DefaultFuture future : futureCache.values()) {
                if (future == null || future.isDone()) {
                    continue;
                }


            }
        }
    }
}
