package org.cgfork.grass.common.cache.limit.map;

import org.cgfork.grass.common.cache.limit.LimitedCache;
import org.cgfork.grass.common.cache.limit.RejectedException;
import org.cgfork.grass.common.check.Checker;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class ConcurrentLimitedCache<K, V> implements LimitedCache<K, V> {

    private final ConcurrentMap<K, V> cache;

    private final int limits;

    public ConcurrentLimitedCache(int limits) {
        this(new ConcurrentHashMap<K, V>(limits+1), limits);
    }

    public ConcurrentLimitedCache(ConcurrentMap<K, V> cache, int limits) {
        Checker.Arg.notNull(cache, "cache is null");
        Checker.Arg.in(limits, 0, Integer.MAX_VALUE);
        this.cache = cache;
        this.limits = limits;
    }

    protected boolean isLimitReached() {
        return size() + 1 > limitedSize();
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return cache.containsValue(value);
    }

    @Override
    public V put(K key, V value) throws RejectedException {
        if (isLimitReached()) {
            throw new RejectedException();
        }
        return cache.put(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) throws RejectedException {
        if (isLimitReached()) {
            throw new RejectedException();
        }
        return cache.putIfAbsent(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public int limitedSize() {
        return limits;
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public V remove(K key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public Collection<V> values() {
        return cache.values();
    }
}
