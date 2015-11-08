package org.cgfork.grass.common.cache.limit.map;

import org.cgfork.grass.common.cache.limit.RejectedException;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class ConcurrentLimitedCache<K, V> extends SimpleLimitedCache<K, V> {

    private final Lock lock = new ReentrantLock();

    public ConcurrentLimitedCache(Map<K, V> cache, int limits) {
        super(cache, limits);
    }

    @Override
    public V put(K key, V value) throws RejectedException {
        try {
            lock.lock();
            return super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) throws RejectedException {
        try {
            lock.lock();
            return super.putIfAbsent(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(K key) {
        try {
            lock.lock();
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int limitedSize() {
        try {
            lock.lock();
            return super.limitedSize();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.lock();
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(K key) {
        try {
            lock.lock();
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            super.clear();
        } finally {
            lock.unlock();
        }
    }
}
