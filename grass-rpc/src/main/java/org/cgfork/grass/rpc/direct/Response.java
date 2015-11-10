package org.cgfork.grass.rpc.direct;

import java.io.Serializable;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 4495526581462730986L;

    private long id;

    private String version;

    private Flag flag;

    private Object data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
