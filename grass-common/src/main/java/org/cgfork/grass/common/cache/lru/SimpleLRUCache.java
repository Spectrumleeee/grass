package org.cgfork.grass.common.cache.lru;

import java.util.LinkedHashMap;

/**
 * SimpleLRUCache is a simple lru cache. Not thread safe.
 *
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class SimpleLRUCache<K, V> extends LinkedHashMap<K, V> implements LRUCache<K, V> {

    private static final float LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_MAX_CAPACITY = 1000;

    private volatile int capacity;

    public SimpleLRUCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public SimpleLRUCache(int capacity) {
        super(16, LOAD_FACTOR, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}
