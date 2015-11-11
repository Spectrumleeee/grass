package org.cgfork.grass.rpc.direct.protocol;

import java.io.Serializable;
import java.util.List;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class RemoteMethod implements Serializable {

    private static final long serialVersionUID = -7888030213964362149L;

    private String method;

    private List<RemoteParameter> parameters;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<RemoteParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<RemoteParameter> parameters) {
        this.parameters = parameters;
    }
}
