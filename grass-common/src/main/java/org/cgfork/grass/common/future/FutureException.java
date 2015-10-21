/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.common.future;

/**
 * 
 */
public class FutureException extends Exception {
    private static final long serialVersionUID = 1L;

    public FutureException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FutureException(String message) {
        super(message);
    }
    
    public FutureException(Throwable cause) {
        super(cause);
    }
}
