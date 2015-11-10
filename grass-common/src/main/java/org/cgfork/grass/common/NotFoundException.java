package org.cgfork.grass.common;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2736206722077825618L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String info) {
        super(info);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String info, Throwable cause) {
        super(info, cause);
    }
}
