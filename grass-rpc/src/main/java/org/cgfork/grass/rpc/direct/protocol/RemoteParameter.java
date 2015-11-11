package org.cgfork.grass.rpc.direct.protocol;

import java.io.Serializable;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class RemoteParameter implements Serializable {
    private static final long serialVersionUID = 7063832787460058199L;

    private Class<?> type;

    private String name;

    private Object value;

    public RemoteParameter() {

    }

    public RemoteParameter(Object value) {
        this.type = value.getClass();
        this.value = value;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
