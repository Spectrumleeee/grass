package org.cgfork.grass.remote;

import org.cgfork.grass.common.addon.support.SPIAddonLoader;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class CodecLoader extends SPIAddonLoader<Codec> {
    
    @Override
    public Class<Codec> getInterface() {
        return Codec.class;
    }
}
