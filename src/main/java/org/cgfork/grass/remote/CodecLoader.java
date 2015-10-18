/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-18
 */
package org.cgfork.grass.remote;

import org.cgfork.grass.common.addon.AbstractLoader;

/**
 * 
 */
public class CodecLoader extends AbstractLoader<Codec> {
    
    @Override
    public Class<Codec> getInterface() {
        return Codec.class;
    }
}
