/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.common.future;

/**
 * 
 */
public class TimeoutException extends Exception {
    private static final long serialVersionUID = 1L;

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TimeoutException(String message) {
        super(message);
    }
    
    public TimeoutException(Throwable cause) {
        super(cause);
    }
    
    public TimeoutException() {
        super();
    }
}
