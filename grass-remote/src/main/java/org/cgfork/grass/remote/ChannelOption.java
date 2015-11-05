package org.cgfork.grass.remote;

import org.cgfork.grass.common.check.Checker;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
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
            
    public static final ChannelOption<Boolean> FORCE_WRITTEN = valueOf("ensureWritten");
    
    public static final ChannelOption<Long> TIMEOUT_MS = valueOf("timeout");
    
    public static final ChannelOption<Long> CONNECT_TIMEOUT_MS = valueOf("connectTimeout");

    public static final ChannelOption<Integer> MAX_ACCEPTED_CONNECTIONS = valueOf("maxAcceptedConnections");

    public static final ChannelOption<Integer> IDLE_TIMEOUT = valueOf("idleTimeout");
    
    @SuppressWarnings("unchecked")
    public static <T> ChannelOption<T> valueOf(String name) {
        Checker.Arg.notNull(name, "name is null");
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
    public static <T> T getOption(ChannelOption<T> option, T defaultValue, Locator locator) {
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
    
    public static long timeoutMillis(Locator locator) {
        return getOption(TIMEOUT_MS, Constants.DEFAULT_TIMEOUT, locator);
    }
    
    public static long connectTimeoutMillis(Locator locator){
        return getOption(CONNECT_TIMEOUT_MS, Constants.DEFAULT_CONNECT_TIMEOUT, locator);
    }
    
    public static boolean forceWritten(Locator locator) {
        return getOption(FORCE_WRITTEN, false, locator);
    }

    public static int maxAcceptedConnections(Locator locator) {
        return getOption(MAX_ACCEPTED_CONNECTIONS, Constants.DEFAULT_MAX_ACCEPTED_CONNECTIONS, locator);
    }

    public static int idleTimeout(Locator locator) {
        return getOption(IDLE_TIMEOUT, Constants.DEFAULT_IDLE_TIMEOUT, locator);
    }
}
