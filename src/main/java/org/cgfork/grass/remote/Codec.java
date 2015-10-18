/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

import java.util.List;
import java.io.IOException;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.common.addon.Loader;
import org.cgfork.grass.common.addon.Addon.Type;

/**
 * 
 */
@Addon(type = Type.Interface)
@Loader(org.cgfork.grass.remote.CodecLoader.class)
public interface Codec {
    void encode(Channel channel, ChannelBuffer out, Object message)
            throws IOException;
    
    boolean decode(Channel channel, ChannelBuffer in, List<Object> out)
            throws IOException;
}
