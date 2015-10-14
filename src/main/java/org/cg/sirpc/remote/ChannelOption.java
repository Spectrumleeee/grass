/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-10
 */
package org.cg.sirpc.remote;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 */
public class ChannelOption<T extends Object> implements Comparable<ChannelOption<T>>{
    
    private static final AtomicInteger nextIndex = new AtomicInteger();

    private final int index;
    
    private final String name;

    protected ChannelOption(String name) {
        this.name = name;
        this.index = nextIndex.incrementAndGet();
    }

    @Override
    public int compareTo(ChannelOption<T> other) {
        if (this == other) {
            return 0;
        }
        int c = name.compareTo(other.name);
        if (c !=0) {
            return c;
        }
        
        return ((Integer) index).compareTo(other.index);
    }
    
    private static final ConcurrentMap<String, ChannelOption<?>> names = new ConcurrentHashMap<>();
            
    public static final ChannelOption<Boolean> ENSURE_WRITTEN = valueOf("ensureWritten");
    
    public static final ChannelOption<Long> TIMEOUT_MS = valueOf("timeout");
    
    public static final ChannelOption<Long> CONNECT_TIMEOUT_MS = valueOf("connectTimeout");
    
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> valueOf(String name) {
        checkNotNull(name, "name");
        
        ChannelOption<T> option = (ChannelOption<T>) names.get(name);
        if (option == null) {
            option = new ChannelOption<>(name);
            ChannelOption<?> old = names.putIfAbsent(name, option);
            if (old != null) {
                option = (ChannelOption<T>) old;
            }
        }
        return option;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getOption(ChannelOption<T> option, T defaultValue, RemoteLocator locator) {
        if (option == null) {
            throw new IllegalArgumentException("option is null");
        }
        
        if (locator == null) {
            return defaultValue;
        }
        String value = locator.getParameter(option.name);
        
        if (value == null) {
            return defaultValue;
        }
        
        Class<?> clazz = defaultValue.getClass();
        try {
            Method method = clazz.getMethod("valueOf", String.class);
            return (T) method.invoke(null, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static long getTimeoutMillis(RemoteLocator locator) {
        return getOption(TIMEOUT_MS, Constants.DEFAULT_TIMEOUT, locator);
    }
    
    public static long getConnectTimeoutMillis(RemoteLocator locator){
        return getOption(CONNECT_TIMEOUT_MS, Constants.DEFAULT_CONNECT_TIMEOUT, locator);
    }
    
    public static boolean ensureWritten(RemoteLocator locator) {
        return getOption(ENSURE_WRITTEN, false, locator);
    }
    
    public static <T> T checkNotNull(T arg, String text) {
        if (arg == null) {
            throw new NullPointerException(text);
        }
        return arg;
    }
}
