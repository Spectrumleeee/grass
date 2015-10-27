package org.cgfork.grass.remote;

import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public class RemoteLocator {
    
    private final java.net.URL url;
    
    private final Map<String, String> parameters;
    
    public RemoteLocator(String spec) throws MalformedURLException {
        this(new URL(null, spec, new UrlHandler()));
    }
    
    public RemoteLocator(String protocol, String host, int port, String file) throws MalformedURLException {
        this(new URL(protocol, host, port, file, new UrlHandler()));
    }
    
    private RemoteLocator(URL url) {
        this(url, null);
    }
    
    private RemoteLocator(URL url, Map<String, String> parameters) {
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
