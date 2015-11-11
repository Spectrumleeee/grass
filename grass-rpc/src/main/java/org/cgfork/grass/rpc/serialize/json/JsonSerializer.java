package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.rpc.direct.Flag;
import org.cgfork.grass.rpc.serialize.ObjectInput;
import org.cgfork.grass.rpc.serialize.ObjectOutput;
import org.cgfork.grass.rpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
@Addon("jsonSerializer")
public class JsonSerializer implements Serializer {

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public ObjectOutput serialize(Location location, OutputStream output, Flag flag) throws IOException {
        return new JsonObjectOutput(output);
    }

    @Override
    public ObjectInput deserialize(Location location, InputStream input, Flag flag) throws IOException {
        if (flag.isReq()) {
            return new JsonReqObjectInput(input);
        }
        return new JsonRspObjectInput(input);
    }
}
