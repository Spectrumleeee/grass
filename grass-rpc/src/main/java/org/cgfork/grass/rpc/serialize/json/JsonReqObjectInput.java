package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.rpc.direct.protocol.RemoteMethod;

import java.io.InputStream;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class JsonReqObjectInput extends JsonObjectInput {

    public JsonReqObjectInput(InputStream in) {
        super(in);
    }

    @Override
    protected Class<?> getTargetClass() {
        return RemoteMethod.class;
    }
}
