package org.cgfork.grass.common.future;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
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
