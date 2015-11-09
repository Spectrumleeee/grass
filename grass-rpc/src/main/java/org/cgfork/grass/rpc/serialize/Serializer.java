package org.cgfork.grass.rpc.serialize;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.remote.Locator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
@Addon(type = Addon.Type.Interface)
public interface Serializer {

    String getContentType();

    ObjectOutput serialize(Locator locator, OutputStream output) throws IOException;

    ObjectInput deserialize(Locator locator, InputStream input) throws IOException;
}
