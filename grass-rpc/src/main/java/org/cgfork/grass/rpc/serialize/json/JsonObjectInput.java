package org.cgfork.grass.rpc.serialize.json;

import com.fasterxml.jackson.core.JsonParseException;
import org.cgfork.grass.common.serialize.json.JsonDataInput;
import org.cgfork.grass.common.serialize.json.JsonDataOutput;
import org.cgfork.grass.rpc.direct.Flag;
import org.cgfork.grass.rpc.direct.protocol.RemoteMethod;
import org.cgfork.grass.rpc.direct.protocol.RemoteReturn;
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
public abstract class JsonObjectInput extends JsonDataInput implements ObjectInput {

    public JsonObjectInput(InputStream in) {
        super(in);
    }

    @Override
    public Object readObject() throws IOException {
        return readValue(getTargetClass());
    }

    @Override
    public Object readObject(int length) throws IOException {
        return readObject(length, getTargetClass());
    }

    public <T> T readObject(int length, Class<T> clazz) throws IOException {
        if (length == 0) {
            return null;
        }

        byte[] jsonBytes = new byte[length];
        if (getInputStream().read(jsonBytes) < length) {
            throw new IOException("readable bytes is not so long");
        }

        return readValue(jsonBytes, clazz);
    }

    protected abstract Class<?> getTargetClass();
}
