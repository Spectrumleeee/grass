package org.cgfork.grass.rpc.generic.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class RemoteReturn implements Serializable {

    private static final long serialVersionUID = 6796030064521665460L;

    private Map<String, Object> value;

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
}
