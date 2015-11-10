package org.cgfork.grass.rpc.serialize;

import org.cgfork.grass.common.addon.support.SPIAddonLoader;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class SerializerLoader extends SPIAddonLoader<Serializer> {
    @Override
    public Class<Serializer> getInterface() {
        return Serializer.class;
    }
}
