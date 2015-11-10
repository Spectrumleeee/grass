package org.cgfork.grass.rpc.serialize.json;

import com.fasterxml.jackson.core.JsonParseException;
import org.cgfork.grass.common.serialize.json.JsonDataInput;
import org.cgfork.grass.common.serialize.json.JsonDataOutput;
import org.cgfork.grass.rpc.direct.Flag;
import org.cgfork.grass.rpc.direct.protocol.RemoteMethod;
import org.cgfork.grass.rpc.serialize.ObjectInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class JsonObjectInput extends JsonDataInput implements ObjectInput {

    private final Flag flag;

    public JsonObjectInput(InputStream in, Flag flag) {
        super(in);
        this.flag = flag;
    }

    @Override
    public Object readObject() throws IOException {
        if (flag.isReq()) {
            return readValue(RemoteMethod.class);
        }
        return readValue(Map.class);
    }

    @Override
    public Object readObject(int length) throws IOException {
        if (length == 0) {
            return null;
        }
        byte[] jsonBytes = new byte[length];
        if (getInputStream().read(jsonBytes) < length) {
            throw new IOException("readable bytes is not so long");
        }
        if (flag.isReq()) {
            return readValue(jsonBytes, RemoteMethod.class);
        }
        return readValue(jsonBytes, Map.class);
    }
}
