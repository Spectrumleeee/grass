package org.cgfork.grass.rpc.serialize.text;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.rpc.generic.Flag;
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
@Addon("textSerializer")
public class TextSerializer implements Serializer {
    @Override
    public String getContentType() {
        return "application/text";
    }

    @Override
    public ObjectOutput serialize(Location location, OutputStream output, Flag flag) throws IOException {
        return new TextObjectOutput(output);
    }

    @Override
    public ObjectInput deserialize(Location location, InputStream input, Flag flag) throws IOException {
        return new TextObjectInput(input);
    }
}
