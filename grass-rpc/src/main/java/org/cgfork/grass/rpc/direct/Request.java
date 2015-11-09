package org.cgfork.grass.rpc.direct;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class Request implements Serializable {

    private static final long serialVersionUID = -5606578599981996049L;

    private static final AtomicLong REQUEST_ID = new AtomicLong(0);

    private final long id;

    private String version;

    private Object data;

    public Request(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    private static long id() {
        return REQUEST_ID.incrementAndGet();
    }
}
