package org.cgfork.grass.rpc.serialize;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.common.addon.Loader;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.rpc.direct.Flag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
@Addon(type = Addon.Type.Interface)
@Loader(org.cgfork.grass.rpc.serialize.SerializerLoader.class)
public interface Serializer {

    String getContentType();

    ObjectOutput serialize(Location location, OutputStream output, Flag flag) throws IOException;

    ObjectInput deserialize(Location location, InputStream input, Flag flag) throws IOException;
}
