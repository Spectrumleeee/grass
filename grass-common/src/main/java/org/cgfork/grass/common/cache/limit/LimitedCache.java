package org.cgfork.grass.common.cache.limit;

import java.util.Collection;
import java.util.Set;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface LimitedCache<K, V> {

    boolean containsKey(K key);

    boolean containsValue(V value);

    V put(K key, V value) throws RejectedException;

    V putIfAbsent(K key, V value) throws RejectedException;

    V get(K key);

    V remove(K key);

    int limitedSize();

    int size();

    void clear();

    Set<K> keySet();

    Collection<V> values();
}
