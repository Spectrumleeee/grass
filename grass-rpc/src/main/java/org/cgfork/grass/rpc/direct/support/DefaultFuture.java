package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.cache.limit.LimitedCache;
import org.cgfork.grass.common.cache.limit.RejectedException;
import org.cgfork.grass.common.cache.limit.map.ConcurrentLimitedCache;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.common.future.*;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.rpc.direct.GenericListener;
import org.cgfork.grass.rpc.direct.Request;
import org.cgfork.grass.rpc.direct.Response;
import org.cgfork.grass.rpc.direct.ResponseFuture;

import java.util.TimerTask;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultFuture extends ListenerFuture<Response> implements ResponseFuture {

    private static final LimitedCache<Long, DefaultFuture> futureCache = new ConcurrentLimitedCache<>(10000);

    private static final LimitedCache<Long, Channel> channelCache = new ConcurrentLimitedCache<>(10000);

    private final long id;

    private final Channel channel;

    private final Request request;

    public DefaultFuture(Channel channel, Request request) throws RejectedException {
        this.channel = channel;
        this.request = request;
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

    public Request getRequest() {
        return request;
    }

    @Override
    public ResponseFuture addListener(Listener<? extends Future<? super Response>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public ResponseFuture removeListener(Listener<? extends Future<? super Response>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    protected void notifyListener(Listener<? extends Future<? super Response>> listener) {
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
    protected Class<Response> getTypeClass() {
        return Response.class;
    }

    @Override
    public Response get(long timeoutMillis) throws InterruptedException, FutureException, TimeoutException {
        return super.get(timeoutMillis);
    }

    @Override
    public ResponseFuture setFailure(Throwable cause) {
        if (tryFailure(cause)) {
            removeFuture(id);
            return this;
        }
        throw new IllegalStateException("Already done failure");
    }

    @Override
    public ResponseFuture setValue(Response value) {
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
    public ResponseFuture sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public ResponseFuture syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public ResponseFuture await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public ResponseFuture awaitUninterruptibly() {
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

    public static void finishRequested(Channel channel, Response response) {
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

    public static void caughtRequested(Channel channel, Request request, Throwable cause) {
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
