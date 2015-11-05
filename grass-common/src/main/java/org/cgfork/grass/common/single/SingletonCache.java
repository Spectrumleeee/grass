package org.cgfork.grass.common.single;

import org.cgfork.grass.common.check.Checker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class SingletonCache {

    private final ConcurrentMap<Class<?>, Object> instanceCache = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Class<?>> classCache = new ConcurrentHashMap<>();

    public SingletonCache() {}

    public <T> T get(Class<T> clazz) {
        Checker.Arg.notNull(clazz, "class is null");
        Object instance = instanceCache.get(clazz);

        if (instance == null) {
            return null;
        }

        return clazz.cast(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        Checker.Arg.notNull(name, "name is null");

        Class<?> clazz = classCache.get(name);
        if (clazz == null) {
            return null;
        }
        Object instance = instanceCache.get(clazz);

        if (instance == null) {
            return null;
        }
        return (T) clazz.cast(instance);
    }

    public void register(Object o) {
        Class<?> clazz = o.getClass();

        if (instanceCache.containsKey(clazz)) {
            throw new RegisteredException(clazz.getSimpleName() + " already registered");
        }

        instanceCache.putIfAbsent(clazz, o);

        String className = clazz.getCanonicalName();
        Singleton singletonAnnotation = clazz.getAnnotation(Singleton.class);
        if (singletonAnnotation != null) {
            className = singletonAnnotation.value();
        }
        classCache.putIfAbsent(className, clazz);
    }

    public void remove(String className) {
        Class<?> clazz = classCache.remove(className);

        if (clazz == null) {
            throw new UnRegisteredException(className + " not registered");
        }

        if (instanceCache.remove(clazz) == null ) {
            throw new UnRegisteredException(className + " not registered");
        }
    }

    public void remove(Class<?> clazz) {
        String className = clazz.getCanonicalName();
        Singleton singletonAnnotation = clazz.getAnnotation(Singleton.class);
        if (singletonAnnotation != null) {
            className = singletonAnnotation.value();
        }
        classCache.remove(className);
        if (instanceCache.remove(clazz) == null ) {
            throw new UnRegisteredException(clazz.getSimpleName() + " not registered");
        }
    }

    public void remove(Object o) {
        remove(o.getClass());
    }

    public void clear() {
        instanceCache.clear();
        classCache.clear();
    }

    public static class RegisteredException extends RuntimeException {

        public RegisteredException() { super();}

        public RegisteredException(String info) {
            super(info);
        }

        public RegisteredException(Throwable cause) {
            super(cause);
        }

        public RegisteredException(String info, Throwable cause) {
            super(info, cause);
        }
    }


    public static class UnRegisteredException extends RuntimeException {

        public UnRegisteredException() { super();}

        public UnRegisteredException(String info) {
            super(info);
        }

        public UnRegisteredException(Throwable cause) {
            super(cause);
        }

        public UnRegisteredException(String info, Throwable cause) {
            super(info, cause);
        }
    }

}
