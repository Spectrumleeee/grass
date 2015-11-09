package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.common.serialize.json.JsonDataInput;
import org.cgfork.grass.rpc.serialize.ObjectInput;

import java.io.InputStream;
import java.io.Reader;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonObjectInput extends JsonDataInput implements ObjectInput {

    public JsonObjectInput(InputStream in) {
        super(in);
    }

    public JsonObjectInput(Reader reader) {
        super(reader);
    }
}
