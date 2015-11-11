package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.rpc.direct.protocol.RemoteMethod;
import org.cgfork.grass.rpc.direct.protocol.RemoteReturn;

import java.io.InputStream;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class JsonRspObjectInput extends JsonObjectInput {

    public JsonRspObjectInput(InputStream in) {
        super(in);
    }

    @Override
    protected Class<?> getTargetClass() {
        return RemoteReturn.class;
    }
}
