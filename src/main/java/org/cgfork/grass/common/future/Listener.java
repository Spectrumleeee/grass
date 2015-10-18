/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.common.future;


/**
 * 
 */
public interface Listener <F extends Future<?>> {
    void operationComplete(F future) throws Exception;
}