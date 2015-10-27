package org.cgfork.grass.common.future;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
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
