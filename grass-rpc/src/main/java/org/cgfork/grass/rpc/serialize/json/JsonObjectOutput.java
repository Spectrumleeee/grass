package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.common.serialize.json.JsonDataOutput;
import org.cgfork.grass.rpc.serialize.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonObjectOutput extends JsonDataOutput implements ObjectOutput {
    public JsonObjectOutput(OutputStream out) {
        super(out);
    }

    public JsonObjectOutput(Writer writer) {
        super(writer);
    }

    @Override
    public void writeObject(Object v) throws IOException {
        writeValue(v);
    }
}
