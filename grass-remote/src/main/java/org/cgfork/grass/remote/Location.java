package org.cgfork.grass.remote;

import org.cgfork.grass.common.check.Checker;

import java.lang.reflect.Method;
import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class Location {
    
    private final java.net.URL url;
    
    private final Map<String, String> parameters;
    
    public Location(String spec) throws MalformedURLException {
        this(new URL(null, spec, new UrlHandler()));
    }
    
    public Location(String protocol, String host, int port, String file) throws MalformedURLException {
        this(new URL(protocol, host, port, file, new UrlHandler()));
    }
    
    private Location(URL url) {
        this(url, null);
    }
    
    private Location(URL url, Map<String, String> parameters) {
        if (url == null) {
            throw new NullPointerException("uri is null");
        }
        this.url = url;
        if (parameters == null) {
            parameters = new HashMap<>();
        } else {
            parameters = new HashMap<>(parameters);
        }
        parameters.putAll(readParameters(url));
        this.parameters = Collections.unmodifiableMap(parameters);
    }
    
    public URL getUrl() {
        return url;
    }
    
    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public String getParameter(String key) {
        return parameters.get(key);
    }

    public <T> T setParameter(String key, T value) {
        T old = getParameter(key, value);
        parameters.put(key, String.valueOf(value));
        return old;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, T defaultValue) {
        Checker.Arg.notNull(defaultValue);
        Class<T> clazz = (Class<T>)defaultValue.getClass();
        return getParameter(key, defaultValue, clazz);
    }

    public <T> T getParameter(String key, T defaultValue, Class<T> type) {
        Checker.Arg.notNull(defaultValue);
        String value = parameters.get(key);
        if (value == null) {
            return defaultValue;
        }

        Class<?> clazz = defaultValue.getClass();
        try {
            Method method = clazz.getMethod("valueOf", String.class);
            return type.cast(method.invoke(null, value));
        } catch (Exception e) {
            throw new RuntimeException("Failed to cast string to" + clazz, e);
        }
    }

    private static Map<String, String> readParameters(URL url) {
        String query = url.getQuery();
        if (query == null || query.equals("")) {
            return new HashMap<>();
        }
        
        Map<String, String> parameters = new HashMap<>();
        for (String kv : query.split("&")) {
            String []parts = kv.split("=");
            if (parts.length != 2) {
                continue;
            }
            parameters.put(parts[0], parts[1]);
        }
        return parameters;
    }
    
    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(url.getHost(), url.getPort());
    }

    private static class UrlHandler extends URLStreamHandler {

        @Override
        protected URLConnection openConnection(URL u){
            throw new UnsupportedOperationException();
        }

    }
}
