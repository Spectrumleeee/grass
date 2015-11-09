package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.remote.Locator;
import org.cgfork.grass.rpc.serialize.ObjectInput;
import org.cgfork.grass.rpc.serialize.ObjectOutput;
import org.cgfork.grass.rpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonSerializer implements Serializer {
    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public ObjectOutput serialize(Locator locator, OutputStream output) throws IOException {
        return new JsonObjectOutput(output);
    }

    @Override
    public ObjectInput deserialize(Locator locator, InputStream input) throws IOException {
        return new JsonObjectInput(input);
    }
}
