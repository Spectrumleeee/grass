package org.cgfork.grass.common.single;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class SingletonContainer {

    private final ConcurrentMap<Class<?>, Object> instanceCache = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Class<?>> classCache = new ConcurrentHashMap<>();

    public <T> T getInstance(Class<T> clazz) {
        Object instance = instanceCache.get(clazz);

        if (instance == null) {
            return null;
        }

        return clazz.cast(instance);
    }
}
