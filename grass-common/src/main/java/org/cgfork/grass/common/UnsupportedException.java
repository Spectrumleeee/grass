package org.cgfork.grass.common;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class UnsupportedException extends RuntimeException {
    private static final long serialVersionUID = -8424759228280288473L;

    public UnsupportedException() {
        super();
    }

    public UnsupportedException(String info) {
        super(info);
    }

    public UnsupportedException(Throwable cause) {
        super(cause);
    }

    public UnsupportedException(String info, Throwable cause) {
        super(info, cause);
    }
}
