package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.common.serialize.json.JsonDataInput;
import org.cgfork.grass.rpc.serialize.ObjectInput;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class JsonObjectInput extends JsonDataInput implements ObjectInput {

    private final Class<?> targetClass;

    public JsonObjectInput(InputStream in, Class<?> targetClass) {
        super(in);
        this.targetClass = targetClass;
    }

    @Override
    public Object readObject() throws IOException {
        return readValue(targetClass);
    }

    @Override
    public Object readObject(int length) throws IOException {
        return readObject(length, targetClass);
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
}
