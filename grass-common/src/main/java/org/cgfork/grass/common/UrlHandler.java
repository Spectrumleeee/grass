/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.common;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * 
 */
public class UrlHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u){
        throw new UnsupportedOperationException();
    }

}
