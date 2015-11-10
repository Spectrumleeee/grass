package org.cgfork.grass.common.cache.lru;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface LRUCache<K, V> {
    boolean containsKey(Object key);

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    int size();

    void clear();

    int getCapacity();
}
